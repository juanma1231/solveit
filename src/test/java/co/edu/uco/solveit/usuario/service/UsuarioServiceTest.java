package co.edu.uco.solveit.usuario.service;

import co.edu.uco.solveit.usuario.dto.*;
import co.edu.uco.solveit.usuario.entity.Calificacion;
import co.edu.uco.solveit.usuario.entity.Usuario;
import co.edu.uco.solveit.usuario.exception.UsuarioException;
import co.edu.uco.solveit.usuario.repository.CalificacionRepository;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CalificacionRepository calificacionRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private ActualizarUsuarioRequest actualizarUsuarioRequest;
    private SolicitudResetPasswordRequest solicitudResetPasswordRequest;
    private ResetPasswordRequest resetPasswordRequest;
    private CalificarUsuarioRequest calificarUsuarioRequest;

    @BeforeEach
    void setUp() {
        // Set up SecurityContextHolder mock
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        lenient().when(authentication.getName()).thenReturn("testuser");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setPassword("encodedPassword");
        usuario.setEmail("test@example.com");
        usuario.setNombreCompleto("Test User");
        usuario.setNumeroIdentificacion("123456789");
        usuario.setTipoIdentificacion("CC");
        usuario.setDescripcionPerfil("Test description");
        usuario.setTelefono("1234567890");

        actualizarUsuarioRequest = new ActualizarUsuarioRequest(
                "Updated User",
                "987654321",
                "TI",
                "Updated description",
                "0987654321",
                "currentPassword",
                "newPassword"
        );

        solicitudResetPasswordRequest = new SolicitudResetPasswordRequest("test@example.com");

        resetPasswordRequest = new ResetPasswordRequest("resetToken", "newPassword");

        calificarUsuarioRequest = new CalificarUsuarioRequest(1L, 5);
    }

    @Test
    void actualizarDatosUsuario_DeberiaActualizarDatosCorrectamente() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        MessageResponse response = usuarioService.actualizarDatosUsuario(actualizarUsuarioRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Datos actualizados correctamente", response.message());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void actualizarDatosUsuario_CuandoUsuarioNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> usuarioService.actualizarDatosUsuario(actualizarUsuarioRequest));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void actualizarDatosUsuario_CuandoPasswordActualIncorrecta_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(UsuarioException.class, () -> usuarioService.actualizarDatosUsuario(actualizarUsuarioRequest));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void solicitarResetPassword_DeberiaGenerarTokenCorrectamente() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        MessageResponse response = usuarioService.solicitarResetPassword(solicitudResetPasswordRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Se ha enviado un correo con instrucciones para restablecer tu contraseña", response.message());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void solicitarResetPassword_CuandoEmailNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.solicitarResetPassword(solicitudResetPasswordRequest));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void resetPassword_DeberiaRestablecerPasswordCorrectamente() {
        // Arrange
        usuario.setTokenRecuperacion("resetToken");
        usuario.setExpiracionTokenRecuperacion(LocalDateTime.now().plusMinutes(30));
        when(usuarioRepository.findByTokenRecuperacion(anyString())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        MessageResponse response = usuarioService.resetPassword(resetPasswordRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Contraseña actualizada correctamente", response.message());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void resetPassword_CuandoTokenNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findByTokenRecuperacion(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsuarioException.class, () -> usuarioService.resetPassword(resetPasswordRequest));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void resetPassword_CuandoTokenExpirado_DeberiaLanzarExcepcion() {
        // Arrange
        usuario.setTokenRecuperacion("resetToken");
        usuario.setExpiracionTokenRecuperacion(LocalDateTime.now().minusMinutes(30)); // Token expirado
        when(usuarioRepository.findByTokenRecuperacion(anyString())).thenReturn(Optional.of(usuario));

        // Act & Assert
        assertThrows(UsuarioException.class, () -> usuarioService.resetPassword(resetPasswordRequest));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void calificarUsuario_DeberiaCalificarUsuarioCorrectamente() {
        // Arrange
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(calificacionRepository.save(any(Calificacion.class))).thenAnswer(invocation -> {
            Calificacion calificacion = invocation.getArgument(0);
            calificacion.setId(1L);
            return calificacion;
        });

        // Act
        MessageResponse response = usuarioService.calificarUsuario(calificarUsuarioRequest);

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Usuario calificado con exito", response.message());
        verify(calificacionRepository).save(any(Calificacion.class));
    }

    @Test
    void calificarUsuario_CuandoUsuarioNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> usuarioService.calificarUsuario(calificarUsuarioRequest));
        verify(calificacionRepository, never()).save(any(Calificacion.class));
    }
}
