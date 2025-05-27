package co.edu.uco.solveit.publicacion.application.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import jakarta.validation.constraints.NotBlank;

public record ReportarPublicacionRequest(
    @NotBlank(message = CatalogoDeMensajes.MOTIVO_REQUERIDO)
    String motivo
) {}
