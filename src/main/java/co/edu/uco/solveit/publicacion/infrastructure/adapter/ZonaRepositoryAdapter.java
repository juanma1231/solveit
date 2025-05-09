package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.port.out.ZonaRepositoryPort;
import co.edu.uco.solveit.publicacion.entity.Zona;
import co.edu.uco.solveit.publicacion.infrastructure.repository.ZonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ZonaRepositoryAdapter implements ZonaRepositoryPort {

    private final ZonaRepository zonaRepository;

    @Override
    public Zona save(Zona zona) {
        return zonaRepository.save(zona);
    }

    @Override
    public Optional<Zona> findById(Long id) {
        return zonaRepository.findById(id);
    }

    @Override
    public List<Zona> findAll() {
        return zonaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        zonaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return zonaRepository.existsById(id);
    }
}