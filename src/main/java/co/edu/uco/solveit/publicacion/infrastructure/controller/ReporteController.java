package co.edu.uco.solveit.publicacion.infrastructure.controller;

import co.edu.uco.solveit.publicacion.application.dto.ReporteResponse;
import co.edu.uco.solveit.publicacion.domain.port.in.ReporteUseCase;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for report management
 */
@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteUseCase reporteUseCase;

    /**
     * Cancels a report on a publication, changing its state back to PUBLICADA
     * @param publicacionId The publication ID
     * @return A message response
     */
    @PostMapping("/publicacion/{publicacionId}/cancelar")
    public MessageResponse cancelarReporte(@PathVariable Long publicacionId) {
        return reporteUseCase.cancelarReporte(publicacionId);
    }

    /**
     * Re-enables a publication to PUBLICADA state
     * @param publicacionId The publication ID
     * @return A message response
     */
    @PostMapping("/publicacion/{publicacionId}/habilitar")
    public MessageResponse habilitarPublicacion(@PathVariable Long publicacionId) {
        return reporteUseCase.habilitarPublicacion(publicacionId);
    }

    /**
     * Permanently blocks a publication, changing its state to BLOQUEADA
     * @param publicacionId The publication ID
     * @return A message response
     */
    @PostMapping("/publicacion/{publicacionId}/bloquear")
    public MessageResponse bloquearPublicacion(@PathVariable Long publicacionId) {
        return reporteUseCase.bloquearPublicacion(publicacionId);
    }

    /**
     * Lists all reports
     * @param procesado Optional filter for processed reports
     * @return The list of reports
     */
    @GetMapping
    public List<ReporteResponse> listarReportes(
            @RequestParam(required = false) Boolean procesado) {
        return reporteUseCase.listarReportes(procesado);
    }

    /**
     * Gets a report by its ID
     * @param reporteId The report ID
     * @return The report response
     */
    @GetMapping("/{reporteId}")
    public ReporteResponse obtenerReportePorId(@PathVariable Long reporteId) {
        return reporteUseCase.obtenerReportePorId(reporteId);
    }
}
