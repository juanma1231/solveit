package co.edu.uco.solveit.poliza.dto;

import java.time.LocalDate;

public record RegistrarPolizaRequest(
    String numeroPoliza,
    String nombreAseguradora,
    Double prima,
    LocalDate fechaEmision,
    LocalDate fechaVencimiento,
    String tipoPoliza
) {}