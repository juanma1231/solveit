package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.publicacion.application.dto.ZonaRequest;
import co.edu.uco.solveit.publicacion.application.dto.ZonaResponse;
import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.domain.port.out.ZonaRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZonaServiceTest {

    @Mock
    private ZonaRepositoryPort zonaRepositoryPort;

    @InjectMocks
    private ZonaService zonaService;

    private Zona zona;
    private ZonaRequest zonaRequest;

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

        zonaRequest = new ZonaRequest(
                "Corregimiento Test",
                "Municipio Test",
                "Ciudad Test",
                "Departamento Test",
                "País Test"
        );
    }

    @Test
    void crearZona_DeberiaCrearZonaCorrectamente() {
        // Arrange
        when(zonaRepositoryPort.save(any(Zona.class))).thenReturn(zona);

        // Act
        ZonaResponse response = zonaService.crearZona(zonaRequest);

        // Assert
        assertNotNull(response);
        assertEquals(zona.getId(), response.id());
        assertEquals(zona.getCorregimiento(), response.corregimiento());
        assertEquals(zona.getMunicipio(), response.municipio());
        assertEquals(zona.getCiudad(), response.ciudad());
        assertEquals(zona.getDepartamento(), response.departamento());
        assertEquals(zona.getPais(), response.pais());
        verify(zonaRepositoryPort).save(any(Zona.class));
    }

    @Test
    void actualizarZona_DeberiaActualizarZonaCorrectamente() {
        // Arrange
        when(zonaRepositoryPort.findById(anyLong())).thenReturn(Optional.of(zona));
        when(zonaRepositoryPort.save(any(Zona.class))).thenReturn(zona);

        // Act
        ZonaResponse response = zonaService.actualizarZona(1L, zonaRequest);

        // Assert
        assertNotNull(response);
        assertEquals(zona.getId(), response.id());
        assertEquals(zona.getCorregimiento(), response.corregimiento());
        assertEquals(zona.getMunicipio(), response.municipio());
        assertEquals(zona.getCiudad(), response.ciudad());
        assertEquals(zona.getDepartamento(), response.departamento());
        assertEquals(zona.getPais(), response.pais());
        verify(zonaRepositoryPort).save(any(Zona.class));
    }

    @Test
    void actualizarZona_CuandoZonaNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(zonaRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> zonaService.actualizarZona(1L, zonaRequest));
        verify(zonaRepositoryPort, never()).save(any(Zona.class));
    }

    @Test
    void obtenerZona_DeberiaRetornarZonaCorrectamente() {
        // Arrange
        when(zonaRepositoryPort.findById(anyLong())).thenReturn(Optional.of(zona));

        // Act
        ZonaResponse response = zonaService.obtenerZona(1L);

        // Assert
        assertNotNull(response);
        assertEquals(zona.getId(), response.id());
        assertEquals(zona.getCorregimiento(), response.corregimiento());
        assertEquals(zona.getMunicipio(), response.municipio());
        assertEquals(zona.getCiudad(), response.ciudad());
        assertEquals(zona.getDepartamento(), response.departamento());
        assertEquals(zona.getPais(), response.pais());
    }

    @Test
    void obtenerZona_CuandoZonaNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(zonaRepositoryPort.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PublicacionException.class, () -> zonaService.obtenerZona(1L));
    }

    @Test
    void listarZonas_DeberiaRetornarListaDeZonas() {
        // Arrange
        when(zonaRepositoryPort.findAll()).thenReturn(Collections.singletonList(zona));

        // Act
        List<ZonaResponse> response = zonaService.listarZonas();

        // Assert
        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
        assertEquals(zona.getId(), response.get(0).id());
        assertEquals(zona.getCorregimiento(), response.get(0).corregimiento());
        assertEquals(zona.getMunicipio(), response.get(0).municipio());
        assertEquals(zona.getCiudad(), response.get(0).ciudad());
        assertEquals(zona.getDepartamento(), response.get(0).departamento());
        assertEquals(zona.getPais(), response.get(0).pais());
    }

    @Test
    void eliminarZona_DeberiaEliminarZonaCorrectamente() {
        // Arrange
        when(zonaRepositoryPort.existsById(anyLong())).thenReturn(true);
        doNothing().when(zonaRepositoryPort).deleteById(anyLong());

        // Act
        assertDoesNotThrow(() -> zonaService.eliminarZona(1L));

        // Assert
        verify(zonaRepositoryPort).deleteById(anyLong());
    }

    @Test
    void eliminarZona_CuandoZonaNoExiste_DeberiaLanzarExcepcion() {
        // Arrange
        when(zonaRepositoryPort.existsById(anyLong())).thenReturn(false);

        // Act & Assert
        assertThrows(PublicacionException.class, () -> zonaService.eliminarZona(1L));
        verify(zonaRepositoryPort, never()).deleteById(anyLong());
    }
}
