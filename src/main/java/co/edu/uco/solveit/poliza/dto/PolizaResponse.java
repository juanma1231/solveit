package co.edu.uco.solveit.poliza.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record PolizaResponse(
    Long id,
    Long idTitular,
    String nombreTitular,
    String numeroPoliza,
    String nombreAseguradora,
    Double prima,
    LocalDate fechaEmision,
    LocalDate fechaVencimiento,
    String tipoPoliza,
    String nombreArchivo,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaActualizacion
) {}