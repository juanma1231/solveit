package co.edu.uco.solveit.usuario.dto;

import co.edu.uco.solveit.usuario.entity.Role;
import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String username,
        String email,
        Role role,
        String nombreCompleto,
        String numeroIdentificacion,
        String tipoIdentificacion,
        String descripcionPerfil,
        String telefono) {
}
