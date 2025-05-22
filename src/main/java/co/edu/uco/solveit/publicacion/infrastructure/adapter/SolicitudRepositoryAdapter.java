package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.domain.model.Solicitud;
import co.edu.uco.solveit.publicacion.domain.port.out.SolicitudRepositoryPort;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.SolicitudEntity;
import co.edu.uco.solveit.publicacion.infrastructure.mapper.SolicitudMapper;
import co.edu.uco.solveit.publicacion.infrastructure.repository.SolicitudRepository;
import co.edu.uco.solveit.publicacion.infrastructure.repository.PublicacionRepository;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SolicitudRepositoryAdapter implements SolicitudRepositoryPort {

    private final SolicitudRepository solicitudRepository;
    private final PublicacionRepository publicacionRepository;
    private final UsuarioApi usuarioApi;

    @Override
    public Solicitud save(Solicitud solicitud) {
        SolicitudEntity entity = SolicitudMapper.toEntity(solicitud);

        // Set the publicacion if it's not set but we have the ID
        if (entity.getPublicacionEntity() == null && solicitud.getPublicacionId() != null) {
            publicacionRepository.findById(solicitud.getPublicacionId())
                    .ifPresent(entity::setPublicacionEntity);
        }

        // Set the usuario if it's not set but we have the ID
        if (entity.getUsuarioQueSolicita() == null && solicitud.getUsuarioInteresadoId() != null) {
            usuarioApi.findById(solicitud.getUsuarioInteresadoId())
                    .ifPresent(entity::setUsuarioQueSolicita);
        }

        SolicitudEntity savedEntity = solicitudRepository.save(entity);
        return SolicitudMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Solicitud> findById(Long id) {
        return solicitudRepository.findById(id)
                .map(SolicitudMapper::toDomain);
    }

    @Override
    public List<Solicitud> findByPublicacionId(Long publicacionId) {
        Optional<PublicacionEntity> publicacion = publicacionRepository.findById(publicacionId);
        return publicacion.map(value -> solicitudRepository.findByPublicacion(value).stream()
                .map(SolicitudMapper::toDomain)
                .toList()).orElseGet(List::of);
    }

    @Override
    public List<Solicitud> findByPublicacionIdAndEstado(Long publicacionId, EstadoInteres estado) {
        Optional<PublicacionEntity> publicacion = publicacionRepository.findById(publicacionId);
        if (publicacion.isEmpty()) {
            return List.of();
        }
        EstadoInteres entityEstado =
                EstadoInteres.valueOf(estado.name());
        return solicitudRepository.findByPublicacionAndEstado(publicacion.get(), entityEstado).stream()
                .map(SolicitudMapper::toDomain)
                .toList();
    }

    @Override
    public List<Solicitud> findByPublicacionIdAndEstadoIn(Long publicacionId, List<EstadoInteres> estados) {
        return solicitudRepository.findByPublicacionIdAndEstadoIn(publicacionId, estados)
                .stream()
                .map(SolicitudMapper::toDomain)
                .toList();
    }

    @Override
    public List<Solicitud> findByUsuarioInteresadoId(Long usuarioInteresadoId) {
        Optional<Usuario> usuario = usuarioApi.findById(usuarioInteresadoId);
        return usuario.map(value -> solicitudRepository.findByUsuarioInteresado(value).stream()
                .map(SolicitudMapper::toDomain)
                .toList()).orElseGet(List::of);
    }

    @Override
    public List<Solicitud> findByUsuarioInteresadoIdAndEstado(Long usuarioInteresadoId, EstadoInteres estado) {
        Optional<Usuario> usuario = usuarioApi.findById(usuarioInteresadoId);
        if (usuario.isEmpty()) {
            return List.of();
        }
        EstadoInteres entityEstado =
                EstadoInteres.valueOf(estado.name());
        return solicitudRepository.findByUsuarioInteresadoAndEstado(usuario.get(), entityEstado).stream()
                .map(SolicitudMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Solicitud> findByPublicacionIdAndUsuarioInteresadoId(Long publicacionId, Long usuarioInteresadoId) {
        Optional<PublicacionEntity> publicacion = publicacionRepository.findById(publicacionId);
        Optional<Usuario> usuario = usuarioApi.findById(usuarioInteresadoId);
        
        if (publicacion.isEmpty() || usuario.isEmpty()) {
            return Optional.empty();
        }
        
        return solicitudRepository.findByPublicacionAndUsuarioInteresado(publicacion.get(), usuario.get())
                .map(SolicitudMapper::toDomain);
    }

    @Override
    public boolean existsByPublicacionIdAndUsuarioInteresadoId(Long publicacionId, Long usuarioInteresadoId) {
        Optional<PublicacionEntity> publicacion = publicacionRepository.findById(publicacionId);
        Optional<Usuario> usuario = usuarioApi.findById(usuarioInteresadoId);
        
        if (publicacion.isEmpty() || usuario.isEmpty()) {
            return false;
        }
        
        return solicitudRepository.existsByPublicacionAndUsuarioInteresado(publicacion.get(), usuario.get());
    }

    @Override
    public void deleteById(Long id) {
        solicitudRepository.deleteById(id);
    }
}