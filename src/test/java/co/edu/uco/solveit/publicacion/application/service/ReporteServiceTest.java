package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.publicacion.application.dto.ReporteResponse;
import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.Reporte;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.ReporteRepositoryPort;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepositoryPort reporteRepositoryPort;

    @Mock
    private PublicacionRepositoryPort publicacionRepositoryPort;

    @Mock
    private UsuarioApi usuarioApi;

    @InjectMocks
    private ReporteService reporteService;

    private Publicacion publicacion;
    private Reporte reporte;

    @BeforeEach
    void setUp() {
        publicacion = Publicacion.builder()
                .id(1L)
                .titulo("Título Test")
                .descripcion("Descripción Test")
                .usuarioId(1L)
                .nombreUsuario("Usuario Test")
                .estado(EstadoPublicacion.REPORTADA)
                .build();

        reporte = Reporte.builder()
                .id(1L)
                .publicacionId(1L)
                .publicacion(publicacion)
                .usuarioId(2L)
                .nombreUsuario("Usuario Reportador")
                .motivo("Motivo del reporte")
                .fechaReporte(LocalDateTime.now())
                .procesado(false)
                .build();
    }

    @Test
    void cancelarReporte_DeberiaCancelarReporteCorrectamente() {
        // Arrange
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(reporteRepositoryPort.findByPublicacionId(anyLong())).thenReturn(Collections.singletonList(reporte));

        // Act
        MessageResponse response = reporteService.cancelarReporte(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Reportes cancelados y publicación habilitada correctamente", response.message());
        verify(publicacionRepositoryPort).save(any(Publicacion.class));
        verify(reporteRepositoryPort).save(any(Reporte.class));
    }

    @Test
    void cancelarReporte_CuandoPublicacionNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> reporteService.cancelarReporte(1L));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void cancelarReporte_CuandoPublicacionNoEstaReportada_DeberiaLanzarExcepcion() {
        // Arrange
        publicacion.setEstado(EstadoPublicacion.PUBLICADA);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act & Assert
        assertThrows(PublicacionException.class, () -> reporteService.cancelarReporte(1L));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void habilitarPublicacion_DeberiaHabilitarPublicacionCorrectamente() {
        // Arrange
        publicacion.setEstado(EstadoPublicacion.BLOQUEADA);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(reporteRepositoryPort.findByPublicacionId(anyLong())).thenReturn(Collections.singletonList(reporte));

        // Act
        MessageResponse response = reporteService.habilitarPublicacion(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Publicación habilitada correctamente", response.message());
        verify(publicacionRepositoryPort).save(any(Publicacion.class));
        verify(reporteRepositoryPort).save(any(Reporte.class));
    }

    @Test
    void habilitarPublicacion_CuandoPublicacionNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> reporteService.habilitarPublicacion(1L));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void habilitarPublicacion_CuandoPublicacionYaEstaHabilitada_DeberiaLanzarExcepcion() {
        // Arrange
        publicacion.setEstado(EstadoPublicacion.PUBLICADA);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act & Assert
        assertThrows(PublicacionException.class, () -> reporteService.habilitarPublicacion(1L));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void listarReportes_DeberiaRetornarReportesFiltrados() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(reporteRepositoryPort.findByProcesado(anyBoolean())).thenReturn(Collections.singletonList(reporte));
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act
        List<ReporteResponse> response = reporteService.listarReportes(false);

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void listarReportes_SinFiltro_DeberiaRetornarTodosLosReportes() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(reporteRepositoryPort.findByProcesado(false)).thenReturn(Collections.singletonList(reporte));
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act
        List<ReporteResponse> response = reporteService.listarReportes(null);

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void obtenerReportePorId_DeberiaRetornarReporteCorrectamente() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(reporteRepositoryPort.findById(anyLong())).thenReturn(Optional.of(reporte));

        // Act
        ReporteResponse response = reporteService.obtenerReportePorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(reporte.getId(), response.getId());
        assertEquals(reporte.getPublicacionId(), response.getPublicacionId());
        assertEquals(reporte.getUsuarioId(), response.getUsuarioId());
        assertEquals(reporte.getNombreUsuario(), response.getNombreUsuario());
        assertEquals(reporte.getMotivo(), response.getMotivo());
    }

    @Test
    void obtenerReportePorId_CuandoReporteNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(reporteRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> reporteService.obtenerReportePorId(1L));
    }

    @Test
    void bloquearPublicacion_DeberiaBloquearPublicacionCorrectamente() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(reporteRepositoryPort.findByPublicacionId(anyLong())).thenReturn(Collections.singletonList(reporte));

        // Act
        MessageResponse response = reporteService.bloquearPublicacion(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Publicación bloqueada permanentemente", response.message());
        verify(publicacionRepositoryPort).save(any(Publicacion.class));
        verify(reporteRepositoryPort).save(any(Reporte.class));
    }

    @Test
    void bloquearPublicacion_CuandoPublicacionNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> reporteService.bloquearPublicacion(1L));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }
}
