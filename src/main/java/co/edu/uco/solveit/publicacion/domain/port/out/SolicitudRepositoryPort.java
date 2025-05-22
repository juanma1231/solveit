package co.edu.uco.solveit.publicacion.domain.port.out;

import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.domain.model.Solicitud;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepositoryPort {
    Solicitud save(Solicitud Solicitud);
    Optional<Solicitud> findById(Long id);
    List<Solicitud> findByPublicacionId(Long publicacionId);
    List<Solicitud> findByPublicacionIdAndEstado(Long publicacionId, EstadoInteres estado);
    List<Solicitud> findByPublicacionIdAndEstadoIn(Long publicacionId, List<EstadoInteres> estados);
    List<Solicitud> findByUsuarioInteresadoId(Long usuarioInteresadoId);
    List<Solicitud> findByUsuarioInteresadoIdAndEstado(Long usuarioInteresadoId, EstadoInteres estado);
    Optional<Solicitud> findByPublicacionIdAndUsuarioInteresadoId(Long publicacionId, Long usuarioInteresadoId);
    boolean existsByPublicacionIdAndUsuarioInteresadoId(Long publicacionId, Long usuarioInteresadoId);
    void deleteById(Long id);
}
