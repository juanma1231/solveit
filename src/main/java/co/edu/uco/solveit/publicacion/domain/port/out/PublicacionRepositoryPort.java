package co.edu.uco.solveit.publicacion.domain.port.out;

import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;

import java.util.List;
import java.util.Optional;

public interface PublicacionRepositoryPort {
    Publicacion save(Publicacion publicacion);
    Optional<Publicacion> findById(Long id);
    List<Publicacion> findAll();
    List<Publicacion> findByUsuarioId(Long usuarioId);
    List<Publicacion> findByTipoPublicacion(TipoPublicacion tipoPublicacion);
    List<Publicacion> findByEstado(EstadoPublicacion estado);
    List<Publicacion> findByUsuarioIdAndEstado(Long usuarioId, EstadoPublicacion estado);
    List<Publicacion> findByTipoPublicacionAndEstado(TipoPublicacion tipoPublicacion, EstadoPublicacion estado);
    void deleteById(Long id);
    boolean existsById(Long id);
}
