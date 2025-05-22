package co.edu.uco.solveit.publicacion.application.dto;

import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SolicitudResponse(
    Long id,
    Long publicacionId,
    String tituloPublicacion,
    Long usuarioInteresadoId,
    String nombreUsuarioInteresado,
    EstadoInteres estado,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) {}