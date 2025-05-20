package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.domain.model.Interes;
import co.edu.uco.solveit.publicacion.domain.port.out.InteresRepositoryPort;
import co.edu.uco.solveit.publicacion.infrastructure.entity.InteresEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion;
import co.edu.uco.solveit.publicacion.infrastructure.mapper.InteresMapper;
import co.edu.uco.solveit.publicacion.infrastructure.repository.InteresRepository;
import co.edu.uco.solveit.publicacion.infrastructure.repository.PublicacionRepository;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InteresRepositoryAdapter implements InteresRepositoryPort {

    private final InteresRepository interesRepository;
    private final PublicacionRepository publicacionRepository;
    private final UsuarioApi usuarioApi;

    @Override
    public Interes save(Interes interes) {
        InteresEntity entity = InteresMapper.toEntity(interes);

        // Set the publicacion if it's not set but we have the ID
        if (entity.getPublicacion() == null && interes.getPublicacionId() != null) {
            publicacionRepository.findById(interes.getPublicacionId())
                    .ifPresent(entity::setPublicacion);
        }

        // Set the usuario if it's not set but we have the ID
        if (entity.getUsuarioInteresado() == null && interes.getUsuarioInteresadoId() != null) {
            usuarioApi.findById(interes.getUsuarioInteresadoId())
                    .ifPresent(entity::setUsuarioInteresado);
        }

        InteresEntity savedEntity = interesRepository.save(entity);
        return InteresMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Interes> findById(Long id) {
        return interesRepository.findById(id)
                .map(InteresMapper::toDomain);
    }

    @Override
    public List<Interes> findByPublicacionId(Long publicacionId) {
        Optional<Publicacion> publicacion = publicacionRepository.findById(publicacionId);
        return publicacion.map(value -> interesRepository.findByPublicacion(value).stream()
                .map(InteresMapper::toDomain)
                .toList()).orElseGet(List::of);
    }

    @Override
    public List<Interes> findByPublicacionIdAndEstado(Long publicacionId, EstadoInteres estado) {
        Optional<Publicacion> publicacion = publicacionRepository.findById(publicacionId);
        if (publicacion.isEmpty()) {
            return List.of();
        }
        co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoInteres entityEstado = 
                co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoInteres.valueOf(estado.name());
        return interesRepository.findByPublicacionAndEstado(publicacion.get(), entityEstado).stream()
                .map(InteresMapper::toDomain)
                .toList();
    }

    @Override
    public List<Interes> findByUsuarioInteresadoId(Long usuarioInteresadoId) {
        Optional<Usuario> usuario = usuarioApi.findById(usuarioInteresadoId);
        if (usuario.isEmpty()) {
            return List.of();
        }
        return interesRepository.findByUsuarioInteresado(usuario.get()).stream()
                .map(InteresMapper::toDomain)
                .toList();
    }

    @Override
    public List<Interes> findByUsuarioInteresadoIdAndEstado(Long usuarioInteresadoId, EstadoInteres estado) {
        Optional<Usuario> usuario = usuarioApi.findById(usuarioInteresadoId);
        if (usuario.isEmpty()) {
            return List.of();
        }
        co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoInteres entityEstado = 
                co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoInteres.valueOf(estado.name());
        return interesRepository.findByUsuarioInteresadoAndEstado(usuario.get(), entityEstado).stream()
                .map(InteresMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Interes> findByPublicacionIdAndUsuarioInteresadoId(Long publicacionId, Long usuarioInteresadoId) {
        Optional<Publicacion> publicacion = publicacionRepository.findById(publicacionId);
        Optional<Usuario> usuario = usuarioApi.findById(usuarioInteresadoId);
        
        if (publicacion.isEmpty() || usuario.isEmpty()) {
            return Optional.empty();
        }
        
        return interesRepository.findByPublicacionAndUsuarioInteresado(publicacion.get(), usuario.get())
                .map(InteresMapper::toDomain);
    }

    @Override
    public boolean existsByPublicacionIdAndUsuarioInteresadoId(Long publicacionId, Long usuarioInteresadoId) {
        Optional<Publicacion> publicacion = publicacionRepository.findById(publicacionId);
        Optional<Usuario> usuario = usuarioApi.findById(usuarioInteresadoId);
        
        if (publicacion.isEmpty() || usuario.isEmpty()) {
            return false;
        }
        
        return interesRepository.existsByPublicacionAndUsuarioInteresado(publicacion.get(), usuario.get());
    }

    @Override
    public void deleteById(Long id) {
        interesRepository.deleteById(id);
    }
}