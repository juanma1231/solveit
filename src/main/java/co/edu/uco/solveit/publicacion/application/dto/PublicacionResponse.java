package co.edu.uco.solveit.publicacion.application.dto;

import co.edu.uco.solveit.publicacion.entity.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.entity.TipoPublicacion;
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