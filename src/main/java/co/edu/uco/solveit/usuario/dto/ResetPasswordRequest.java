package co.edu.uco.solveit.usuario.dto;

public record ResetPasswordRequest(String token, String newPassword) {
}