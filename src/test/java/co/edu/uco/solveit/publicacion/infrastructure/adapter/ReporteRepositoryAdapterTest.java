package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.Reporte;
import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ReporteEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ZonaEntity;
import co.edu.uco.solveit.publicacion.infrastructure.repository.ReporteRepository;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteRepositoryAdapterTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private UsuarioApi usuarioApi;

    @InjectMocks
    private ReporteRepositoryAdapter reporteRepositoryAdapter;

    private ReporteEntity reporteEntity;
    private Reporte reporte;
    private Usuario usuario;
    private PublicacionEntity publicacionEntity;
    private Publicacion publicacion;
    private ZonaEntity zonaEntity;
    private Zona zona;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setNombreCompleto("Test User");

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

        publicacionEntity = PublicacionEntity.builder()
                .id(1L)
                .titulo("Título Test")
                .descripcion("Descripción Test")
                .usuario(usuario)
                .zonaEntity(zonaEntity)
                .estado(EstadoPublicacion.PUBLICADA)
                .build();

        publicacion = Publicacion.builder()
                .id(1L)
                .titulo("Título Test")
                .descripcion("Descripción Test")
                .usuarioId(1L)
                .nombreUsuario("Test User")
                .zonaId(1L)
                .zona(zona)
                .estado(EstadoPublicacion.PUBLICADA)
                .build();

        reporteEntity = ReporteEntity.builder()
                .id(1L)
                .publicacion(publicacionEntity)
                .usuario(usuario)
                .motivo("Motivo Test")
                .fechaReporte(LocalDateTime.now())
                .procesado(false)
                .build();

        reporte = Reporte.builder()
                .id(1L)
                .publicacionId(1L)
                .publicacion(publicacion)
                .usuarioId(1L)
                .nombreUsuario("Test User")
                .motivo("Motivo Test")
                .fechaReporte(LocalDateTime.now())
                .procesado(false)
                .build();
    }

    @Test
    void save_DeberiaGuardarReporteCorrectamente() {
        // Arrange
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(reporteRepository.save(any(ReporteEntity.class))).thenReturn(reporteEntity);

        // Act
        Reporte resultado = reporteRepositoryAdapter.save(reporte);

        // Assert
        assertNotNull(resultado);
        assertEquals(reporte.getId(), resultado.getId());
        assertEquals(reporte.getPublicacionId(), resultado.getPublicacionId());
        assertEquals(reporte.getUsuarioId(), resultado.getUsuarioId());
        assertEquals(reporte.getNombreUsuario(), resultado.getNombreUsuario());
        assertEquals(reporte.getMotivo(), resultado.getMotivo());
        assertEquals(reporte.isProcesado(), resultado.isProcesado());
        verify(reporteRepository).save(any(ReporteEntity.class));
    }

    @Test
    void findById_CuandoExisteReporte_DeberiaRetornarReporte() {
        // Arrange
        when(reporteRepository.findById(anyLong())).thenReturn(Optional.of(reporteEntity));

        // Act
        Optional<Reporte> resultado = reporteRepositoryAdapter.findById(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(reporte.getId(), resultado.get().getId());
        assertEquals(reporte.getPublicacionId(), resultado.get().getPublicacionId());
        assertEquals(reporte.getUsuarioId(), resultado.get().getUsuarioId());
        assertEquals(reporte.getNombreUsuario(), resultado.get().getNombreUsuario());
        assertEquals(reporte.getMotivo(), resultado.get().getMotivo());
        assertEquals(reporte.isProcesado(), resultado.get().isProcesado());
        verify(reporteRepository).findById(1L);
    }

    @Test
    void findById_CuandoNoExisteReporte_DeberiaRetornarEmpty() {
        // Arrange
        when(reporteRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Reporte> resultado = reporteRepositoryAdapter.findById(1L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(reporteRepository).findById(1L);
    }

    @Test
    void findByPublicacionId_DeberiaRetornarReportesPorPublicacion() {
        // Arrange
        List<ReporteEntity> reporteEntities = Arrays.asList(reporteEntity);
        when(reporteRepository.findByPublicacion_Id(anyLong())).thenReturn(reporteEntities);

        // Act
        List<Reporte> resultado = reporteRepositoryAdapter.findByPublicacionId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reporte.getId(), resultado.get(0).getId());
        assertEquals(reporte.getPublicacionId(), resultado.get(0).getPublicacionId());
        assertEquals(reporte.getUsuarioId(), resultado.get(0).getUsuarioId());
        assertEquals(reporte.getNombreUsuario(), resultado.get(0).getNombreUsuario());
        assertEquals(reporte.getMotivo(), resultado.get(0).getMotivo());
        assertEquals(reporte.isProcesado(), resultado.get(0).isProcesado());
        verify(reporteRepository).findByPublicacion_Id(1L);
    }

    @Test
    void findByUsuarioId_DeberiaRetornarReportesPorUsuario() {
        // Arrange
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        List<ReporteEntity> reporteEntities = Arrays.asList(reporteEntity);
        when(reporteRepository.findByUsuario(any(Usuario.class))).thenReturn(reporteEntities);

        // Act
        List<Reporte> resultado = reporteRepositoryAdapter.findByUsuarioId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reporte.getId(), resultado.get(0).getId());
        assertEquals(reporte.getPublicacionId(), resultado.get(0).getPublicacionId());
        assertEquals(reporte.getUsuarioId(), resultado.get(0).getUsuarioId());
        assertEquals(reporte.getNombreUsuario(), resultado.get(0).getNombreUsuario());
        assertEquals(reporte.getMotivo(), resultado.get(0).getMotivo());
        assertEquals(reporte.isProcesado(), resultado.get(0).isProcesado());
        verify(reporteRepository).findByUsuario(usuario);
    }

    @Test
    void findByProcesado_DeberiaRetornarReportesPorEstadoProcesado() {
        // Arrange
        List<ReporteEntity> reporteEntities = Arrays.asList(reporteEntity);
        when(reporteRepository.findByProcesado(anyBoolean())).thenReturn(reporteEntities);

        // Act
        List<Reporte> resultado = reporteRepositoryAdapter.findByProcesado(false);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(reporte.getId(), resultado.get(0).getId());
        assertEquals(reporte.getPublicacionId(), resultado.get(0).getPublicacionId());
        assertEquals(reporte.getUsuarioId(), resultado.get(0).getUsuarioId());
        assertEquals(reporte.getNombreUsuario(), resultado.get(0).getNombreUsuario());
        assertEquals(reporte.getMotivo(), resultado.get(0).getMotivo());
        assertEquals(reporte.isProcesado(), resultado.get(0).isProcesado());
        verify(reporteRepository).findByProcesado(false);
    }

    @Test
    void countByPublicacionId_DeberiaRetornarCantidadDeReportesPorPublicacion() {
        // Arrange
        when(reporteRepository.countByPublicacion_Id(anyLong())).thenReturn(5L);

        // Act
        long resultado = reporteRepositoryAdapter.countByPublicacionId(1L);

        // Assert
        assertEquals(5L, resultado);
        verify(reporteRepository).countByPublicacion_Id(1L);
    }

    @Test
    void existsByPublicacionIdAndUsuarioId_CuandoExisteReporte_DeberiaRetornarTrue() {
        // Arrange
        when(reporteRepository.existsByPublicacion_IdAndUsuario_Id(anyLong(), anyLong())).thenReturn(true);

        // Act
        boolean resultado = reporteRepositoryAdapter.existsByPublicacionIdAndUsuarioId(1L, 1L);

        // Assert
        assertTrue(resultado);
        verify(reporteRepository).existsByPublicacion_IdAndUsuario_Id(1L, 1L);
    }

    @Test
    void existsByPublicacionIdAndUsuarioId_CuandoNoExisteReporte_DeberiaRetornarFalse() {
        // Arrange
        when(reporteRepository.existsByPublicacion_IdAndUsuario_Id(anyLong(), anyLong())).thenReturn(false);

        // Act
        boolean resultado = reporteRepositoryAdapter.existsByPublicacionIdAndUsuarioId(1L, 1L);

        // Assert
        assertFalse(resultado);
        verify(reporteRepository).existsByPublicacion_IdAndUsuario_Id(1L, 1L);
    }
}