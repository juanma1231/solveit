package co.edu.uco.solveit.usuario.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = CatalogoDeMensajes.USERNAME_REQUERIDO)
        String username,

        @NotBlank(message = CatalogoDeMensajes.PASSWORD_REQUERIDO)
        String password) {
}
