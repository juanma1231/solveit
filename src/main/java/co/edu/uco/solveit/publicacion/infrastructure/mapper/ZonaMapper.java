package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ZonaEntity;

public class ZonaMapper {

    private ZonaMapper() {}

    public static Zona toDomain(ZonaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Zona.builder()
                .id(entity.getId())
                .corregimiento(entity.getCorregimiento())
                .municipio(entity.getMunicipio())
                .ciudad(entity.getCiudad())
                .departamento(entity.getDepartamento())
                .pais(entity.getPais())
                .build();
    }

    public static ZonaEntity toEntity(Zona domain) {
        if (domain == null) {
            return null;
        }
        return ZonaEntity.builder()
                .id(domain.getId())
                .corregimiento(domain.getCorregimiento())
                .municipio(domain.getMunicipio())
                .ciudad(domain.getCiudad())
                .departamento(domain.getDepartamento())
                .pais(domain.getPais())
                .build();
    }
}