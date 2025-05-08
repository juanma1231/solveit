package co.edu.uco.solveit.usuario.dto;

public record RegistroRequest(
        String username,
        String password,
        String email,
        String nombreCompleto) {

}