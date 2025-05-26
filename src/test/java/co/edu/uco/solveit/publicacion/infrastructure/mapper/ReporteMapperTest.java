package co.edu.uco.solveit.publicacion.infrastructure.mapper;

import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.Reporte;
import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ReporteEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ZonaEntity;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReporteMapperTest {

    private ReporteEntity reporteEntity;
    private Reporte reporte;
    private PublicacionEntity publicacionEntity;
    private Usuario usuario;
    private ZonaEntity zonaEntity;
    private LocalDateTime fechaReporte;

    @BeforeEach
    void setUp() {
        fechaReporte = LocalDateTime.now();

        zonaEntity = ZonaEntity.builder()
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
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        reporteEntity = ReporteEntity.builder()
                .id(1L)
                .publicacion(publicacionEntity)
                .usuario(usuario)
                .motivo("Motivo Test")
                .fechaReporte(fechaReporte)
                .procesado(false)
                .build();

        reporte = Reporte.builder()
                .id(1L)
                .publicacionId(1L)
                .usuarioId(1L)
                .nombreUsuario("Usuario Test")
                .motivo("Motivo Test")
                .fechaReporte(fechaReporte)
                .procesado(false)
                .build();
    }

    @Test
    void toDomain_CuandoEntityNoEsNulo_DeberiaMapearCorrectamente() {
        // Act
        Reporte result = ReporteMapper.toDomain(reporteEntity);

        // Assert
        assertNotNull(result);
        assertEquals(reporteEntity.getId(), result.getId());
        assertEquals(reporteEntity.getPublicacion().getId(), result.getPublicacionId());
        assertEquals(reporteEntity.getUsuario().getId(), result.getUsuarioId());
        assertEquals(reporteEntity.getUsuario().getNombreCompleto(), result.getNombreUsuario());
        assertEquals(reporteEntity.getMotivo(), result.getMotivo());
        assertEquals(reporteEntity.getFechaReporte(), result.getFechaReporte());
        assertEquals(reporteEntity.isProcesado(), result.isProcesado());
    }

    @Test
    void toDomain_CuandoEntityEsNulo_DeberiaRetornarNulo() {
        // Act
        Reporte result = ReporteMapper.toDomain(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_CuandoDomainNoEsNulo_DeberiaMapearCorrectamente() {
        // Act
        ReporteEntity result = ReporteMapper.toEntity(reporte);

        // Assert
        assertNotNull(result);
        assertEquals(reporte.getId(), result.getId());
        assertEquals(reporte.getMotivo(), result.getMotivo());
        assertEquals(reporte.getFechaReporte(), result.getFechaReporte());
        assertEquals(reporte.isProcesado(), result.isProcesado());
    }

    @Test
    void toEntity_CuandoDomainEsNulo_DeberiaRetornarNulo() {
        // Act
        ReporteEntity result = ReporteMapper.toEntity(null);

        // Assert
        assertNull(result);
    }
}