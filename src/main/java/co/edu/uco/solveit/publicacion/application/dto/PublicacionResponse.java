package co.edu.uco.solveit.publicacion.application.dto;

import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PublicacionResponse(
    Long id,
    String titulo,
    String descripcion,
    Long usuarioId,
    String nombreUsuario,
    TipoPublicacion tipoPublicacion,
    String categoriaServicio,
    Long zonaId,
    String ubicacionCompleta,
    EstadoPublicacion estado,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) {}