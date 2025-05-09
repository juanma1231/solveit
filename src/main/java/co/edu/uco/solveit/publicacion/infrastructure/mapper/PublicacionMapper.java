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
                .estado(mapEstadoPublicacionToDomain(entity.getEstado()))
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
                .estado(mapEstadoPublicacionToEntity(domain.getEstado()))
                .fechaCreacion(domain.getFechaCreacion())
                .fechaActualizacion(domain.getFechaActualizacion())
                .build();
                
        // Note: usuario and zona need to be set separately as they require fetching from repositories
        
        return entity;
    }
    
    private static EstadoPublicacion mapEstadoPublicacionToDomain(co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoPublicacion estado) {
        if (estado == null) {
            return null;
        }
        return EstadoPublicacion.valueOf(estado.name());
    }
    
    private static co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoPublicacion mapEstadoPublicacionToEntity(EstadoPublicacion estado) {
        if (estado == null) {
            return null;
        }
        return co.edu.uco.solveit.publicacion.infrastructure.entity.EstadoPublicacion.valueOf(estado.name());
    }
    
    private static TipoPublicacion mapTipoPublicacionToDomain(co.edu.uco.solveit.publicacion.infrastructure.entity.TipoPublicacion tipo) {
        if (tipo == null) {
            return null;
        }
        return TipoPublicacion.valueOf(tipo.name());
    }
    
    private static co.edu.uco.solveit.publicacion.infrastructure.entity.TipoPublicacion mapTipoPublicacionToEntity(TipoPublicacion tipo) {
        if (tipo == null) {
            return null;
        }
        return co.edu.uco.solveit.publicacion.infrastructure.entity.TipoPublicacion.valueOf(tipo.name());
    }
}