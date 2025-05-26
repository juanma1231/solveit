package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.publicacion.application.dto.ActualizarPublicacionRequest;
import co.edu.uco.solveit.publicacion.application.dto.CrearPublicacionRequest;
import co.edu.uco.solveit.publicacion.application.dto.PublicacionResponse;
import co.edu.uco.solveit.publicacion.application.dto.ReportarPublicacionRequest;
import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.Reporte;
import co.edu.uco.solveit.publicacion.domain.model.Solicitud;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.ReporteRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.SolicitudRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.ZonaRepositoryPort;
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
class PublicacionServiceTest {

    @Mock
    private PublicacionRepositoryPort publicacionRepositoryPort;

    @Mock
    private ZonaRepositoryPort zonaRepositoryPort;

    @Mock
    private ReporteRepositoryPort reporteRepositoryPort;

    @Mock
    private SolicitudRepositoryPort solicitudRepositoryPort;

    @Mock
    private UsuarioApi usuarioApi;

    @InjectMocks
    private PublicacionService publicacionService;

    private Zona zona;
    private Publicacion publicacion;
    private CrearPublicacionRequest crearPublicacionRequest;
    private ActualizarPublicacionRequest actualizarPublicacionRequest;

    @BeforeEach
    void setUp() {
        zona = Zona.builder()
                .id(1L)
                .corregimiento("Corregimiento Test")
                .municipio("Municipio Test")
                .ciudad("Ciudad Test")
                .departamento("Departamento Test")
                .pais("País Test")
                .build();

        publicacion = Publicacion.builder()
                .id(1L)
                .titulo("Título Test")
                .descripcion("Descripción Test")
                .usuarioId(1L)
                .nombreUsuario("Usuario Test")
                .tipoPublicacion(TipoPublicacion.OFERTA)
                .categoriaServicio("Categoría Test")
                .zonaId(1L)
                .zona(zona)
                .estado(EstadoPublicacion.PUBLICADA)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        crearPublicacionRequest = new CrearPublicacionRequest(
                "Título Test",
                "Descripción Test",
                TipoPublicacion.OFERTA,
                "Categoría Test",
                1L
        );

        actualizarPublicacionRequest = new ActualizarPublicacionRequest(
                "Título Actualizado",
                "Descripción Actualizada",
                TipoPublicacion.OFERTA,
                "Categoría Actualizada",
                1L
        );
    }

    @Test
    void crearPublicacion_DeberiaCrearPublicacionCorrectamente() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(usuarioApi.getCurrentUserFullName()).thenReturn("Usuario Test");
        when(zonaRepositoryPort.findById(anyLong())).thenReturn(Optional.of(zona));
        when(publicacionRepositoryPort.save(any(Publicacion.class))).thenReturn(publicacion);

        // Act
        PublicacionResponse response = publicacionService.crearPublicacion(crearPublicacionRequest);

        // Assert
        assertNotNull(response);
        assertEquals(publicacion.getId(), response.id());
        assertEquals(publicacion.getTitulo(), response.titulo());
        assertEquals(publicacion.getDescripcion(), response.descripcion());
        assertEquals(publicacion.getUsuarioId(), response.usuarioId());
        assertEquals(publicacion.getNombreUsuario(), response.nombreUsuario());
        assertEquals(publicacion.getTipoPublicacion(), response.tipoPublicacion());
        assertEquals(publicacion.getCategoriaServicio(), response.categoriaServicio());
        assertEquals(publicacion.getZonaId(), response.zonaId());
        assertEquals(publicacion.getEstado(), response.estado());

        verify(publicacionRepositoryPort).save(any(Publicacion.class));
    }

    @Test
    void crearPublicacion_CuandoZonaNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(usuarioApi.getCurrentUserFullName()).thenReturn("Usuario Test");
        when(zonaRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> publicacionService.crearPublicacion(crearPublicacionRequest));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void actualizarPublicacion_DeberiaActualizarPublicacionCorrectamente() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(zonaRepositoryPort.findById(anyLong())).thenReturn(Optional.of(zona));
        when(solicitudRepositoryPort.findByPublicacionIdAndEstadoIn(anyLong(), anyList())).thenReturn(Collections.emptyList());
        when(publicacionRepositoryPort.save(any(Publicacion.class))).thenReturn(publicacion);

        // Act
        PublicacionResponse response = publicacionService.actualizarPublicacion(1L, actualizarPublicacionRequest);

        // Assert
        assertNotNull(response);
        verify(publicacionRepositoryPort).save(any(Publicacion.class));
    }

    @Test
    void actualizarPublicacion_CuandoPublicacionNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> publicacionService.actualizarPublicacion(1L, actualizarPublicacionRequest));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void actualizarPublicacion_CuandoUsuarioNoEsPropietario_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L); // Usuario diferente
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act & Assert
        assertThrows(PublicacionException.class, () -> publicacionService.actualizarPublicacion(1L, actualizarPublicacionRequest));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void actualizarPublicacion_CuandoPublicacionEstaCancelada_DeberiaLanzarExcepcion() {
        // Arrange
        publicacion.setEstado(EstadoPublicacion.CANCELADA);
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act & Assert
        assertThrows(PublicacionException.class, () -> publicacionService.actualizarPublicacion(1L, actualizarPublicacionRequest));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void actualizarPublicacion_CuandoTieneInteresesVigentes_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        List<Solicitud> interesesVigentes = Collections.singletonList(
            Solicitud.builder().id(1L).build()
        );
        when(solicitudRepositoryPort.findByPublicacionIdAndEstadoIn(anyLong(), anyList())).thenReturn(interesesVigentes);

        // Act & Assert
        assertThrows(PublicacionException.class, () -> publicacionService.actualizarPublicacion(1L, actualizarPublicacionRequest));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void obtenerPublicacion_DeberiaRetornarPublicacionCorrectamente() {
        // Arrange
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act
        PublicacionResponse response = publicacionService.obtenerPublicacion(1L);

        // Assert
        assertNotNull(response);
        assertEquals(publicacion.getId(), response.id());
        assertEquals(publicacion.getTitulo(), response.titulo());
    }

    @Test
    void obtenerPublicacion_CuandoPublicacionNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> publicacionService.obtenerPublicacion(1L));
    }

    @Test
    void listarPublicaciones_DeberiaRetornarListaDePublicaciones() {
        // Arrange
        List<Publicacion> publicaciones = Collections.singletonList(publicacion);
        when(publicacionRepositoryPort.findByEstado(any(EstadoPublicacion.class))).thenReturn(publicaciones);

        // Act
        List<PublicacionResponse> response = publicacionService.listarPublicaciones(null);

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void listarPublicaciones_ConTipoPublicacion_DeberiaFiltrarCorrectamente() {
        // Arrange
        List<Publicacion> publicaciones = Collections.singletonList(publicacion);
        when(publicacionRepositoryPort.findByTipoPublicacionAndEstado(any(TipoPublicacion.class), any(EstadoPublicacion.class)))
                .thenReturn(publicaciones);

        // Act
        List<PublicacionResponse> response = publicacionService.listarPublicaciones(TipoPublicacion.OFERTA);

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void listarMisPublicaciones_DeberiaRetornarPublicacionesDelUsuario() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        List<Publicacion> publicaciones = Collections.singletonList(publicacion);
        when(publicacionRepositoryPort.findByUsuarioId(anyLong())).thenReturn(publicaciones);

        // Act
        List<PublicacionResponse> response = publicacionService.listarMisPublicaciones();

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void cancelarPublicacion_DeberiaCancelarPublicacionCorrectamente() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(solicitudRepositoryPort.findByPublicacionIdAndEstadoIn(anyLong(), anyList())).thenReturn(Collections.emptyList());

        // Act
        MessageResponse response = publicacionService.cancelarPublicacion(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Publicación cancelada correctamente", response.message());
        verify(publicacionRepositoryPort).save(any(Publicacion.class));
    }

    @Test
    void cancelarPublicacion_CuandoPublicacionNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> publicacionService.cancelarPublicacion(1L));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void cancelarPublicacion_CuandoUsuarioNoEsPropietario_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L); // Usuario diferente
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act & Assert
        assertThrows(PublicacionException.class, () -> publicacionService.cancelarPublicacion(1L));
        verify(publicacionRepositoryPort, never()).save(any(Publicacion.class));
    }

    @Test
    void reportarPublicacion_DeberiaReportarPublicacionCorrectamente() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L); // Usuario diferente al propietario
        when(usuarioApi.getCurrentUserFullName()).thenReturn("Usuario Reportador");
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(reporteRepositoryPort.existsByPublicacionIdAndUsuarioId(anyLong(), anyLong())).thenReturn(false);
        when(reporteRepositoryPort.countByPublicacionId(anyLong())).thenReturn(1L); // Primer reporte

        ReportarPublicacionRequest request = new ReportarPublicacionRequest("Motivo del reporte");

        // Act
        MessageResponse response = publicacionService.reportarPublicacion(1L, request);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Publicación reportada correctamente", response.message());
        verify(reporteRepositoryPort).save(any(Reporte.class));
    }

    @Test
    void reportarPublicacion_CuandoYaHaReportado_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(reporteRepositoryPort.existsByPublicacionIdAndUsuarioId(anyLong(), anyLong())).thenReturn(true);

        ReportarPublicacionRequest request = new ReportarPublicacionRequest("Motivo del reporte");

        // Act & Assert
        assertThrows(PublicacionException.class, () -> publicacionService.reportarPublicacion(1L, request));
        verify(reporteRepositoryPort, never()).save(any(Reporte.class));
    }

    @Test
    void reportarPublicacion_CuandoHayMasDeTresReportes_DeberiaMarcarComoReportada() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L);
        when(usuarioApi.getCurrentUserFullName()).thenReturn("Usuario Reportador");
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(reporteRepositoryPort.existsByPublicacionIdAndUsuarioId(anyLong(), anyLong())).thenReturn(false);
        when(reporteRepositoryPort.countByPublicacionId(anyLong())).thenReturn(3L); // 3 reportes

        ReportarPublicacionRequest request = new ReportarPublicacionRequest("Motivo del reporte");

        // Act
        MessageResponse response = publicacionService.reportarPublicacion(1L, request);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        verify(publicacionRepositoryPort).save(any(Publicacion.class)); // Verifica que se guarde con estado REPORTADA
    }
}
