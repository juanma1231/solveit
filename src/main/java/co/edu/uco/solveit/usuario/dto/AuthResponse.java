package co.edu.uco.solveit.usuario.dto;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String username,
        String email,
        String nombreCompleto) {
}