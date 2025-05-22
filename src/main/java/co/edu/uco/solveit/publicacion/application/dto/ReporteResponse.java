package co.edu.uco.solveit.publicacion.application.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for report response
 */
@Data
@Builder
public class ReporteResponse {
    private Long id;
    private Long publicacionId;
    private String tituloPublicacion;
    private Long usuarioId;
    private String nombreUsuario;
    private String motivo;
    private LocalDateTime fechaReporte;
    private boolean procesado;
}