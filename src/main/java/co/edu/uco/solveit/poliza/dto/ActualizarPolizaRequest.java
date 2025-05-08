package co.edu.uco.solveit.poliza.dto;

import java.time.LocalDate;

public record ActualizarPolizaRequest(
    String numeroPoliza,
    String nombreAseguradora,
    Double prima,
    LocalDate fechaEmision,
    LocalDate fechaVencimiento,
    String tipoPoliza
) {}