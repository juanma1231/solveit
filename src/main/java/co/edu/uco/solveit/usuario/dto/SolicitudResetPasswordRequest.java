package co.edu.uco.solveit.usuario.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import jakarta.validation.constraints.NotBlank;

public record SolicitudResetPasswordRequest(
        @NotBlank(message = CatalogoDeMensajes.EMAIL_REQUERIDO)
        String email) {
}
