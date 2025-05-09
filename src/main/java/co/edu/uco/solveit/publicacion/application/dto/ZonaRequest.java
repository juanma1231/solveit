package co.edu.uco.solveit.publicacion.application.dto;

public record ZonaRequest(
    String corregimiento,
    String municipio,
    String ciudad,
    String departamento,
    String pais
) {}