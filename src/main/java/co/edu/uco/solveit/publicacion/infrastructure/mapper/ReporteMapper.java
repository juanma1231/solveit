package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.Reporte;

public class ReporteMapper {

    public static Reporte toDomain(co.edu.uco.solveit.publicacion.infrastructure.entity.Reporte entity) {
        if (entity == null) {
            return null;
        }
        return Reporte.builder()
                .id(entity.getId())
                .publicacionId(entity.getPublicacion().getId())
                .publicacion(PublicacionMapper.toDomain(entity.getPublicacion()))
                .usuarioId(entity.getUsuario().getId())
                .nombreUsuario(entity.getUsuario().getNombreCompleto())
                .motivo(entity.getMotivo())
                .fechaReporte(entity.getFechaReporte())
                .procesado(entity.isProcesado())
                .build();
    }

    public static co.edu.uco.solveit.publicacion.infrastructure.entity.Reporte toEntity(Reporte domain) {
        if (domain == null) {
            return null;
        }
        
        co.edu.uco.solveit.publicacion.infrastructure.entity.Reporte entity = 
            co.edu.uco.solveit.publicacion.infrastructure.entity.Reporte.builder()
                .id(domain.getId())
                .motivo(domain.getMotivo())
                .fechaReporte(domain.getFechaReporte())
                .procesado(domain.isProcesado())
                .build();
                
        // Note: publicacion and usuario need to be set separately as they require fetching from repositories
        
        return entity;
    }
}