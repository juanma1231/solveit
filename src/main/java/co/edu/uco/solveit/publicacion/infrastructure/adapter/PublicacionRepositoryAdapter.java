package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.infrastructure.mapper.PublicacionMapper;
import co.edu.uco.solveit.publicacion.infrastructure.repository.PublicacionRepository;
import co.edu.uco.solveit.usuario.entity.Usuario;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PublicacionRepositoryAdapter implements PublicacionRepositoryPort {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public Publicacion save(Publicacion publicacion) {
        co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion entity = PublicacionMapper.toEntity(publicacion);

        // Set usuario and zona if they are not set
        if (entity.getUsuario() == null && publicacion.getUsuarioId() != null) {
            usuarioRepository.findById(publicacion.getUsuarioId())
                    .ifPresent(entity::setUsuario);
        }

        co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion savedEntity = publicacionRepository.save(entity);
        return PublicacionMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Publicacion> findById(Long id) {
        return publicacionRepository.findById(id)
                .map(PublicacionMapper::toDomain);
    }

    @Override
    public List<Publicacion> findAll() {
        return publicacionRepository.findAll().stream()
                .map(PublicacionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Publicacion> findByUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return publicacionRepository.findByUsuario(usuario).stream()
                .map(PublicacionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Publicacion> findByTipoPublicacion(TipoPublicacion tipoPublicacion) {
        co.edu.uco.solveit.publicacion.infrastructure.entity.TipoPublicacion entityTipo = 
                co.edu.uco.solveit.publicacion.infrastructure.entity.TipoPublicacion.valueOf(tipoPublicacion.name());
        return publicacionRepository.findByTipoPublicacion(entityTipo).stream()
                .map(PublicacionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Publicacion> findByEstado(EstadoPublicacion estado) {
        co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoPublicacion entityEstado = 
                co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoPublicacion.valueOf(estado.name());
        return publicacionRepository.findByEstado(entityEstado).stream()
                .map(PublicacionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Publicacion> findByUsuarioIdAndEstado(Long usuarioId, EstadoPublicacion estado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoPublicacion entityEstado = 
                co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoPublicacion.valueOf(estado.name());
        return publicacionRepository.findByUsuarioAndEstado(usuario, entityEstado).stream()
                .map(PublicacionMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Publicacion> findByTipoPublicacionAndEstado(TipoPublicacion tipoPublicacion, EstadoPublicacion estado) {
        co.edu.uco.solveit.publicacion.infrastructure.entity.TipoPublicacion entityTipo = 
                co.edu.uco.solveit.publicacion.infrastructure.entity.TipoPublicacion.valueOf(tipoPublicacion.name());
        co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoPublicacion entityEstado = 
                co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoPublicacion.valueOf(estado.name());
        return publicacionRepository.findByTipoPublicacionAndEstado(entityTipo, entityEstado).stream()
                .map(PublicacionMapper::toDomain)
                .collect(Collectors.toList());
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
