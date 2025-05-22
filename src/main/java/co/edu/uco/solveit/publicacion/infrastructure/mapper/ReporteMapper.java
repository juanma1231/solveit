package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.Reporte;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ReporteEntity;

public class ReporteMapper {

    public static Reporte toDomain(ReporteEntity entity) {
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

    public static ReporteEntity toEntity(Reporte domain) {
        if (domain == null) {
            return null;
        }
        
        return ReporteEntity.builder()
                .id(domain.getId())
                .motivo(domain.getMotivo())
                .fechaReporte(domain.getFechaReporte())
                .procesado(domain.isProcesado())
                .build();
    }
}