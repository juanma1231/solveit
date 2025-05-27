package co.edu.uco.solveit.publicacion.application.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearPublicacionRequest(
    @NotBlank(message = CatalogoDeMensajes.TITULO_REQUERIDO)
    String titulo,

    @NotBlank(message = CatalogoDeMensajes.DESCRIPCION_REQUERIDA)
    String descripcion,

    @NotNull(message = CatalogoDeMensajes.TIPO_PUBLICACION_REQUERIDO)
    TipoPublicacion tipoPublicacion,

    @NotBlank(message = CatalogoDeMensajes.CATEGORIA_SERVICIO_REQUERIDA)
    String categoriaServicio,

    @NotNull(message = CatalogoDeMensajes.ZONA_ID_REQUERIDO)
    Long zonaId
) {}
