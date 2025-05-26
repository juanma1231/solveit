package co.edu.uco.solveit.usuario.service;

import co.edu.uco.solveit.usuario.dto.AuthResponse;
import co.edu.uco.solveit.usuario.dto.LoginRequest;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import co.edu.uco.solveit.usuario.dto.RegistroRequest;
import co.edu.uco.solveit.usuario.entity.Role;
import co.edu.uco.solveit.usuario.entity.Usuario;
import co.edu.uco.solveit.usuario.exception.AuthenticationException;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import co.edu.uco.solveit.usuario.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegistroRequest registroRequest;
    private LoginRequest loginRequest;
    private Usuario usuario;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        registroRequest = new RegistroRequest(
                "testuser",
                "password123",
                "test@example.com",
                "Test User",
                "123456789",
                "CC",
                "Test description",
                "1234567890"
        );

        loginRequest = new LoginRequest("testuser", "password123");

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
        usuario.setRole(Role.USER);

        jwtToken = "test.jwt.token";
    }

    @Test
    void register_DeberiaRegistrarUsuarioCorrectamente() {
        // Arrange
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.generateToken(any(Usuario.class))).thenReturn(jwtToken);

        // Act
        AuthResponse response = authService.register(registroRequest);

        // Assert
        assertNotNull(response);
        assertEquals(jwtToken, response.token());
        assertEquals(usuario.getUsername(), response.username());
        assertEquals(usuario.getEmail(), response.email());
        assertEquals(usuario.getRole(), response.role());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void register_CuandoUsernameExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> authService.register(registroRequest));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void register_CuandoEmailExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(AuthenticationException.class, () -> authService.register(registroRequest));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void authenticate_DeberiaAutenticarUsuarioCorrectamente() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any(Usuario.class))).thenReturn(jwtToken);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        AuthResponse response = authService.authenticate(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(jwtToken, response.token());
        assertEquals(usuario.getUsername(), response.username());
        assertEquals(usuario.getEmail(), response.email());
        assertEquals(usuario.getRole(), response.role());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(usuarioRepository).save(any(Usuario.class)); // Verify that ultimoLogin is updated
    }

    @Test
    void authenticate_CuandoUsuarioNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new RuntimeException("Usuario no encontrado"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.authenticate(loginRequest));
    }

    @Test
    void logout_DeberiaRetornarMensajeExitoso() {
        // Act
        MessageResponse response = authService.logout();

        // Assert
        assertNotNull(response);
        assertTrue(response.success());
        assertEquals("Sesión cerrada correctamente", response.message());
    }
}