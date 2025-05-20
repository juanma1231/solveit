package co.edu.uco.solveit.publicacion.domain.port.out;

import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.domain.model.Interes;

import java.util.List;
import java.util.Optional;

public interface InteresRepositoryPort {
    Interes save(Interes interes);
    Optional<Interes> findById(Long id);
    List<Interes> findByPublicacionId(Long publicacionId);
    List<Interes> findByPublicacionIdAndEstado(Long publicacionId, EstadoInteres estado);
    List<Interes> findByUsuarioInteresadoId(Long usuarioInteresadoId);
    List<Interes> findByUsuarioInteresadoIdAndEstado(Long usuarioInteresadoId, EstadoInteres estado);
    Optional<Interes> findByPublicacionIdAndUsuarioInteresadoId(Long publicacionId, Long usuarioInteresadoId);
    boolean existsByPublicacionIdAndUsuarioInteresadoId(Long publicacionId, Long usuarioInteresadoId);
    void deleteById(Long id);
}