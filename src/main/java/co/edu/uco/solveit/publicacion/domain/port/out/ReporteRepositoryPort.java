package co.edu.uco.solveit.publicacion.domain.port.out;

import co.edu.uco.solveit.publicacion.domain.model.Reporte;

import java.util.List;
import java.util.Optional;

public interface ReporteRepositoryPort {
    Reporte save(Reporte reporte);
    Optional<Reporte> findById(Long id);
    List<Reporte> findByPublicacionId(Long publicacionId);
    List<Reporte> findByUsuarioId(Long usuarioId);
    List<Reporte> findByProcesado(boolean procesado);
    long countByPublicacionId(Long publicacionId);
}
