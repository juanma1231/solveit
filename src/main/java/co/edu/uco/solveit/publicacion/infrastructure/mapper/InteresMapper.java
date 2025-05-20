package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.Interes;
import co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoInteres;
import co.edu.uco.solveit.publicacion.infrastructure.entity.InteresEntity;

public class InteresMapper {

    private InteresMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Interes toDomain(InteresEntity entity) {
        if (entity == null) {
            return null;
        }

        return Interes.builder()
                .id(entity.getId())
                .publicacionId(entity.getPublicacion() != null ? entity.getPublicacion().getId() : null)
                .publicacion(entity.getPublicacion() != null ? PublicacionMapper.toDomain(entity.getPublicacion()) : null)
                .usuarioInteresadoId(entity.getUsuarioInteresado() != null ? entity.getUsuarioInteresado().getId() : null)
                .nombreUsuarioInteresado(entity.getNombreUsuarioInteresado())
                .estado(mapToDomainEstado(entity.getEstado()))
                .fechaCreacion(entity.getFechaCreacion())
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }

    public static InteresEntity toEntity(Interes domain) {
        if (domain == null) {
            return null;
        }

        return InteresEntity.builder()
                .id(domain.getId())
                .nombreUsuarioInteresado(domain.getNombreUsuarioInteresado())
                .estado(mapToEntityEstado(domain.getEstado()))
                .fechaCreacion(domain.getFechaCreacion())
                .fechaActualizacion(domain.getFechaActualizacion())
                .build();
    }

    private static co.edu.uco.solveit.publicacion.domain.model.EstadoInteres mapToDomainEstado(EstadoInteres estado) {
        if (estado == null) {
            return null;
        }
        return co.edu.uco.solveit.publicacion.domain.model.EstadoInteres.valueOf(estado.name());
    }

    private static EstadoInteres mapToEntityEstado(co.edu.uco.solveit.publicacion.domain.model.EstadoInteres estado) {
        if (estado == null) {
            return null;
        }
        return EstadoInteres.valueOf(estado.name());
    }
}