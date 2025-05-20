package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;

public class PublicacionMapper {

    public static Publicacion toDomain(co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion entity) {
        if (entity == null) {
            return null;
        }
        return Publicacion.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descripcion(entity.getDescripcion())
                .usuarioId(entity.getUsuario().getId())
                .nombreUsuario(entity.getUsuario().getNombreCompleto())
                .tipoPublicacion(mapTipoPublicacionToDomain(entity.getTipoPublicacion()))
                .categoriaServicio(entity.getCategoriaServicio())
                .zonaId(entity.getZona().getId())
                .zona(ZonaMapper.toDomain(entity.getZona()))
                .estado(entity.getEstado())
                .fechaCreacion(entity.getFechaCreacion())
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }

    public static co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion toEntity(Publicacion domain) {
        if (domain == null) {
            return null;
        }
        
        co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion entity = 
            co.edu.uco.solveit.publicacion.infrastructure.entity.Publicacion.builder()
                .id(domain.getId())
                .titulo(domain.getTitulo())
                .descripcion(domain.getDescripcion())
                .tipoPublicacion(mapTipoPublicacionToEntity(domain.getTipoPublicacion()))
                .categoriaServicio(domain.getCategoriaServicio())
                .estado(domain.getEstado())
                .fechaCreacion(domain.getFechaCreacion())
                .fechaActualizacion(domain.getFechaActualizacion())
                .build();
                
        // Note: usuario and zona need to be set separately as they require fetching from repositories
        
        return entity;
    }
    

    
    private static TipoPublicacion mapTipoPublicacionToDomain(TipoPublicacion tipo) {
        if (tipo == null) {
            return null;
        }
        return TipoPublicacion.valueOf(tipo.name());
    }
    
    private static TipoPublicacion mapTipoPublicacionToEntity(TipoPublicacion tipo) {
        if (tipo == null) {
            return null;
        }
        return TipoPublicacion.valueOf(tipo.name());
    }
}