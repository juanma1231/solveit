package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.entity.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.entity.Publicacion;
import co.edu.uco.solveit.publicacion.entity.TipoPublicacion;
import co.edu.uco.solveit.publicacion.infrastructure.repository.PublicacionRepository;
import co.edu.uco.solveit.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PublicacionRepositoryAdapter implements PublicacionRepositoryPort {

    private final PublicacionRepository publicacionRepository;

    @Override
    public Publicacion save(Publicacion publicacion) {
        return publicacionRepository.save(publicacion);
    }

    @Override
    public Optional<Publicacion> findById(Long id) {
        return publicacionRepository.findById(id);
    }

    @Override
    public List<Publicacion> findAll() {
        return publicacionRepository.findAll();
    }

    @Override
    public List<Publicacion> findByUsuario(Usuario usuario) {
        return publicacionRepository.findByUsuario(usuario);
    }

    @Override
    public List<Publicacion> findByTipoPublicacion(TipoPublicacion tipoPublicacion) {
        return publicacionRepository.findByTipoPublicacion(tipoPublicacion);
    }

    @Override
    public List<Publicacion> findByEstado(EstadoPublicacion estado) {
        return publicacionRepository.findByEstado(estado);
    }

    @Override
    public List<Publicacion> findByUsuarioAndEstado(Usuario usuario, EstadoPublicacion estado) {
        return publicacionRepository.findByUsuarioAndEstado(usuario, estado);
    }

    @Override
    public List<Publicacion> findByTipoPublicacionAndEstado(TipoPublicacion tipoPublicacion, EstadoPublicacion estado) {
        return publicacionRepository.findByTipoPublicacionAndEstado(tipoPublicacion, estado);
    }

    @Override
    public void deleteById(Long id) {
        publicacionRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return publicacionRepository.existsById(id);
    }
}