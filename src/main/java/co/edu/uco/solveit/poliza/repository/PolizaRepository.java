package co.edu.uco.solveit.poliza.repository;

import co.edu.uco.solveit.poliza.entity.Poliza;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolizaRepository extends JpaRepository<Poliza, Long> {
    List<Poliza> findByTitular(Usuario titular);
    List<Poliza> findByTitularId(Long titularId);
}