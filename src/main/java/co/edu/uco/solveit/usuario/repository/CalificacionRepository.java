package co.edu.uco.solveit.usuario.repository;

import co.edu.uco.solveit.usuario.entity.Calificacion;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CalificacionRepository extends CrudRepository<Calificacion, Long> {
    List<Calificacion> findByUsuarioId(Long usuarioId);
}
