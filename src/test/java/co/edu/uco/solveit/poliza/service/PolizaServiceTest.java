package co.edu.uco.solveit.poliza.service;

import co.edu.uco.solveit.poliza.dto.ActualizarPolizaRequest;
import co.edu.uco.solveit.poliza.dto.PolizaResponse;
import co.edu.uco.solveit.poliza.dto.RegistrarPolizaRequest;
import co.edu.uco.solveit.poliza.entity.Poliza;
import co.edu.uco.solveit.poliza.exception.PolizaException;
import co.edu.uco.solveit.poliza.repository.PolizaRepository;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import co.edu.uco.solveit.usuario.entity.Role;
import co.edu.uco.solveit.usuario.entity.Usuario;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolizaServiceTest {

    @Mock
    private PolizaRepository polizaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PolizaService polizaService;

    private Usuario usuario;
    private Usuario adminUsuario;
    private Poliza poliza;
    private RegistrarPolizaRequest registrarPolizaRequest;
    private ActualizarPolizaRequest actualizarPolizaRequest;
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        // Set up SecurityContextHolder mock
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        lenient().when(authentication.getName()).thenReturn("testUser");

        // Set up test data
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testUser");
        usuario.setNombreCompleto("Test User");
        usuario.setRole(Role.USER);

        adminUsuario = new Usuario();
        adminUsuario.setId(2L);
        adminUsuario.setUsername("adminUser");
        adminUsuario.setNombreCompleto("Admin User");
        adminUsuario.setRole(Role.ADMIN);

        poliza = new Poliza();
        poliza.setId(1L);
        poliza.setTitular(usuario);
        poliza.setNumeroPoliza("POL-123");
        poliza.setNombreAseguradora("Test Insurance");
        poliza.setPrima(100.00);
        poliza.setFechaEmision(LocalDate.now().minusDays(10));
        poliza.setFechaVencimiento(LocalDate.now().plusYears(1));
        poliza.setTipoPoliza("Vida");

        registrarPolizaRequest = new RegistrarPolizaRequest(
                "POL-123",
                "Test Insurance",
                100.00,
                LocalDate.now().minusDays(10),
                LocalDate.now().plusYears(1),
                "Vida"
        );

        actualizarPolizaRequest = new ActualizarPolizaRequest(
                "POL-123-Updated",
                "Updated Insurance",
                150.00,
                LocalDate.now().minusDays(5),
                LocalDate.now().plusYears(2),
                "Salud"
        );

        mockFile = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                "test content".getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    void registrarPoliza_DeberiaRegistrarPolizaCorrectamente() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(polizaRepository.save(any(Poliza.class))).thenReturn(poliza);

        // Act
        PolizaResponse response = polizaService.registrarPoliza(registrarPolizaRequest, mockFile);

        // Assert
        assertNotNull(response);
        assertEquals(poliza.getId(), response.id());
        assertEquals(poliza.getTitular().getId(), response.idTitular());
        assertEquals(poliza.getNumeroPoliza(), response.numeroPoliza());
        verify(polizaRepository).save(any(Poliza.class));
    }

    @Test
    void registrarPoliza_CuandoUsuarioNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> polizaService.registrarPoliza(registrarPolizaRequest, mockFile));
        verify(polizaRepository, never()).save(any(Poliza.class));
    }

    @Test
    void actualizarPoliza_DeberiaActualizarPolizaCorrectamente() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(polizaRepository.findById(anyLong())).thenReturn(Optional.of(poliza));
        when(polizaRepository.save(any(Poliza.class))).thenReturn(poliza);

        // Act
        PolizaResponse response = polizaService.actualizarPoliza(1L, actualizarPolizaRequest, mockFile);

        // Assert
        assertNotNull(response);
        verify(polizaRepository).save(any(Poliza.class));
    }

    @Test
    void actualizarPoliza_CuandoPolizaNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(polizaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PolizaException.class, () -> polizaService.actualizarPoliza(1L, actualizarPolizaRequest, mockFile));
        verify(polizaRepository, never()).save(any(Poliza.class));
    }

    @Test
    void actualizarPoliza_CuandoUsuarioNoEsTitular_DeberiaLanzarExcepcion() {
        // Arrange
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(3L);
        otroUsuario.setUsername("otroUsuario");

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(otroUsuario));
        when(polizaRepository.findById(anyLong())).thenReturn(Optional.of(poliza));

        // Act & Assert
        assertThrows(PolizaException.class, () -> polizaService.actualizarPoliza(1L, actualizarPolizaRequest, mockFile));
        verify(polizaRepository, never()).save(any(Poliza.class));
    }

    @Test
    void obtenerPoliza_DeberiaRetornarPolizaCorrectamente() {
        // Arrange
        when(polizaRepository.findById(anyLong())).thenReturn(Optional.of(poliza));

        // Act
        PolizaResponse response = polizaService.obtenerPoliza(1L);

        // Assert
        assertNotNull(response);
        assertEquals(poliza.getId(), response.id());
        assertEquals(poliza.getTitular().getId(), response.idTitular());
        assertEquals(poliza.getNumeroPoliza(), response.numeroPoliza());
    }

    @Test
    void obtenerPoliza_CuandoPolizaNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(polizaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PolizaException.class, () -> polizaService.obtenerPoliza(1L));
    }

    @Test
    void obtenerPolizasUsuario_CuandoEsUsuarioAutenticado_DeberiaRetornarPolizas() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(polizaRepository.findByTitularId(anyLong())).thenReturn(Collections.singletonList(poliza));

        // Act
        List<PolizaResponse> response = polizaService.obtenerPolizasUsuario(1L);

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void obtenerPolizasUsuario_CuandoEsAdmin_DeberiaRetornarPolizas() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(adminUsuario));
        when(polizaRepository.findByTitularId(anyLong())).thenReturn(Collections.singletonList(poliza));

        // Act
        List<PolizaResponse> response = polizaService.obtenerPolizasUsuario(1L);

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void obtenerPolizasUsuario_CuandoNoEsUsuarioAutenticadoNiAdmin_DeberiaLanzarExcepcion() {
        // Arrange
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(3L);
        otroUsuario.setUsername("otroUsuario");
        otroUsuario.setRole(Role.USER);

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(otroUsuario));

        // Act & Assert
        assertThrows(PolizaException.class, () -> polizaService.obtenerPolizasUsuario(1L));
    }

    @Test
    void obtenerMisPolizas_DeberiaRetornarPolizasDelUsuarioAutenticado() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(polizaRepository.findByTitular(any(Usuario.class))).thenReturn(Collections.singletonList(poliza));

        // Act
        List<PolizaResponse> response = polizaService.obtenerMisPolizas();

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void eliminarPoliza_DeberiaEliminarPolizaCorrectamente() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(polizaRepository.findById(anyLong())).thenReturn(Optional.of(poliza));
        doNothing().when(polizaRepository).delete(any(Poliza.class));

        // Act
        MessageResponse response = polizaService.eliminarPoliza(1L);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Póliza eliminada correctamente", response.message());
        verify(polizaRepository).delete(poliza);
    }

    @Test
    void eliminarPoliza_CuandoPolizaNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(polizaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PolizaException.class, () -> polizaService.eliminarPoliza(1L));
        verify(polizaRepository, never()).delete(any(Poliza.class));
    }

    @Test
    void eliminarPoliza_CuandoUsuarioNoEsTitularNiAdmin_DeberiaLanzarExcepcion() {
        // Arrange
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(3L);
        otroUsuario.setUsername("otroUsuario");
        otroUsuario.setRole(Role.USER);

        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(otroUsuario));
        when(polizaRepository.findById(anyLong())).thenReturn(Optional.of(poliza));

        // Act & Assert
        assertThrows(PolizaException.class, () -> polizaService.eliminarPoliza(1L));
        verify(polizaRepository, never()).delete(any(Poliza.class));
    }
}
