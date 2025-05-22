package co.edu.uco.solveit.publicacion.infrastructure.repository;


import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.infrastructure.entity.SolicitudEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Long> {
    List<SolicitudEntity> findByPublicacion(Publicacion publicacion);
    List<SolicitudEntity> findByPublicacionAndEstado(Publicacion publicacion, EstadoInteres estado);
    List<SolicitudEntity> findByUsuarioInteresado(Usuario usuarioInteresado);
    List<SolicitudEntity> findByUsuarioInteresadoAndEstado(Usuario usuarioInteresado, EstadoInteres estado);
    Optional<SolicitudEntity> findByPublicacionAndUsuarioInteresado(Publicacion publicacion, Usuario usuarioInteresado);
    boolean existsByPublicacionAndUsuarioInteresado(Publicacion publicacion, Usuario usuarioInteresado);
    List<SolicitudEntity> findByPublicacionIdAndEstadoIn(Long publicacionId, List<EstadoInteres> estados);
}