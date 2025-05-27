package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.publicacion.application.dto.ActualizarPublicacionRequest;
import co.edu.uco.solveit.publicacion.application.dto.CrearPublicacionRequest;
import co.edu.uco.solveit.publicacion.application.dto.PublicacionResponse;
import co.edu.uco.solveit.publicacion.application.dto.ReportarPublicacionRequest;
import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Solicitud;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.Reporte;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.domain.port.in.PublicacionUseCase;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.ReporteRepositoryPort;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.publicacion.domain.port.out.ZonaRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.SolicitudRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicacionService implements PublicacionUseCase {

    public static final String PUBLICACION_NO_ENCONTRADA = "Publicación no encontrada";
    private final PublicacionRepositoryPort publicacionRepositoryPort;
    private final ZonaRepositoryPort zonaRepositoryPort;
    private final ReporteRepositoryPort reporteRepositoryPort;
    private final SolicitudRepositoryPort solicitudRepositoryPort;
    private final UsuarioApi usuarioApi;

    @Override
    public PublicacionResponse crearPublicacion(CrearPublicacionRequest request) {
        Long usuarioId = usuarioApi.getCurrentUserId();
        String nombreUsuario = usuarioApi.getCurrentUserFullName();

        Zona zona = zonaRepositoryPort.findById(request.zonaId())
                .orElseThrow(() -> new PublicacionException("Zona no encontrada"));

        Publicacion publicacion = Publicacion.builder()
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .usuarioId(usuarioId)
                .nombreUsuario(nombreUsuario)
                .tipoPublicacion(request.tipoPublicacion())
                .categoriaServicio(request.categoriaServicio())
                .zonaId(zona.getId())
                .zona(zona)
                .estado(EstadoPublicacion.PUBLICADA)
                .build();

        Publicacion publicacionGuardada = publicacionRepositoryPort.save(publicacion);
        return mapToPublicacionResponse(publicacionGuardada);
    }

    @Override
    public PublicacionResponse actualizarPublicacion(Long id, ActualizarPublicacionRequest request) {
        Long usuarioId = usuarioApi.getCurrentUserId();

        Publicacion publicacion = publicacionRepositoryPort.findById(id)
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));

        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException("No tienes permiso para actualizar esta publicación");
        }

        if (publicacion.getEstado() == EstadoPublicacion.CANCELADA ||
                publicacion.getEstado() == EstadoPublicacion.BLOQUEADA) {
            throw new PublicacionException("No se puede actualizar una publicación cancelada o bloqueada");
        }

        List<EstadoInteres> estadosVigentes = List.of(EstadoInteres.PENDIENTE, EstadoInteres.ACEPTADO);
        List<Solicitud> interesesVigentes = solicitudRepositoryPort.findByPublicacionIdAndEstadoIn(id, estadosVigentes);

        if (!interesesVigentes.isEmpty()) {
            throw new PublicacionException("No se puede actualizar una publicación que tiene intereses vigentes");
        }

        Zona zona = zonaRepositoryPort.findById(request.zonaId())
                .orElseThrow(() -> new PublicacionException("Zona no encontrada"));

        publicacion.setTitulo(request.titulo());
        publicacion.setDescripcion(request.descripcion());
        publicacion.setTipoPublicacion(request.tipoPublicacion());
        publicacion.setCategoriaServicio(request.categoriaServicio());
        publicacion.setZonaId(zona.getId());
        publicacion.setZona(zona);

        Publicacion publicacionActualizada = publicacionRepositoryPort.save(publicacion);
        return mapToPublicacionResponse(publicacionActualizada);
    }

    @Override
    public PublicacionResponse obtenerPublicacion(Long id) {
        Publicacion publicacion = publicacionRepositoryPort.findById(id)
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));
        return mapToPublicacionResponse(publicacion);
    }

    @Override
    public List<PublicacionResponse> listarPublicaciones(TipoPublicacion tipoPublicacion) {
        List<Publicacion> publicaciones;
        if (tipoPublicacion != null) {
            publicaciones = publicacionRepositoryPort.findByTipoPublicacionAndEstado(
                    tipoPublicacion, EstadoPublicacion.PUBLICADA);
        } else {
            publicaciones = publicacionRepositoryPort.findByEstado(EstadoPublicacion.PUBLICADA);
        }
        return publicaciones.stream()
                .map(this::mapToPublicacionResponse)
                .toList();
    }

    @Override
    public List<PublicacionResponse> listarMisPublicaciones() {
        Long usuarioId = usuarioApi.getCurrentUserId();

        List<Publicacion> publicaciones = publicacionRepositoryPort.findByUsuarioId(usuarioId);
        return publicaciones.stream()
                .map(this::mapToPublicacionResponse)
                .toList();
    }

    @Override
    public MessageResponse cancelarPublicacion(Long id) {
        Long usuarioId = usuarioApi.getCurrentUserId();

        Publicacion publicacion = publicacionRepositoryPort.findById(id)
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));

        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException("No tienes permiso para cancelar esta publicación");
        }

        if (publicacion.getEstado() == EstadoPublicacion.CANCELADA) {
            throw new PublicacionException("La publicación ya está cancelada");
        }


        List<EstadoInteres> estadosVigentes = List.of(EstadoInteres.ACEPTADO);
        List<Solicitud> interesesVigentes = solicitudRepositoryPort.findByPublicacionIdAndEstadoIn(id, estadosVigentes);

        if (!interesesVigentes.isEmpty()) {
            throw new PublicacionException("No se puede cancelar una publicación que tiene intereses vigentes");
        }

        publicacion.setEstado(EstadoPublicacion.CANCELADA);
        publicacionRepositoryPort.save(publicacion);

        return MessageResponse.builder()
                .message("Publicación cancelada correctamente")
                .success(true)
                .build();
    }

    @Override
    public MessageResponse reportarPublicacion(Long id, ReportarPublicacionRequest request) {
        Long usuarioId = usuarioApi.getCurrentUserId();
        String nombreUsuario = usuarioApi.getCurrentUserFullName();

        Publicacion publicacion = publicacionRepositoryPort.findById(id)
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));

        if (reporteRepositoryPort.existsByPublicacionIdAndUsuarioId(publicacion.getId(), usuarioId)) {
            throw new PublicacionException("Ya has reportado esta publicación anteriormente");
        }

        Reporte reporte = Reporte.builder()
                .publicacionId(publicacion.getId())
                .publicacion(publicacion)
                .usuarioId(usuarioId)
                .nombreUsuario(nombreUsuario)
                .motivo(request.motivo())
                .build();

        reporteRepositoryPort.save(reporte);

        long cantidadReportes = reporteRepositoryPort.countByPublicacionId(publicacion.getId());
        if (cantidadReportes >= 3 && publicacion.getEstado() != EstadoPublicacion.REPORTADA) {
            publicacion.setEstado(EstadoPublicacion.REPORTADA);
            publicacionRepositoryPort.save(publicacion);
        }

        return MessageResponse.builder()
                .message("Publicación reportada correctamente")
                .success(true)
                .build();
    }

    private PublicacionResponse mapToPublicacionResponse(Publicacion publicacion) {
        String ubicacionCompleta = String.format("%s, %s, %s, %s, %s",
                publicacion.getZona().getCorregimiento(),
                publicacion.getZona().getMunicipio(),
                publicacion.getZona().getCiudad(),
                publicacion.getZona().getDepartamento(),
                publicacion.getZona().getPais());

        return PublicacionResponse.builder()
                .id(publicacion.getId())
                .titulo(publicacion.getTitulo())
                .descripcion(publicacion.getDescripcion())
                .usuarioId(publicacion.getUsuarioId())
                .nombreUsuario(publicacion.getNombreUsuario())
                .tipoPublicacion(publicacion.getTipoPublicacion())
                .categoriaServicio(publicacion.getCategoriaServicio())
                .zonaId(publicacion.getZonaId())
                .ubicacionCompleta(ubicacionCompleta)
                .estado(publicacion.getEstado())
                .fechaCreacion(publicacion.getFechaCreacion())
                .fechaActualizacion(publicacion.getFechaActualizacion())
                .build();
    }
}
