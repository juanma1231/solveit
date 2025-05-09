package co.edu.uco.solveit.publicacion.domain.port.out;

import co.edu.uco.solveit.publicacion.entity.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.entity.Publicacion;
import co.edu.uco.solveit.publicacion.entity.TipoPublicacion;
import co.edu.uco.solveit.usuario.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface PublicacionRepositoryPort {
    Publicacion save(Publicacion publicacion);
    Optional<Publicacion> findById(Long id);
    List<Publicacion> findAll();
    List<Publicacion> findByUsuario(Usuario usuario);
    List<Publicacion> findByTipoPublicacion(TipoPublicacion tipoPublicacion);
    List<Publicacion> findByEstado(EstadoPublicacion estado);
    List<Publicacion> findByUsuarioAndEstado(Usuario usuario, EstadoPublicacion estado);
    List<Publicacion> findByTipoPublicacionAndEstado(TipoPublicacion tipoPublicacion, EstadoPublicacion estado);
    void deleteById(Long id);
    boolean existsById(Long id);
}