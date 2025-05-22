package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.publicacion.application.dto.CrearInteresRequest;
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
    public SolicitudResponse mostraInteres(CrearInteresRequest request) {
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
    public List<SolicitudResponse> listarInteresesPorPublicacion(Long publicacionId) {
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
    public List<SolicitudResponse> listarInteresesEnMisPublicaciones() {
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
    public List<SolicitudResponse> listarMisIntereses() {
        Long usuarioId = usuarioApi.getCurrentUserId();

        List<Solicitud> intereses = solicitudRepositoryPort.findByUsuarioInteresadoId(usuarioId);
        return intereses.stream()
                .map(this::mapToInteresResponse)
                .toList();
    }

    @Override
    public MessageResponse aceptarInteres(Long interesId) {
        Long usuarioId = usuarioApi.getCurrentUserId();

        Solicitud Solicitud = solicitudRepositoryPort.findById(interesId)
                .orElseThrow(() -> new PublicacionException("Interés no encontrado"));

        Publicacion publicacion = publicacionRepositoryPort.findById(Solicitud.getPublicacionId())
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));

        // Verificar que sea el propietario de la publicación
        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException("No tienes permiso para aceptar este interés");
        }

        // Verificar que el interés esté pendiente
        if (Solicitud.getEstado() != EstadoInteres.PENDIENTE) {
            throw new PublicacionException("Este interés ya ha sido procesado");
        }

        // Actualizar el estado del interés
        Solicitud.setEstado(EstadoInteres.ACEPTADO);
        solicitudRepositoryPort.save(Solicitud);

        // Aquí se activaría el chat, pero eso sería responsabilidad de otro módulo
        // Por ahora, solo devolvemos un mensaje de éxito

        return MessageResponse.builder()
                .message("Interés aceptado correctamente. Se ha habilitado el chat con el usuario interesado.")
                .success(true)
                .build();
    }

    @Override
    public MessageResponse rechazarInteres(Long interesId) {
        Long usuarioId = usuarioApi.getCurrentUserId();

        Solicitud Solicitud = solicitudRepositoryPort.findById(interesId)
                .orElseThrow(() -> new PublicacionException("Interés no encontrado"));

        Publicacion publicacion = publicacionRepositoryPort.findById(Solicitud.getPublicacionId())
                .orElseThrow(() -> new PublicacionException("Publicación no encontrada"));

        // Verificar que sea el propietario de la publicación
        if (!publicacion.getUsuarioId().equals(usuarioId)) {
            throw new PublicacionException("No tienes permiso para rechazar este interés");
        }

        // Verificar que el interés esté pendiente
        if (Solicitud.getEstado() != EstadoInteres.PENDIENTE) {
            throw new PublicacionException("Este interés ya ha sido procesado");
        }

        // Actualizar el estado del interés
        Solicitud.setEstado(EstadoInteres.RECHAZADO);
        solicitudRepositoryPort.save(Solicitud);

        // Obtener el email del usuario interesado para enviar notificación
        Usuario usuarioInteresado = usuarioApi.findById(Solicitud.getUsuarioInteresadoId())
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

    private SolicitudResponse mapToInteresResponse(Solicitud Solicitud) {
        String tituloPublicacion = "";
        if (Solicitud.getPublicacion() != null) {
            tituloPublicacion = Solicitud.getPublicacion().getTitulo();
        } else if (Solicitud.getPublicacionId() != null) {
            tituloPublicacion = publicacionRepositoryPort.findById(Solicitud.getPublicacionId())
                    .map(Publicacion::getTitulo)
                    .orElse("");
        }

        return SolicitudResponse.builder()
                .id(Solicitud.getId())
                .publicacionId(Solicitud.getPublicacionId())
                .tituloPublicacion(tituloPublicacion)
                .usuarioInteresadoId(Solicitud.getUsuarioInteresadoId())
                .nombreUsuarioInteresado(Solicitud.getNombreUsuarioInteresado())
                .estado(Solicitud.getEstado())
                .fechaCreacion(Solicitud.getFechaCreacion())
                .fechaActualizacion(Solicitud.getFechaActualizacion())
                .build();
    }
}