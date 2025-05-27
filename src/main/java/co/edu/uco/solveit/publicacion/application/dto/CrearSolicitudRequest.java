package co.edu.uco.solveit.publicacion.application.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearSolicitudRequest(
    @NotNull(message = CatalogoDeMensajes.PUBLICACION_ID_REQUERIDO)
    Long publicacionId,

    @NotBlank(message = CatalogoDeMensajes.TITULO_REQUERIDO)
    String titulo,

    @NotBlank(message = CatalogoDeMensajes.DESCRIPCION_REQUERIDA)
    String descripcion
) {}
