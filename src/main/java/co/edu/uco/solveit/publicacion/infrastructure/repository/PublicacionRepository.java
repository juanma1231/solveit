package co.edu.uco.solveit.publicacion.infrastructure.repository;

import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<PublicacionEntity, Long> {
    List<PublicacionEntity> findByUsuario(Usuario usuario);
    List<PublicacionEntity> findByTipoPublicacion(TipoPublicacion tipoPublicacion);
    List<PublicacionEntity> findByEstado(EstadoPublicacion estado);
    List<PublicacionEntity> findByUsuarioAndEstado(Usuario usuario, EstadoPublicacion estado);
    List<PublicacionEntity> findByTipoPublicacionAndEstado(TipoPublicacion tipoPublicacion, EstadoPublicacion estado);
}
