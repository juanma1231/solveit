package co.edu.uco.solveit.publicacion.application.dto;

import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;

public record ActualizarPublicacionRequest(
    String titulo,
    String descripcion,
    TipoPublicacion tipoPublicacion,
    String categoriaServicio,
    Long zonaId
) {}