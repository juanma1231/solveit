package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.publicacion.application.dto.CrearSolicitudRequest;
import co.edu.uco.solveit.publicacion.application.dto.SolicitudResponse;
import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.Solicitud;
import co.edu.uco.solveit.publicacion.domain.port.out.EmailServicePort;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.SolicitudRepositoryPort;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import co.edu.uco.solveit.usuario.entity.Usuario;
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
class SolicitudServiceTest {

    @Mock
    private SolicitudRepositoryPort solicitudRepositoryPort;

    @Mock
    private PublicacionRepositoryPort publicacionRepositoryPort;

    @Mock
    private EmailServicePort emailServicePort;

    @Mock
    private UsuarioApi usuarioApi;

    @InjectMocks
    private SolicitudService solicitudService;

    private Publicacion publicacion;
    private Solicitud solicitud;
    private Usuario usuario;
    private CrearSolicitudRequest crearSolicitudRequest;

    @BeforeEach
    void setUp() {
        publicacion = Publicacion.builder()
                .id(1L)
                .titulo("Título Test")
                .descripcion("Descripción Test")
                .usuarioId(1L)
                .nombreUsuario("Usuario Propietario")
                .estado(EstadoPublicacion.PUBLICADA)
                .build();

        solicitud = Solicitud.builder()
                .id(1L)
                .publicacionId(1L)
                .publicacion(publicacion)
                .usuarioInteresadoId(2L)
                .nombreUsuarioInteresado("Usuario Interesado")
                .titulo("Título Solicitud")
                .descripcion("Descripción Solicitud")
                .estado(EstadoInteres.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@test.com");
        usuario.setNombreCompleto("Usuario Test");

        crearSolicitudRequest = new CrearSolicitudRequest(
                1L,
                "Título Solicitud",
                "Descripción Solicitud"
        );
    }

    @Test
    void mostraSolicitud_DeberiaCrearSolicitudCorrectamente() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L);
        when(usuarioApi.getCurrentUserFullName()).thenReturn("Usuario Interesado");
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(solicitudRepositoryPort.existsByPublicacionIdAndUsuarioInteresadoId(anyLong(), anyLong())).thenReturn(false);
        when(solicitudRepositoryPort.save(any(Solicitud.class))).thenReturn(solicitud);
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        doNothing().when(emailServicePort).enviarNotificacionNuevaSolicitud(anyString(), anyString(), anyString());

        // Act
        SolicitudResponse response = solicitudService.mostraSolicitud(crearSolicitudRequest);

        // Assert
        assertNotNull(response);
        assertEquals(solicitud.getId(), response.id());
        assertEquals(solicitud.getPublicacionId(), response.publicacionId());
        assertEquals(solicitud.getUsuarioInteresadoId(), response.usuarioInteresadoId());
        assertEquals(solicitud.getNombreUsuarioInteresado(), response.nombreUsuarioInteresado());
        assertEquals(solicitud.getTitulo(), response.titulo());
        assertEquals(solicitud.getDescripcion(), response.descripcion());
        assertEquals(solicitud.getEstado(), response.estado());

        verify(solicitudRepositoryPort).save(any(Solicitud.class));
        verify(emailServicePort).enviarNotificacionNuevaSolicitud(anyString(), anyString(), anyString());
    }

    @Test
    void mostraSolicitud_CuandoPublicacionNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> solicitudService.mostraSolicitud(crearSolicitudRequest));
        verify(solicitudRepositoryPort, never()).save(any(Solicitud.class));
    }

    @Test
    void mostraSolicitud_CuandoUsuarioEsPropietario_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L); // Mismo ID que el propietario
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act & Assert
        assertThrows(PublicacionException.class, () -> solicitudService.mostraSolicitud(crearSolicitudRequest));
        verify(solicitudRepositoryPort, never()).save(any(Solicitud.class));
    }

    @Test
    void mostraSolicitud_CuandoPublicacionNoEstaPublicada_DeberiaLanzarExcepcion() {
        // Arrange
        publicacion.setEstado(EstadoPublicacion.CANCELADA);
        when(usuarioApi.getCurrentUserId()).thenReturn(2L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act & Assert
        assertThrows(PublicacionException.class, () -> solicitudService.mostraSolicitud(crearSolicitudRequest));
        verify(solicitudRepositoryPort, never()).save(any(Solicitud.class));
    }

    @Test
    void mostraSolicitud_CuandoYaHaMostradoInteres_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L);
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(solicitudRepositoryPort.existsByPublicacionIdAndUsuarioInteresadoId(anyLong(), anyLong())).thenReturn(true);

        // Act & Assert
        assertThrows(PublicacionException.class, () -> solicitudService.mostraSolicitud(crearSolicitudRequest));
        verify(solicitudRepositoryPort, never()).save(any(Solicitud.class));
    }

    @Test
    void listarSolicitudPorPublicacion_DeberiaRetornarSolicitudesDePublicacion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L); // Propietario de la publicación
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(solicitudRepositoryPort.findByPublicacionId(anyLong())).thenReturn(Collections.singletonList(solicitud));

        // Act
        List<SolicitudResponse> response = solicitudService.listarSolicitudPorPublicacion(1L);

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void listarSolicitudPorPublicacion_CuandoNoEsPropietario_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L); // No es propietario
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act & Assert
        assertThrows(PublicacionException.class, () -> solicitudService.listarSolicitudPorPublicacion(1L));
    }

    @Test
    void listarSolicitudEnMisPublicaciones_DeberiaRetornarSolicitudesEnPublicacionesDelUsuario() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(publicacionRepositoryPort.findByUsuarioId(anyLong())).thenReturn(Collections.singletonList(publicacion));
        when(solicitudRepositoryPort.findByPublicacionId(anyLong())).thenReturn(Collections.singletonList(solicitud));

        // Act
        List<SolicitudResponse> response = solicitudService.listarSolicitudEnMisPublicaciones();

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void listarMisSolicitud_DeberiaRetornarSolicitudesDelUsuario() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L);
        when(solicitudRepositoryPort.findByUsuarioInteresadoId(anyLong())).thenReturn(Collections.singletonList(solicitud));

        // Act
        List<SolicitudResponse> response = solicitudService.listarMisSolicitud();

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void aceptarSolicitud_DeberiaAceptarSolicitudCorrectamente() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L); // Propietario
        when(solicitudRepositoryPort.findById(anyLong())).thenReturn(Optional.of(solicitud));
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        doNothing().when(emailServicePort).enviarNotificacionSolicitudAceptada(anyString(), anyString());

        // Act
        MessageResponse response = solicitudService.aceptarSolicitud(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Interés aceptado correctamente. Se ha habilitado el chat con el usuario interesado.", response.message());
        verify(solicitudRepositoryPort).save(any(Solicitud.class));
        verify(emailServicePort).enviarNotificacionSolicitudAceptada(anyString(), anyString());
    }

    @Test
    void aceptarSolicitud_CuandoNoEsPropietario_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(2L); // No es propietario
        when(solicitudRepositoryPort.findById(anyLong())).thenReturn(Optional.of(solicitud));
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act & Assert
        assertThrows(PublicacionException.class, () -> solicitudService.aceptarSolicitud(1L));
        verify(solicitudRepositoryPort, never()).save(any(Solicitud.class));
    }

    @Test
    void aceptarSolicitud_CuandoSolicitudYaProcesada_DeberiaLanzarExcepcion() {
        // Arrange
        solicitud.setEstado(EstadoInteres.ACEPTADO); // Ya procesada
        when(usuarioApi.getCurrentUserId()).thenReturn(1L);
        when(solicitudRepositoryPort.findById(anyLong())).thenReturn(Optional.of(solicitud));
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));

        // Act & Assert
        assertThrows(PublicacionException.class, () -> solicitudService.aceptarSolicitud(1L));
        verify(solicitudRepositoryPort, never()).save(any(Solicitud.class));
    }

    @Test
    void rechazarSolicitud_DeberiaRechazarSolicitudCorrectamente() {
        // Arrange
        when(usuarioApi.getCurrentUserId()).thenReturn(1L); // Propietario
        when(solicitudRepositoryPort.findById(anyLong())).thenReturn(Optional.of(solicitud));
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        doNothing().when(emailServicePort).enviarNotificacionSolicitudRechazada(anyString(), anyString());

        // Act
        MessageResponse response = solicitudService.rechazarSolicitud(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Interés rechazado correctamente. Se ha notificado al usuario interesado.", response.message());
        verify(solicitudRepositoryPort).save(any(Solicitud.class));
        verify(emailServicePort).enviarNotificacionSolicitudRechazada(anyString(), anyString());
    }

    @Test
    void finalizarSolicitud_DeberiaFinalizarSolicitudCorrectamente() {
        // Arrange
        solicitud.setEstado(EstadoInteres.ACEPTADO); // Debe estar aceptada para finalizar
        when(usuarioApi.getCurrentUserId()).thenReturn(1L); // Propietario
        when(solicitudRepositoryPort.findById(anyLong())).thenReturn(Optional.of(solicitud));
        when(publicacionRepositoryPort.findById(anyLong())).thenReturn(Optional.of(publicacion));
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        doNothing().when(emailServicePort).enviarNotificacionSolicitudRechazada(anyString(), anyString());

        // Act
        MessageResponse response = solicitudService.finalizarSolicitud(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Interés finalizado correctamente. Se ha notificado al usuario interesado.", response.message());
        verify(solicitudRepositoryPort).save(any(Solicitud.class));
        verify(emailServicePort).enviarNotificacionSolicitudRechazada(anyString(), anyString());
    }

    @Test
    void obtenerSolicitudPorId_DeberiaRetornarSolicitudCorrectamente() {
        // Arrange
        when(solicitudRepositoryPort.findById(anyLong())).thenReturn(Optional.of(solicitud));

        // Act
        SolicitudResponse response = solicitudService.obtenerSolicitudPorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals(solicitud.getId(), response.id());
        assertEquals(solicitud.getPublicacionId(), response.publicacionId());
        assertEquals(solicitud.getUsuarioInteresadoId(), response.usuarioInteresadoId());
    }

    @Test
    void obtenerSolicitudPorId_CuandoSolicitudNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(solicitudRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> solicitudService.obtenerSolicitudPorId(1L));
    }
}
