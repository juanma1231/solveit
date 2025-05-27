package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import co.edu.uco.solveit.publicacion.application.dto.ReporteResponse;
import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.Reporte;
import co.edu.uco.solveit.publicacion.domain.port.in.ReporteUseCase;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.ReporteRepositoryPort;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static co.edu.uco.solveit.publicacion.application.service.PublicacionService.PUBLICACION_NO_ENCONTRADA;
@Service
@RequiredArgsConstructor
public class ReporteService implements ReporteUseCase {

    private final ReporteRepositoryPort reporteRepositoryPort;
    private final PublicacionRepositoryPort publicacionRepositoryPort;
    private final UsuarioApi usuarioApi;

    @Override
    public MessageResponse cancelarReporte(Long publicacionId) {

        Publicacion publicacion = publicacionRepositoryPort.findById(publicacionId)
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));

        if (publicacion.getEstado() != EstadoPublicacion.REPORTADA) {
            throw new PublicacionException(CatalogoDeMensajes.PUBLICACION_NO_REPORTADA);
        }

        publicacion.setEstado(EstadoPublicacion.PUBLICADA);
        publicacionRepositoryPort.save(publicacion);

        marcarComoProcesadoLosReportesBy(publicacionId);

        return MessageResponse.builder()
                .message(CatalogoDeMensajes.REPORTES_CANCELADOS_PUBLICACION_HABILITADA)
                .success(true)
                .build();
    }

    @Override
    public MessageResponse habilitarPublicacion(Long publicacionId) {


        Publicacion publicacion = publicacionRepositoryPort.findById(publicacionId)
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));

        if (publicacion.getEstado() == EstadoPublicacion.PUBLICADA) {
            throw new PublicacionException(CatalogoDeMensajes.PUBLICACION_YA_HABILITADA);
        }

        publicacion.setEstado(EstadoPublicacion.PUBLICADA);
        publicacionRepositoryPort.save(publicacion);

        marcarComoProcesadoLosReportesBy(publicacionId);

        return MessageResponse.builder()
                .message(CatalogoDeMensajes.PUBLICACION_HABILITADA_CORRECTAMENTE)
                .success(true)
                .build();
    }

    @Override
    public List<ReporteResponse> listarReportes(Boolean procesado) {
        usuarioApi.getCurrentUserId();

        List<Reporte> reportes;
        if (procesado != null) {
            reportes = reporteRepositoryPort.findByProcesado(procesado);
        } else {

            List<Reporte> reportesNoProcesados = reporteRepositoryPort.findByProcesado(false);
            reportes = new ArrayList<>(reportesNoProcesados);
        }


        reportes = reportes.stream()
                .filter(reporte -> {
                    if (reporte.getPublicacionId() != null) {
                        return publicacionRepositoryPort.findById(reporte.getPublicacionId())
                                .map(publicacion -> publicacion.getEstado() == EstadoPublicacion.REPORTADA)
                                .orElse(false);
                    }
                    return false;
                })
                .toList();

        return reportes.stream()
                .map(this::mapToReporteResponse)
                .toList();
    }

    @Override
    public ReporteResponse obtenerReportePorId(Long reporteId) {
        usuarioApi.getCurrentUserId();

        Reporte reporte = reporteRepositoryPort.findById(reporteId)
                .orElseThrow(() -> new PublicacionException(CatalogoDeMensajes.REPORTE_NO_ENCONTRADO));

        return mapToReporteResponse(reporte);
    }

    @Override
    public MessageResponse bloquearPublicacion(Long publicacionId) {
        usuarioApi.getCurrentUserId();

        Publicacion publicacion = publicacionRepositoryPort.findById(publicacionId)
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));

        publicacion.setEstado(EstadoPublicacion.BLOQUEADA);
        publicacionRepositoryPort.save(publicacion);

        marcarComoProcesadoLosReportesBy(publicacionId);

        return MessageResponse.builder()
                .message(CatalogoDeMensajes.PUBLICACION_BLOQUEADA_PERMANENTEMENTE)
                .success(true)
                .build();
    }

    private void marcarComoProcesadoLosReportesBy(Long publicacionId) {
        List<Reporte> reportes = reporteRepositoryPort.findByPublicacionId(publicacionId);
        Publicacion publicacion = publicacionRepositoryPort.findById(publicacionId)
                .orElseThrow(() -> new PublicacionException(PUBLICACION_NO_ENCONTRADA));
        for (Reporte reporte : reportes) {
            reporte.setProcesado(true);
            reporte.setPublicacion(publicacion);
            reporteRepositoryPort.save(reporte);
        }
    }

    private ReporteResponse mapToReporteResponse(Reporte reporte) {
        String tituloPublicacion = "";
        if (reporte.getPublicacion() != null) {
            tituloPublicacion = reporte.getPublicacion().getTitulo();
        } else if (reporte.getPublicacionId() != null) {
            tituloPublicacion = publicacionRepositoryPort.findById(reporte.getPublicacionId())
                    .map(Publicacion::getTitulo)
                    .orElse("");
        }

        return ReporteResponse.builder()
                .id(reporte.getId())
                .publicacionId(reporte.getPublicacionId())
                .tituloPublicacion(tituloPublicacion)
                .usuarioId(reporte.getUsuarioId())
                .nombreUsuario(reporte.getNombreUsuario())
                .motivo(reporte.getMotivo())
                .fechaReporte(reporte.getFechaReporte())
                .procesado(reporte.isProcesado())
                .build();
    }
}
