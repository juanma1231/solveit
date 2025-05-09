package co.edu.uco.solveit.publicacion.application.dto;

import co.edu.uco.solveit.publicacion.entity.TipoPublicacion;

public record CrearPublicacionRequest(
    String titulo,
    String descripcion,
    TipoPublicacion tipoPublicacion,
    String categoriaServicio,
    Long zonaId
) {}