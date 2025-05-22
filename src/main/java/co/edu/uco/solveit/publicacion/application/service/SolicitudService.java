package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.publicacion.application.dto.CrearSolicitudRequest;
import co.edu.uco.solveit.publicacion.application.dto.SolicitudResponse;
import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.domain.model.Solicitud;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.port.in.SolicitudUseCase;
import co.edu.uco.solveit.publicacion.domain.port.out.SolicitudRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.infrastructure.service.EmailService;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import co.edu.uco.solveit.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService implements SolicitudUseCase {

    private final SolicitudRepositoryPort solicitudRepositoryPort;
    private final PublicacionRepositoryPort publicacionRepositoryPort;
    private final UsuarioApi usuarioApi;
    private final EmailService emailService;

    @Override
    public SolicitudResponse mostraSolicitud(CrearSolicitudRequest request) {
        Long usuarioId = usuarioApi.getCurrentUserId();
        String nombreUsuario = usuarioApi.getCurrentUserFullName();

        Publicacion publicacion = publicacionRepositoryPort.findById(request.publicacionId())
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));

        // Verificar que el usuario interesado no sea el propietario de la publicación,
        // porque no tiene sentido que se autointerese
        if (publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException("No puedes mostrar interés en tu propia publicación");
        }

        // Verificar que no haya expresado interés previamente
        if (solicitudRepositoryPort.existsByPublicacionIdAndUsuarioInteresadoId(publicacion.getId(), usuarioId)) {
            throw new PublicacionException("Ya has mostrado interés en esta publicación");
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

        // Obtener el email del propietario de la publicación para enviar notificación
        Usuario propietario = usuarioApi.findById(publicacion.getUsuarioId())
                .orElseThrow(() -> new PublicacionException("Usuario propietario no encontrado"));

        // Enviar notificación por email al propietario
        emailService.enviarNotificacionNuevoInteres(
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
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));

        // Verificar que sea el propietario de la publicación
        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException("No tienes permiso para ver los intereses de esta publicación");
        }

        List<Solicitud> intereses = solicitudRepositoryPort.findByPublicacionId(publicacionId);
        return intereses.stream()
                .map(this::mapToInteresResponse)
                .toList();
    }

    @Override
    public List<SolicitudResponse> listarSolicitudEnMisPublicaciones() {
        Long usuarioId = usuarioApi.getCurrentUserId();

        // Obtener todas las publicaciones del usuario
        List<Publicacion> publicaciones = publicacionRepositoryPort.findByUsuarioId(usuarioId);

        // Obtener todos los intereses para esas publicaciones
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
                .orElseThrow(() -> new PublicacionException("Interés no encontrado"));

        Publicacion publicacion = publicacionRepositoryPort.findById(solicitud.getPublicacionId())
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));

        // Verificar que sea el propietario de la publicación
        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException("No tienes permiso para aceptar este interés");
        }

        // Verificar que el interés esté pendiente
        if (solicitud.getEstado() != EstadoInteres.PENDIENTE) {
            throw new PublicacionException("Este interés ya ha sido procesado");
        }

        // Actualizar el estado del interés
        solicitud.setEstado(EstadoInteres.ACEPTADO);
        solicitudRepositoryPort.save(solicitud);

        // Aquí se activaría el chat, pero eso sería responsabilidad de otro módulo
        // Por ahora, solo devolvemos un mensaje de éxito

        return MessageResponse.builder()
                .message("Interés aceptado correctamente. Se ha habilitado el chat con el usuario interesado.")
                .success(true)
                .build();
    }

    @Override
    public MessageResponse rechazarSolicitud(Long interesId) {
        Long usuarioId = usuarioApi.getCurrentUserId();

        Solicitud solicitud = solicitudRepositoryPort.findById(interesId)
                .orElseThrow(() -> new PublicacionException("Interés no encontrado"));

        Publicacion publicacion = publicacionRepositoryPort.findById(solicitud.getPublicacionId())
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));

        // Verificar que sea el propietario de la publicación
        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException("No tienes permiso para rechazar este interés");
        }

        // Verificar que el interés esté pendiente
        if (solicitud.getEstado() != EstadoInteres.PENDIENTE) {
            throw new PublicacionException("Este interés ya ha sido procesado");
        }

        // Actualizar el estado del interés
        solicitud.setEstado(EstadoInteres.RECHAZADO);
        solicitudRepositoryPort.save(solicitud);

        // Obtener el email del usuario interesado para enviar notificación
        Usuario usuarioInteresado = usuarioApi.findById(solicitud.getUsuarioInteresadoId())
                .orElseThrow(() -> new PublicacionException("Usuario interesado no encontrado"));

        // Enviar notificación por email al usuario interesado
        emailService.enviarNotificacionInteresRechazado(
                usuarioInteresado.getEmail(),
                publicacion.getTitulo()
        );

        return MessageResponse.builder()
                .message("Interés rechazado correctamente. Se ha notificado al usuario interesado.")
                .success(true)
                .build();
    }

    @Override
    public MessageResponse finalizarSolicitud(Long interesId) {
        Long usuarioId = usuarioApi.getCurrentUserId();

        Solicitud solicitud = solicitudRepositoryPort.findById(interesId)
                .orElseThrow(() -> new PublicacionException("Interés no encontrado"));

        Publicacion publicacion = publicacionRepositoryPort.findById(solicitud.getPublicacionId())
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));

        // Verificar que sea el propietario de la publicación
        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException("No tienes permiso para finalizar este interés");
        }

        // Verificar que el interés esté aceptado
        if (solicitud.getEstado() != EstadoInteres.ACEPTADO) {
            throw new PublicacionException("Solo se pueden finalizar intereses que estén en estado aceptado");
        }

        // Actualizar el estado del interés
        solicitud.setEstado(EstadoInteres.COMPLETADO);
        solicitudRepositoryPort.save(solicitud);

        // Obtener el email del usuario interesado para enviar notificación
        Usuario usuarioInteresado = usuarioApi.findById(solicitud.getUsuarioInteresadoId())
                .orElseThrow(() -> new PublicacionException("Usuario interesado no encontrado"));

        // Enviar notificación por email al usuario interesado
        emailService.enviarNotificacionInteresRechazado(
                usuarioInteresado.getEmail(),
                publicacion.getTitulo()
        );

        return MessageResponse.builder()
                .message("Interés finalizado correctamente. Se ha notificado al usuario interesado.")
                .success(true)
                .build();
    }

    @Override
    public SolicitudResponse obtenerSolicitudPorId(Long solicitudId) {
        Solicitud solicitud = solicitudRepositoryPort.findById(solicitudId)
                .orElseThrow(() -> new PublicacionException("Solicitud no encontrada"));

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
