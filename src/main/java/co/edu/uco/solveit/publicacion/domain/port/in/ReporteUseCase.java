package co.edu.uco.solveit.publicacion.domain.port.in;

import co.edu.uco.solveit.publicacion.application.dto.ReporteResponse;
import co.edu.uco.solveit.usuario.dto.MessageResponse;

import java.util.List;

/**
 * Interface for report management operations
 */
public interface ReporteUseCase {
    
    /**
     * Cancels a report on a publication, changing its state back to PUBLICADA
     * @param publicacionId The publication ID
     * @return A message response
     */
    MessageResponse cancelarReporte(Long publicacionId);
    
    /**
     * Re-enables a publication to PUBLICADA state
     * @param publicacionId The publication ID
     * @return A message response
     */
    MessageResponse habilitarPublicacion(Long publicacionId);
    
    /**
     * Lists all reports
     * @param procesado Optional filter for processed reports
     * @return The list of reports
     */
    List<ReporteResponse> listarReportes(Boolean procesado);
    
    /**
     * Gets a report by its ID
     * @param reporteId The report ID
     * @return The report response
     */
    ReporteResponse obtenerReportePorId(Long reporteId);
}