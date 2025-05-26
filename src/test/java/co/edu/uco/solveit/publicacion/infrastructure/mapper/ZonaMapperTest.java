package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ZonaEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ZonaMapperTest {

    @Test
    void toDomain_CuandoEntityNoEsNulo_DeberiaMapearCorrectamente() {
        // Arrange
        ZonaEntity entity = ZonaEntity.builder()
                .id(1L)
                .corregimiento("Corregimiento Test")
                .municipio("Municipio Test")
                .ciudad("Ciudad Test")
                .departamento("Departamento Test")
                .pais("País Test")
                .build();

        // Act
        Zona result = ZonaMapper.toDomain(entity);

        // Assert
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getCorregimiento(), result.getCorregimiento());
        assertEquals(entity.getMunicipio(), result.getMunicipio());
        assertEquals(entity.getCiudad(), result.getCiudad());
        assertEquals(entity.getDepartamento(), result.getDepartamento());
        assertEquals(entity.getPais(), result.getPais());
    }

    @Test
    void toDomain_CuandoEntityEsNulo_DeberiaRetornarNulo() {
        // Arrange
        ZonaEntity entity = null;

        // Act
        Zona result = ZonaMapper.toDomain(entity);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_CuandoDomainNoEsNulo_DeberiaMapearCorrectamente() {
        // Arrange
        Zona domain = Zona.builder()
                .id(1L)
                .corregimiento("Corregimiento Test")
                .municipio("Municipio Test")
                .ciudad("Ciudad Test")
                .departamento("Departamento Test")
                .pais("País Test")
                .build();

        // Act
        ZonaEntity result = ZonaMapper.toEntity(domain);

        // Assert
        assertNotNull(result);
        assertEquals(domain.getId(), result.getId());
        assertEquals(domain.getCorregimiento(), result.getCorregimiento());
        assertEquals(domain.getMunicipio(), result.getMunicipio());
        assertEquals(domain.getCiudad(), result.getCiudad());
        assertEquals(domain.getDepartamento(), result.getDepartamento());
        assertEquals(domain.getPais(), result.getPais());
    }

    @Test
    void toEntity_CuandoDomainEsNulo_DeberiaRetornarNulo() {
        // Arrange
        Zona domain = null;

        // Act
        ZonaEntity result = ZonaMapper.toEntity(domain);

        // Assert
        assertNull(result);
    }
}