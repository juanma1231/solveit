package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;

public class PublicacionMapper {

    private PublicacionMapper() {}

    public static Publicacion toDomain(PublicacionEntity entity) {
        if (entity == null) {
            return null;
        }
        return Publicacion.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .usuarioId(entity.getUsuario().getId())
                .nombreUsuario(entity.getUsuario().getNombreCompleto())
                .tipoPublicacion(entity.getTipoPublicacion())
                .categoriaServicio(entity.getCategoriaServicio())
                .zonaId(entity.getZonaEntity().getId())
                .zona(ZonaMapper.toDomain(entity.getZonaEntity()))
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }

    public static PublicacionEntity toEntity(Publicacion domain) {
        if (domain == null) {
            return null;
        }

        return PublicacionEntity.builder()
                .id(domain.getId())
                .titulo(domain.getTitulo())
                .descripcion(domain.getDescripcion())
                .tipoPublicacion(domain.getTipoPublicacion())
                .categoriaServicio(domain.getCategoriaServicio())
                .estado(domain.getEstado())
                .zonaEntity(ZonaMapper.toEntity(domain.getZona()))
                .fechaCreacion(domain.getFechaCreacion())
                .fechaActualizacion(domain.getFechaActualizacion())
                .build();
    }

}