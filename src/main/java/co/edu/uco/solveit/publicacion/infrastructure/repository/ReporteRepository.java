package co.edu.uco.solveit.publicacion.infrastructure.repository;

import co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion;
import co.edu.uco.solveit.publicacion.infrastructure.entity.Reporte;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    List<Reporte> findByPublicacion(Publicacion publicacion);
    List<Reporte> findByUsuario(Usuario usuario);
    List<Reporte> findByProcesado(boolean procesado);
    long countByPublicacion(Publicacion publicacion);
}
