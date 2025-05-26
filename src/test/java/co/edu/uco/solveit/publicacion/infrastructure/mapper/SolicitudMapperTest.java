package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.Solicitud;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.SolicitudEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ZonaEntity;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SolicitudMapperTest {

    private SolicitudEntity solicitudEntity;
    private Solicitud solicitud;
    private PublicacionEntity publicacionEntity;
    private Publicacion publicacion;
    private Usuario usuario;
    private ZonaEntity zonaEntity;
    private Zona zona;
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

        solicitudEntity = SolicitudEntity.builder()
                .id(1L)
                .publicacion(publicacionEntity)
                .usuarioQueSolicita(usuario)
                .nombreUsuarioQueSolicita("Usuario Test")
                .titulo("Solicitud Test")
                .descripcion("Descripción Test")
                .estado(EstadoInteres.PENDIENTE)
                .fechaCreacion(fechaCreacion)
                .fechaActualizacion(fechaActualizacion)
                .build();

        solicitud = Solicitud.builder()
                .id(1L)
                .publicacionId(1L)
                .publicacion(publicacion)
                .usuarioInteresadoId(1L)
                .nombreUsuarioInteresado("Usuario Test")
                .titulo("Solicitud Test")
                .descripcion("Descripción Test")
                .estado(EstadoInteres.PENDIENTE)
                .fechaCreacion(fechaCreacion)
                .fechaActualizacion(fechaActualizacion)
                .build();
    }

    @Test
    void toDomain_CuandoEntityNoEsNulo_DeberiaMapearCorrectamente() {
        // Act
        Solicitud result = SolicitudMapper.toDomain(solicitudEntity);

        // Assert
        assertNotNull(result);
        assertEquals(solicitudEntity.getId(), result.getId());
        assertEquals(solicitudEntity.getPublicacion().getId(), result.getPublicacionId());
        assertNotNull(result.getPublicacion());
        assertEquals(solicitudEntity.getUsuarioQueSolicita().getId(), result.getUsuarioInteresadoId());
        assertEquals(solicitudEntity.getNombreUsuarioQueSolicita(), result.getNombreUsuarioInteresado());
        assertEquals(solicitudEntity.getTitulo(), result.getTitulo());
        assertEquals(solicitudEntity.getDescripcion(), result.getDescripcion());
        assertEquals(solicitudEntity.getEstado(), result.getEstado());
        assertEquals(solicitudEntity.getFechaCreacion(), result.getFechaCreacion());
        assertEquals(solicitudEntity.getFechaActualizacion(), result.getFechaActualizacion());
    }

    @Test
    void toDomain_CuandoEntityEsNulo_DeberiaRetornarNulo() {
        // Act
        Solicitud result = SolicitudMapper.toDomain(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_CuandoDomainNoEsNulo_DeberiaMapearCorrectamente() {
        // Act
        SolicitudEntity result = SolicitudMapper.toEntity(solicitud);

        // Assert
        assertNotNull(result);
        assertEquals(solicitud.getId(), result.getId());
        assertEquals(solicitud.getNombreUsuarioInteresado(), result.getNombreUsuarioQueSolicita());
        assertEquals(solicitud.getTitulo(), result.getTitulo());
        assertEquals(solicitud.getDescripcion(), result.getDescripcion());
        assertEquals(solicitud.getEstado(), result.getEstado());
        assertEquals(solicitud.getFechaCreacion(), result.getFechaCreacion());
        assertEquals(solicitud.getFechaActualizacion(), result.getFechaActualizacion());
    }

    @Test
    void toEntity_CuandoDomainEsNulo_DeberiaRetornarNulo() {
        // Act
        SolicitudEntity result = SolicitudMapper.toEntity(null);

        // Assert
        assertNull(result);
    }
}