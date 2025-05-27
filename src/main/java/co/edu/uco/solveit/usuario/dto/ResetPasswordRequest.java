package co.edu.uco.solveit.usuario.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @NotBlank(message = CatalogoDeMensajes.TOKEN_REQUERIDO)
        String token,

        @NotBlank(message = CatalogoDeMensajes.NUEVA_PASSWORD_REQUERIDA)
        String newPassword) {
}
