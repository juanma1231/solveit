package co.edu.uco.solveit.usuario.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import jakarta.validation.constraints.NotBlank;


public record RegistroRequest(
        @NotBlank(message = CatalogoDeMensajes.USERNAME_REQUERIDO)
        String username,

        @NotBlank(message = CatalogoDeMensajes.PASSWORD_REQUERIDO)
        String password,

        @NotBlank(message = CatalogoDeMensajes.EMAIL_REQUERIDO)
        String email,

        @NotBlank(message = CatalogoDeMensajes.NOMBRE_COMPLETO_REQUERIDO)
        String nombreCompleto,

        @NotBlank(message = CatalogoDeMensajes.NUMERO_IDENTIFICACION_REQUERIDO)
        String numeroIdentificacion,

        @NotBlank(message = CatalogoDeMensajes.TIPO_IDENTIFICACION_REQUERIDO)
        String tipoIdentificacion,

        String descripcionPerfil,

        @NotBlank(message = CatalogoDeMensajes.TELEFONO_REQUERIDO)
        String telefono) {

}
