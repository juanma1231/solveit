package co.edu.uco.solveit.publicacion.infrastructure.repository;

import co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion;
import co.edu.uco.solveit.publicacion.infrastructure.entity.TipoPublicacion;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
    List<Publicacion> findByUsuario(Usuario usuario);
    List<Publicacion> findByTipoPublicacion(TipoPublicacion tipoPublicacion);
    List<Publicacion> findByEstado(EstadoPublicacion estado);
    List<Publicacion> findByUsuarioAndEstado(Usuario usuario, EstadoPublicacion estado);
    List<Publicacion> findByTipoPublicacionAndEstado(TipoPublicacion tipoPublicacion, EstadoPublicacion estado);
}
