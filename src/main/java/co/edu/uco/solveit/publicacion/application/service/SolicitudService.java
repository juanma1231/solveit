package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import co.edu.uco.solveit.publicacion.application.dto.CrearSolicitudRequest;
import co.edu.uco.solveit.publicacion.application.dto.SolicitudResponse;
import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Solicitud;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.port.in.SolicitudUseCase;
import co.edu.uco.solveit.publicacion.domain.port.out.EmailServicePort;
import co.edu.uco.solveit.publicacion.domain.port.out.SolicitudRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import co.edu.uco.solveit.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static co.edu.uco.solveit.publicacion.application.service.PublicacionService.PUBLICACION_NO_ENCONTRADA;

@Service
@RequiredArgsConstructor
public class SolicitudService implements SolicitudUseCase {

    public static final String SOLICITUD_NO_ENCONTRADO = CatalogoDeMensajes.SOLICITUD_NO_ENCONTRADA;
    public static final String USUARIO_INTERESADO_NO_ENCONTRADO = CatalogoDeMensajes.USUARIO_INTERESADO_NO_ENCONTRADO;

    private final SolicitudRepositoryPort solicitudRepositoryPort;
    private final PublicacionRepositoryPort publicacionRepositoryPort;
    private final EmailServicePort emailServicePort;
    private final UsuarioApi usuarioApi;


    @Override
    public SolicitudResponse mostraSolicitud(CrearSolicitudRequest request) {
        Long usuarioId = usuarioApi.getCurrentUserId();
        String nombreUsuario = usuarioApi.getCurrentUserFullName();

        Publicacion publicacion = publicacionRepositoryPort.findById(request.publicacionId())
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));


        if (publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException(CatalogoDeMensajes.NO_INTERES_PROPIA_PUBLICACION);
        }

        if(publicacion.getEstado() != EstadoPublicacion.PUBLICADA){
            throw new PublicacionException(CatalogoDeMensajes.PUBLICACION_NO_PUBLICADA);
        }

        if (solicitudRepositoryPort.existsByPublicacionIdAndUsuarioInteresadoId(publicacion.getId(), usuarioId)) {
            throw new PublicacionException(CatalogoDeMensajes.YA_MOSTRADO_INTERES);
        }

        Solicitud solicitud = Solicitud.builder()
                .publicacionId(publicacion.getId())
                .publicacion(publicacion)
                .usuarioInteresadoId(usuarioId)
                .nombreUsuarioInteresado(nombreUsuario)
                .titulo(request.titulo())
                .descripcion(request.descripcion())
                .estado(EstadoInteres.PENDIENTE)
                .build();

        Solicitud interesGuardado = solicitudRepositoryPort.save(solicitud);

        Usuario propietario = usuarioApi.findById(publicacion.getUsuarioId())
                .orElseThrow(() -> new PublicacionException(CatalogoDeMensajes.USUARIO_PROPIETARIO_NO_ENCONTRADO));

        emailServicePort.enviarNotificacionNuevaSolicitud(
                propietario.getEmail(),
                publicacion.getTitulo(),
                nombreUsuario
        );

        return mapToInteresResponse(interesGuardado);
    }

    @Override
    public List<SolicitudResponse> listarSolicitudPorPublicacion(Long publicacionId) {
        Long usuarioId = usuarioApi.getCurrentUserId();

        Publicacion publicacion = publicacionRepositoryPort.findById(publicacionId)
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));


        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException(CatalogoDeMensajes.SIN_PERMISO_VER_INTERESES);
        }

        List<Solicitud> intereses = solicitudRepositoryPort.findByPublicacionId(publicacionId);
        return intereses.stream()
                .map(this::mapToInteresResponse)
                .toList();
    }

    @Override
    public List<SolicitudResponse> listarSolicitudEnMisPublicaciones() {
        Long usuarioId = usuarioApi.getCurrentUserId();


        List<Publicacion> publicaciones = publicacionRepositoryPort.findByUsuarioId(usuarioId);

        return publicaciones.stream()
                .flatMap(publicacion -> solicitudRepositoryPort.findByPublicacionId(publicacion.getId()).stream())
                .map(this::mapToInteresResponse)
                .toList();
    }

    @Override
    public List<SolicitudResponse> listarMisSolicitud() {
        Long usuarioId = usuarioApi.getCurrentUserId();

        List<Solicitud> intereses = solicitudRepositoryPort.findByUsuarioInteresadoId(usuarioId);
        return intereses.stream()
                .map(this::mapToInteresResponse)
                .toList();
    }

    @Override
    public MessageResponse aceptarSolicitud(Long interesId) {
        Long usuarioId = usuarioApi.getCurrentUserId();

        Solicitud solicitud = solicitudRepositoryPort.findById(interesId)
                .orElseThrow(() -> new PublicacionException(SOLICITUD_NO_ENCONTRADO));

        Publicacion publicacion = publicacionRepositoryPort.findById(solicitud.getPublicacionId())
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));


        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException(CatalogoDeMensajes.SIN_PERMISO_ACEPTAR_INTERES);
        }

        if (solicitud.getEstado() != EstadoInteres.PENDIENTE) {
            throw new PublicacionException(CatalogoDeMensajes.INTERES_YA_PROCESADO);
        }


        solicitud.setEstado(EstadoInteres.ACEPTADO);
        solicitudRepositoryPort.save(solicitud);

        Usuario usuarioInteresado = usuarioApi.findById(solicitud.getUsuarioInteresadoId())
                .orElseThrow(() -> new PublicacionException(USUARIO_INTERESADO_NO_ENCONTRADO));

        emailServicePort.enviarNotificacionSolicitudAceptada(
                usuarioInteresado.getEmail(),
                publicacion.getTitulo()
        );

        return MessageResponse.builder()
                .message(CatalogoDeMensajes.INTERES_ACEPTADO_CORRECTAMENTE)
                .success(true)
                .build();
    }

    @Override
    public MessageResponse rechazarSolicitud(Long interesId) {
        Long usuarioId = usuarioApi.getCurrentUserId();

        Solicitud solicitud = solicitudRepositoryPort.findById(interesId)
                .orElseThrow(() -> new PublicacionException("Interés no encontrado"));

        Publicacion publicacion = publicacionRepositoryPort.findById(solicitud.getPublicacionId())
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));


        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException(CatalogoDeMensajes.SIN_PERMISO_RECHAZAR_INTERES);
        }


        if (solicitud.getEstado() != EstadoInteres.PENDIENTE) {
            throw new PublicacionException(CatalogoDeMensajes.INTERES_YA_PROCESADO);
        }


        solicitud.setEstado(EstadoInteres.RECHAZADO);
        solicitudRepositoryPort.save(solicitud);

        Usuario usuarioInteresado = usuarioApi.findById(solicitud.getUsuarioInteresadoId())
                .orElseThrow(() -> new PublicacionException(USUARIO_INTERESADO_NO_ENCONTRADO));

        emailServicePort.enviarNotificacionSolicitudRechazada(
                usuarioInteresado.getEmail(),
                publicacion.getTitulo()
        );

        return MessageResponse.builder()
                .message(CatalogoDeMensajes.INTERES_RECHAZADO_CORRECTAMENTE)
                .success(true)
                .build();
    }

    @Override
    public MessageResponse finalizarSolicitud(Long interesId) {
        Long usuarioId = usuarioApi.getCurrentUserId();

        Solicitud solicitud = solicitudRepositoryPort.findById(interesId)
                .orElseThrow(() -> new PublicacionException("Interés no encontrado"));

        Publicacion publicacion = publicacionRepositoryPort.findById(solicitud.getPublicacionId())
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));

        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException(CatalogoDeMensajes.SIN_PERMISO_FINALIZAR_INTERES);
        }


        if (solicitud.getEstado() != EstadoInteres.ACEPTADO) {
            throw new PublicacionException(CatalogoDeMensajes.SOLO_FINALIZAR_INTERESES_ACEPTADOS);
        }


        solicitud.setEstado(EstadoInteres.COMPLETADO);
        solicitudRepositoryPort.save(solicitud);


        Usuario usuarioInteresado = usuarioApi.findById(solicitud.getUsuarioInteresadoId())
                .orElseThrow(() -> new PublicacionException(USUARIO_INTERESADO_NO_ENCONTRADO));


        emailServicePort.enviarNotificacionSolicitudRechazada(
                usuarioInteresado.getEmail(),
                publicacion.getTitulo()
        );

        return MessageResponse.builder()
                .message(CatalogoDeMensajes.INTERES_FINALIZADO_CORRECTAMENTE)
                .success(true)
                .build();
    }

    @Override
    public SolicitudResponse obtenerSolicitudPorId(Long solicitudId) {
        Solicitud solicitud = solicitudRepositoryPort.findById(solicitudId)
                .orElseThrow(() -> new PublicacionException(CatalogoDeMensajes.SOLICITUD_NO_ENCONTRADA));

        return mapToInteresResponse(solicitud);
    }

    private SolicitudResponse mapToInteresResponse(Solicitud solicitud) {
        String tituloPublicacion = "";
        if (solicitud.getPublicacion() != null) {
            tituloPublicacion = solicitud.getPublicacion().getTitulo();
        } else if (solicitud.getPublicacionId() != null) {
            tituloPublicacion = publicacionRepositoryPort.findById(solicitud.getPublicacionId())
                    .map(Publicacion::getTitulo)
                    .orElse("");
        }

        return SolicitudResponse.builder()
                .id(solicitud.getId())
                .publicacionId(solicitud.getPublicacionId())
                .tituloPublicacion(tituloPublicacion)
                .usuarioInteresadoId(solicitud.getUsuarioInteresadoId())
                .nombreUsuarioInteresado(solicitud.getNombreUsuarioInteresado())
                .titulo(solicitud.getTitulo())
                .descripcion(solicitud.getDescripcion())
                .estado(solicitud.getEstado())
                .fechaCreacion(solicitud.getFechaCreacion())
                .fechaActualizacion(solicitud.getFechaActualizacion())
                .build();
    }
}
