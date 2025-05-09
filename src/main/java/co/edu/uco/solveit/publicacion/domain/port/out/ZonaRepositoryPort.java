package co.edu.uco.solveit.publicacion.domain.port.out;

import co.edu.uco.solveit.publicacion.entity.Zona;

import java.util.List;
import java.util.Optional;

public interface ZonaRepositoryPort {
    Zona save(Zona zona);
    Optional<Zona> findById(Long id);
    List<Zona> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}