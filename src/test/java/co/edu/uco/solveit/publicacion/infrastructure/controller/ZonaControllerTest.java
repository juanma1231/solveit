package co.edu.uco.solveit.publicacion.infrastructure.controller;

import co.edu.uco.solveit.publicacion.domain.port.in.ZonaUseCase;
import co.edu.uco.solveit.publicacion.application.dto.ZonaRequest;
import co.edu.uco.solveit.publicacion.application.dto.ZonaResponse;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZonaControllerTest {

    @Mock
    private ZonaUseCase zonaUseCase;

    @InjectMocks
    private ZonaController zonaController;

    private ZonaRequest zonaRequest;
    private ZonaResponse zonaResponse;
    private List<ZonaResponse> zonaResponses;

    @BeforeEach
    void setUp() {
        zonaRequest = new ZonaRequest(
                "Corregimiento Test",
                "Municipio Test",
                "Ciudad Test",
                "Departamento Test",
                "País Test"
        );

        zonaResponse = ZonaResponse.builder()
                .id(1L)
                .corregimiento("Corregimiento Test")
                .municipio("Municipio Test")
                .ciudad("Ciudad Test")
                .departamento("Departamento Test")
                .pais("País Test")
                .build();

        zonaResponses = Arrays.asList(zonaResponse);
    }

    @Test
    void crearZona_DeberiaRetornarZonaResponse() {
        // Arrange
        when(zonaUseCase.crearZona(any(ZonaRequest.class))).thenReturn(zonaResponse);

        // Act
        ZonaResponse result = zonaController.crearZona(zonaRequest);

        // Assert
        assertNotNull(result);
        assertEquals(zonaResponse.id(), result.id());
        assertEquals(zonaResponse.corregimiento(), result.corregimiento());
        assertEquals(zonaResponse.municipio(), result.municipio());
        assertEquals(zonaResponse.ciudad(), result.ciudad());
        assertEquals(zonaResponse.departamento(), result.departamento());
        assertEquals(zonaResponse.pais(), result.pais());
        verify(zonaUseCase).crearZona(zonaRequest);
    }

    @Test
    void actualizarZona_DeberiaRetornarZonaResponse() {
        // Arrange
        Long id = 1L;
        when(zonaUseCase.actualizarZona(anyLong(), any(ZonaRequest.class))).thenReturn(zonaResponse);

        // Act
        ZonaResponse result = zonaController.actualizarZona(id, zonaRequest);

        // Assert
        assertNotNull(result);
        assertEquals(zonaResponse.id(), result.id());
        assertEquals(zonaResponse.corregimiento(), result.corregimiento());
        assertEquals(zonaResponse.municipio(), result.municipio());
        assertEquals(zonaResponse.ciudad(), result.ciudad());
        assertEquals(zonaResponse.departamento(), result.departamento());
        assertEquals(zonaResponse.pais(), result.pais());
        verify(zonaUseCase).actualizarZona(id, zonaRequest);
    }

    @Test
    void obtenerZona_DeberiaRetornarZonaResponse() {
        // Arrange
        Long id = 1L;
        when(zonaUseCase.obtenerZona(anyLong())).thenReturn(zonaResponse);

        // Act
        ZonaResponse result = zonaController.obtenerZona(id);

        // Assert
        assertNotNull(result);
        assertEquals(zonaResponse.id(), result.id());
        assertEquals(zonaResponse.corregimiento(), result.corregimiento());
        assertEquals(zonaResponse.municipio(), result.municipio());
        assertEquals(zonaResponse.ciudad(), result.ciudad());
        assertEquals(zonaResponse.departamento(), result.departamento());
        assertEquals(zonaResponse.pais(), result.pais());
        verify(zonaUseCase).obtenerZona(id);
    }

    @Test
    void listarZonas_DeberiaRetornarListaDeZonaResponse() {
        // Arrange
        when(zonaUseCase.listarZonas()).thenReturn(zonaResponses);

        // Act
        List<ZonaResponse> result = zonaController.listarZonas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(zonaResponse.id(), result.get(0).id());
        assertEquals(zonaResponse.corregimiento(), result.get(0).corregimiento());
        assertEquals(zonaResponse.municipio(), result.get(0).municipio());
        assertEquals(zonaResponse.ciudad(), result.get(0).ciudad());
        assertEquals(zonaResponse.departamento(), result.get(0).departamento());
        assertEquals(zonaResponse.pais(), result.get(0).pais());
        verify(zonaUseCase).listarZonas();
    }

    @Test
    void eliminarZona_DeberiaRetornarMessageResponse() {
        // Arrange
        Long id = 1L;
        doNothing().when(zonaUseCase).eliminarZona(anyLong());

        // Act
        MessageResponse result = zonaController.eliminarZona(id);

        // Assert
        assertNotNull(result);
        assertEquals("Zona eliminada correctamente", result.message());
        assertTrue(result.success());
        verify(zonaUseCase).eliminarZona(id);
    }
}