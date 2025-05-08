package co.edu.uco.solveit.usuario.dto;


public record ActualizarUsuarioRequest(
        String nombreCompleto,
        String email,
        String currentPassword,
        String newPassword,
        String numeroIdentificacion,
        String tipoIdentificacion,
        String descripcionPerfil,
        String telefono
) {
}
