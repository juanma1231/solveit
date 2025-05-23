package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;
import co.edu.uco.solveit.publicacion.infrastructure.mapper.PublicacionMapper;
import co.edu.uco.solveit.publicacion.infrastructure.repository.PublicacionRepository;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static co.edu.uco.solveit.usuario.service.UsuarioService.USUARIO_NO_ENCONTRADO;


@Component
@RequiredArgsConstructor
public class PublicacionRepositoryAdapter implements PublicacionRepositoryPort {

    private final PublicacionRepository publicacionRepository;
    private final UsuarioApi usuarioApi;

    @Override
    public Publicacion save(Publicacion publicacion) {
        PublicacionEntity entity = PublicacionMapper.toEntity(publicacion);

        if (entity.getUsuario() == null && publicacion.getUsuarioId() != null) {
            usuarioApi.findById(publicacion.getUsuarioId())
                    .ifPresent(entity::setUsuario);
        }

        PublicacionEntity savedEntity = publicacionRepository.save(entity);
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
                .toList();
    }

    @Override
    public List<Publicacion> findByUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioApi.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException(USUARIO_NO_ENCONTRADO));
        return publicacionRepository.findByUsuario(usuario).stream()
                .map(PublicacionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Publicacion> findByTipoPublicacion(TipoPublicacion tipoPublicacion) {
        TipoPublicacion entityTipo = 
                TipoPublicacion.valueOf(tipoPublicacion.name());
        return publicacionRepository.findByTipoPublicacion(entityTipo).stream()
                .map(PublicacionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Publicacion> findByEstado(EstadoPublicacion estado) {
        EstadoPublicacion entityEstado =
                EstadoPublicacion.valueOf(estado.name());
        return publicacionRepository.findByEstado(entityEstado).stream()
                .map(PublicacionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Publicacion> findByUsuarioIdAndEstado(Long usuarioId, EstadoPublicacion estado) {
        Usuario usuario = usuarioApi.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException(USUARIO_NO_ENCONTRADO));
        EstadoPublicacion entityEstado =
                EstadoPublicacion.valueOf(estado.name());
        return publicacionRepository.findByUsuarioAndEstado(usuario, entityEstado).stream()
                .map(PublicacionMapper::toDomain)
                .toList();
    }

    @Override
    public List<Publicacion> findByTipoPublicacionAndEstado(TipoPublicacion tipoPublicacion, EstadoPublicacion estado) {
        TipoPublicacion entityTipo = 
                TipoPublicacion.valueOf(tipoPublicacion.name());
        EstadoPublicacion entityEstado =
                EstadoPublicacion.valueOf(estado.name());
        return publicacionRepository.findByTipoPublicacionAndEstado(entityTipo, entityEstado).stream()
                .map(PublicacionMapper::toDomain)
                .toList();
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
