package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.publicacion.domain.port.in.PublicacionUseCase;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.ReporteRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.ZonaRepositoryPort;
import co.edu.uco.solveit.publicacion.dto.ActualizarPublicacionRequest;
import co.edu.uco.solveit.publicacion.dto.CrearPublicacionRequest;
import co.edu.uco.solveit.publicacion.dto.PublicacionResponse;
import co.edu.uco.solveit.publicacion.dto.ReportarPublicacionRequest;
import co.edu.uco.solveit.publicacion.entity.*;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import co.edu.uco.solveit.usuario.entity.Usuario;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicacionService implements PublicacionUseCase {

    private final PublicacionRepositoryPort publicacionRepositoryPort;
    private final ZonaRepositoryPort zonaRepositoryPort;
    private final ReporteRepositoryPort reporteRepositoryPort;
    private final UsuarioRepository usuarioRepository;

    @Override
    public PublicacionResponse crearPublicacion(CrearPublicacionRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Zona zona = zonaRepositoryPort.findById(request.zonaId())
                .orElseThrow(() -> new PublicacionException("Zona no encontrada"));

        Publicacion publicacion = Publicacion.builder()
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .usuario(usuario)
                .tipoPublicacion(request.tipoPublicacion())
                .categoriaServicio(request.categoriaServicio())
                .zona(zona)
                .estado(EstadoPublicacion.PUBLICADA)
                .build();

        Publicacion publicacionGuardada = publicacionRepositoryPort.save(publicacion);
        return mapToPublicacionResponse(publicacionGuardada);
    }

    @Override
    public PublicacionResponse actualizarPublicacion(Long id, ActualizarPublicacionRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Publicacion publicacion = publicacionRepositoryPort.findById(id)
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));

        if (!publicacion.getUsuario().getId().equals(usuario.getId())) {
            throw new PublicacionException("No tienes permiso para actualizar esta publicación");
        }

        if (publicacion.getEstado() == EstadoPublicacion.CANCELADA ||
                publicacion.getEstado() == EstadoPublicacion.BLOQUEADA) {
            throw new PublicacionException("No se puede actualizar una publicación cancelada o bloqueada");
        }

        Zona zona = zonaRepositoryPort.findById(request.zonaId())
                .orElseThrow(() -> new PublicacionException("Zona no encontrada"));

        publicacion.setTitulo(request.titulo());
        publicacion.setDescripcion(request.descripcion());
        publicacion.setTipoPublicacion(request.tipoPublicacion());
        publicacion.setCategoriaServicio(request.categoriaServicio());
        publicacion.setZona(zona);

        Publicacion publicacionActualizada = publicacionRepositoryPort.save(publicacion);
        return mapToPublicacionResponse(publicacionActualizada);
    }

    @Override
    public PublicacionResponse obtenerPublicacion(Long id) {
        Publicacion publicacion = publicacionRepositoryPort.findById(id)
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));
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
                .collect(Collectors.toList());
    }

    @Override
    public List<PublicacionResponse> listarMisPublicaciones() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<Publicacion> publicaciones = publicacionRepositoryPort.findByUsuario(usuario);
        return publicaciones.stream()
                .map(this::mapToPublicacionResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MessageResponse cancelarPublicacion(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Publicacion publicacion = publicacionRepositoryPort.findById(id)
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));

        if (!publicacion.getUsuario().getId().equals(usuario.getId())) {
            throw new PublicacionException("No tienes permiso para cancelar esta publicación");
        }

        if (publicacion.getEstado() == EstadoPublicacion.CANCELADA) {
            throw new PublicacionException("La publicación ya está cancelada");
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Publicacion publicacion = publicacionRepositoryPort.findById(id)
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));

        Reporte reporte = Reporte.builder()
                .publicacion(publicacion)
                .usuario(usuario)
                .motivo(request.motivo())
                .build();

        reporteRepositoryPort.save(reporte);

        // Si hay más de 3 reportes, marcar la publicación como reportada
        long cantidadReportes = reporteRepositoryPort.countByPublicacion(publicacion);
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
                .usuarioId(publicacion.getUsuario().getId())
                .nombreUsuario(publicacion.getUsuario().getNombreCompleto())
                .tipoPublicacion(publicacion.getTipoPublicacion())
                .categoriaServicio(publicacion.getCategoriaServicio())
                .zonaId(publicacion.getZona().getId())
                .ubicacionCompleta(ubicacionCompleta)
                .estado(publicacion.getEstado())
                .fechaCreacion(publicacion.getFechaCreacion())
                .fechaActualizacion(publicacion.getFechaActualizacion())
                .build();
    }
}