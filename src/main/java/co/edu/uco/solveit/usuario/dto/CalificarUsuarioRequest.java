package co.edu.uco.solveit.usuario.dto;

public record CalificarUsuarioRequest(
        String email,
        int calificacion
) {
}
