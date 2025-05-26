package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ZonaEntity;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PublicacionMapperTest {

    private ZonaEntity zonaEntity;
    private Zona zona;
    private Usuario usuario;
    private PublicacionEntity publicacionEntity;
    private Publicacion publicacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @BeforeEach
    void setUp() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();

        zonaEntity = ZonaEntity.builder()
                .id(1L)
                .corregimiento("Corregimiento Test")
                .municipio("Municipio Test")
                .ciudad("Ciudad Test")
                .departamento("Departamento Test")
                .pais("País Test")
                .build();

        zona = Zona.builder()
                .id(1L)
                .corregimiento("Corregimiento Test")
                .municipio("Municipio Test")
                .ciudad("Ciudad Test")
                .departamento("Departamento Test")
                .pais("País Test")
                .build();

        usuario = Usuario.builder()
                .id(1L)
                .nombreCompleto("Usuario Test")
                .build();

        publicacionEntity = PublicacionEntity.builder()
                .id(1L)
                .titulo("Publicación Test")
                .descripcion("Descripción Test")
                .usuario(usuario)
                .tipoPublicacion(TipoPublicacion.OFERTA)
                .categoriaServicio("CATEGORIA_TEST")
                .estado(EstadoPublicacion.PUBLICADA)
                .zonaEntity(zonaEntity)
                .fechaCreacion(fechaCreacion)
                .fechaActualizacion(fechaActualizacion)
                .build();

        publicacion = Publicacion.builder()
                .id(1L)
                .titulo("Publicación Test")
                .descripcion("Descripción Test")
                .usuarioId(1L)
                .nombreUsuario("Usuario Test")
                .tipoPublicacion(TipoPublicacion.OFERTA)
                .categoriaServicio("CATEGORIA_TEST")
                .zonaId(1L)
                .zona(zona)
                .estado(EstadoPublicacion.PUBLICADA)
                .fechaCreacion(fechaCreacion)
                .fechaActualizacion(fechaActualizacion)
                .build();
    }

    @Test
    void toDomain_CuandoEntityNoEsNulo_DeberiaMapearCorrectamente() {
        // Act
        Publicacion result = PublicacionMapper.toDomain(publicacionEntity);

        // Assert
        assertNotNull(result);
        assertEquals(publicacionEntity.getId(), result.getId());
        assertEquals(publicacionEntity.getTitulo(), result.getTitulo());
        assertEquals(publicacionEntity.getDescripcion(), result.getDescripcion());
        assertEquals(publicacionEntity.getUsuario().getId(), result.getUsuarioId());
        assertEquals(publicacionEntity.getUsuario().getNombreCompleto(), result.getNombreUsuario());
        assertEquals(publicacionEntity.getTipoPublicacion(), result.getTipoPublicacion());
        assertEquals(publicacionEntity.getCategoriaServicio(), result.getCategoriaServicio());
        assertEquals(publicacionEntity.getZonaEntity().getId(), result.getZonaId());
        assertNotNull(result.getZona());
        assertEquals(publicacionEntity.getEstado(), result.getEstado());
        assertEquals(publicacionEntity.getFechaCreacion(), result.getFechaCreacion());
        assertEquals(publicacionEntity.getFechaActualizacion(), result.getFechaActualizacion());
    }

    @Test
    void toDomain_CuandoEntityEsNulo_DeberiaRetornarNulo() {
        // Act
        Publicacion result = PublicacionMapper.toDomain(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_CuandoDomainNoEsNulo_DeberiaMapearCorrectamente() {
        // Act
        PublicacionEntity result = PublicacionMapper.toEntity(publicacion);

        // Assert
        assertNotNull(result);
        assertEquals(publicacion.getId(), result.getId());
        assertEquals(publicacion.getTitulo(), result.getTitulo());
        assertEquals(publicacion.getDescripcion(), result.getDescripcion());
        assertEquals(publicacion.getTipoPublicacion(), result.getTipoPublicacion());
        assertEquals(publicacion.getCategoriaServicio(), result.getCategoriaServicio());
        assertEquals(publicacion.getEstado(), result.getEstado());
        assertNotNull(result.getZonaEntity());
        assertEquals(publicacion.getZona().getId(), result.getZonaEntity().getId());
        assertEquals(publicacion.getFechaCreacion(), result.getFechaCreacion());
        assertEquals(publicacion.getFechaActualizacion(), result.getFechaActualizacion());
    }

    @Test
    void toEntity_CuandoDomainEsNulo_DeberiaRetornarNulo() {
        // Act
        PublicacionEntity result = PublicacionMapper.toEntity(null);

        // Assert
        assertNull(result);
    }
}
