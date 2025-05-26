package co.edu.uco.solveit.usuario.service;

import co.edu.uco.solveit.usuario.entity.Calificacion;
import co.edu.uco.solveit.usuario.entity.Usuario;
import co.edu.uco.solveit.usuario.repository.CalificacionRepository;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalificacionServiceTest {

    @Mock
    private CalificacionRepository calificacionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CalificacionService calificacionService;

    private Usuario usuario;
    private List<Calificacion> calificaciones;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setNombreCompleto("Test User");

        Calificacion calificacion1 = new Calificacion();
        calificacion1.setId(1L);
        calificacion1.setUsuario(usuario);
        calificacion1.setValor(4);

        Calificacion calificacion2 = new Calificacion();
        calificacion2.setId(2L);
        calificacion2.setUsuario(usuario);
        calificacion2.setValor(5);

        calificaciones = Arrays.asList(calificacion1, calificacion2);
    }

    @Test
    void obtenerCalificacion_DeberiaRetornarPromedioDeCalificaciones() {
        // Arrange
        when(calificacionRepository.findByUsuarioId(anyLong())).thenReturn(calificaciones);

        // Act
        Double calificacionPromedio = calificacionService.obtenerCalificacion(1L);

        // Assert
        assertNotNull(calificacionPromedio);
        assertEquals(4.5, calificacionPromedio); // (4.0 + 5.0) / 2 = 4.5
    }

    @Test
    void obtenerCalificacion_CuandoNoHayCalificaciones_DeberiaRetornarCero() {
        // Arrange
        when(calificacionRepository.findByUsuarioId(anyLong())).thenReturn(Collections.emptyList());

        // Act
        Double calificacionPromedio = calificacionService.obtenerCalificacion(1L);

        // Assert
        assertNotNull(calificacionPromedio);
        assertEquals(0.0, calificacionPromedio);
    }
}
