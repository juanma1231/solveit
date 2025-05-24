package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.Solicitud;
import co.edu.uco.solveit.publicacion.infrastructure.entity.SolicitudEntity;

public class SolicitudMapper {

    private SolicitudMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Solicitud toDomain(SolicitudEntity entity) {
        if (entity == null) {
            return null;
        }

        return Solicitud.builder()
                .id(entity.getId())
                .publicacionId(entity.getPublicacion() != null ? entity.getPublicacion().getId() : null)
                .publicacion(entity.getPublicacion() != null ? PublicacionMapper.toDomain(entity.getPublicacion()) : null)
                .usuarioInteresadoId(entity.getUsuarioQueSolicita() != null ? entity.getUsuarioQueSolicita().getId() : null)
                .nombreUsuarioInteresado(entity.getNombreUsuarioQueSolicita())
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .userEmail(entity.getUserEmail())
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }

    public static SolicitudEntity toEntity(Solicitud domain) {
        if (domain == null) {
            return null;
        }

        return SolicitudEntity.builder()
                .id(domain.getId())
                .nombreUsuarioQueSolicita(domain.getNombreUsuarioInteresado())
                .titulo(domain.getTitulo())
                .descripcion(domain.getDescripcion())
                .estado(domain.getEstado())
                .fechaCreacion(domain.getFechaCreacion())
                .fechaActualizacion(domain.getFechaActualizacion())
                .userEmail(domain.getUserEmail())
                .build();
    }

}
