package co.edu.uco.solveit.usuario.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import jakarta.validation.constraints.NotNull;

public record ActualizarUsuarioRequest(
        @NotNull(message = CatalogoDeMensajes.NOMBRE_COMPLETO_REQUERIDO)
        String nombreCompleto,

        @NotNull(message = CatalogoDeMensajes.PASSWORD_ACTUAL_REQUERIDA)
        String currentPassword,

        @NotNull(message = CatalogoDeMensajes.NUEVA_PASSWORD_REQUERIDA)
        String newPassword,
        String numeroIdentificacion,
        String tipoIdentificacion,
        String descripcionPerfil,
        String telefono
) {
}
