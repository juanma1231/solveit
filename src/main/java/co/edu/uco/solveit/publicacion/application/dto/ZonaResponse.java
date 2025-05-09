package co.edu.uco.solveit.publicacion.application.dto;

import lombok.Builder;

@Builder
public record ZonaResponse(
    Long id,
    String corregimiento,
    String municipio,
    String ciudad,
    String departamento,
    String pais
) {}