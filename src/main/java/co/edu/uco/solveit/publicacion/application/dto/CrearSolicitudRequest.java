package co.edu.uco.solveit.publicacion.application.dto;

public record CrearSolicitudRequest(
    Long publicacionId,
    String titulo,
    String descripcion
) {}
