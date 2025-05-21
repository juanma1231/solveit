package co.edu.uco.solveit.publicacion.infrastructure.repository;


import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.domain.model.Interes;
import co.edu.uco.solveit.publicacion.infrastructure.entity.InteresEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InteresRepository extends JpaRepository<InteresEntity, Long> {
    List<InteresEntity> findByPublicacion(Publicacion publicacion);
    List<InteresEntity> findByPublicacionAndEstado(Publicacion publicacion, EstadoInteres estado);
    List<InteresEntity> findByUsuarioInteresado(Usuario usuarioInteresado);
    List<InteresEntity> findByUsuarioInteresadoAndEstado(Usuario usuarioInteresado, EstadoInteres estado);
    Optional<InteresEntity> findByPublicacionAndUsuarioInteresado(Publicacion publicacion, Usuario usuarioInteresado);
    boolean existsByPublicacionAndUsuarioInteresado(Publicacion publicacion, Usuario usuarioInteresado);
    List<InteresEntity> findByPublicacionIdAndEstadoIn(Long publicacionId, List<EstadoInteres> estados);
}