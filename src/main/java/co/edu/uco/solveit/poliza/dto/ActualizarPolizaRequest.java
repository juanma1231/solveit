package co.edu.uco.solveit.poliza.dto;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ActualizarPolizaRequest(
    @NotBlank(message = CatalogoDeMensajes.NUMERO_POLIZA_REQUERIDO)
    String numeroPoliza,

    @NotBlank(message = CatalogoDeMensajes.NOMBRE_ASEGURADORA_REQUERIDO)
    String nombreAseguradora,

    @NotNull(message = CatalogoDeMensajes.PRIMA_REQUERIDA)
    Double prima,

    LocalDate fechaEmision,

    @NotNull(message = CatalogoDeMensajes.FECHA_VENCIMIENTO_REQUERIDA)
    LocalDate fechaVencimiento,

    @NotBlank(message = CatalogoDeMensajes.TIPO_POLIZA_REQUERIDO)
    String tipoPoliza
) {}
