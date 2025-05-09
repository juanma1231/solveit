package co.edu.uco.solveit.publicacion.domain.port.out;

import co.edu.uco.solveit.publicacion.entity.Publicacion;
import co.edu.uco.solveit.publicacion.entity.Reporte;
import co.edu.uco.solveit.usuario.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface ReporteRepositoryPort {
    Reporte save(Reporte reporte);
    Optional<Reporte> findById(Long id);
    List<Reporte> findByPublicacion(Publicacion publicacion);
    List<Reporte> findByUsuario(Usuario usuario);
    List<Reporte> findByProcesado(boolean procesado);
    long countByPublicacion(Publicacion publicacion);
}