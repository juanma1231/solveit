package co.edu.uco.solveit.publicacion.infrastructure.repository;

import co.edu.uco.solveit.publicacion.infrastructure.entity.ReporteEntity;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReporteRepository extends JpaRepository<ReporteEntity, Long> {

    List<ReporteEntity> findByUsuario(Usuario usuario);
    List<ReporteEntity> findByProcesado(boolean procesado);
    List<ReporteEntity> findByPublicacion_Id(Long publicacionId);
    long countByPublicacion_Id(Long publicacionId);
    boolean existsByPublicacion_IdAndUsuario_Id(Long publicacionId, Long usuarioId);
}