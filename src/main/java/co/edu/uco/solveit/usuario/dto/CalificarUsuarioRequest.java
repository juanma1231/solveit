package co.edu.uco.solveit.usuario.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CalificarUsuarioRequest(
        @NotNull(message = CatalogoDeMensajes.ID_REQUERIDO)
        Long id,

        @Min(value = 1, message = CatalogoDeMensajes.CALIFICACION_MINIMA)
        @Max(value = 5, message = CatalogoDeMensajes.CALIFICACION_MAXIMA)
        int calificacion
) {
}
