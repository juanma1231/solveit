package co.edu.uco.solveit.publicacion.application.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import jakarta.validation.constraints.NotBlank;

public record ZonaRequest(
    @NotBlank(message = CatalogoDeMensajes.CORREGIMIENTO_REQUERIDO)
    String corregimiento,

    @NotBlank(message = CatalogoDeMensajes.MUNICIPIO_REQUERIDO)
    String municipio,

    @NotBlank(message = CatalogoDeMensajes.CIUDAD_REQUERIDA)
    String ciudad,

    @NotBlank(message = CatalogoDeMensajes.DEPARTAMENTO_REQUERIDO)
    String departamento,

    @NotBlank(message = CatalogoDeMensajes.PAIS_REQUERIDO)
    String pais
) {}
