package co.edu.uco.solveit.publicacion.infrastructure.repository;

import co.edu.uco.solveit.publicacion.entity.Zona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaRepository extends JpaRepository<Zona, Long> {
}