package co.edu.uco.solveit.publicacion.infrastructure.repository;


import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.infrastructure.entity.SolicitudEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Long> {
    List<SolicitudEntity> findByPublicacion(PublicacionEntity publicacionEntity);
    List<SolicitudEntity> findByPublicacionAndEstado(PublicacionEntity publicacionEntity, EstadoInteres estado);
    List<SolicitudEntity> findByUsuarioInteresado(Usuario usuarioInteresado);
    List<SolicitudEntity> findByUsuarioInteresadoAndEstado(Usuario usuarioInteresado, EstadoInteres estado);
    Optional<SolicitudEntity> findByPublicacionAndUsuarioInteresado(PublicacionEntity publicacionEntity, Usuario usuarioInteresado);
    boolean existsByPublicacionAndUsuarioInteresado(PublicacionEntity publicacionEntity, Usuario usuarioInteresado);
    List<SolicitudEntity> findByPublicacionIdAndEstadoIn(Long publicacionId, List<EstadoInteres> estados);
}