package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ZonaEntity;
import co.edu.uco.solveit.publicacion.infrastructure.repository.ZonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ZonaRepositoryAdapterTest {

    @Mock
    private ZonaRepository zonaRepository;

    @InjectMocks
    private ZonaRepositoryAdapter zonaRepositoryAdapter;

    private ZonaEntity zonaEntity;
    private Zona zona;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void save_DeberiaGuardarZonaCorrectamente() {
        // Arrange
        when(zonaRepository.save(any(ZonaEntity.class))).thenReturn(zonaEntity);

        // Act
        Zona resultado = zonaRepositoryAdapter.save(zona);

        // Assert
        assertNotNull(resultado);
        assertEquals(zona.getId(), resultado.getId());
        assertEquals(zona.getCorregimiento(), resultado.getCorregimiento());
        assertEquals(zona.getMunicipio(), resultado.getMunicipio());
        assertEquals(zona.getCiudad(), resultado.getCiudad());
        assertEquals(zona.getDepartamento(), resultado.getDepartamento());
        assertEquals(zona.getPais(), resultado.getPais());
        verify(zonaRepository).save(any(ZonaEntity.class));
    }

    @Test
    void findById_CuandoExisteZona_DeberiaRetornarZona() {
        // Arrange
        when(zonaRepository.findById(anyLong())).thenReturn(Optional.of(zonaEntity));

        // Act
        Optional<Zona> resultado = zonaRepositoryAdapter.findById(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(zona.getId(), resultado.get().getId());
        assertEquals(zona.getCorregimiento(), resultado.get().getCorregimiento());
        assertEquals(zona.getMunicipio(), resultado.get().getMunicipio());
        assertEquals(zona.getCiudad(), resultado.get().getCiudad());
        assertEquals(zona.getDepartamento(), resultado.get().getDepartamento());
        assertEquals(zona.getPais(), resultado.get().getPais());
        verify(zonaRepository).findById(1L);
    }

    @Test
    void findById_CuandoNoExisteZona_DeberiaRetornarEmpty() {
        // Arrange
        when(zonaRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Zona> resultado = zonaRepositoryAdapter.findById(1L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(zonaRepository).findById(1L);
    }

    @Test
    void findAll_DeberiaRetornarTodasLasZonas() {
        // Arrange
        List<ZonaEntity> zonaEntities = Arrays.asList(zonaEntity);
        when(zonaRepository.findAll()).thenReturn(zonaEntities);

        // Act
        List<Zona> resultado = zonaRepositoryAdapter.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(zona.getId(), resultado.get(0).getId());
        assertEquals(zona.getCorregimiento(), resultado.get(0).getCorregimiento());
        assertEquals(zona.getMunicipio(), resultado.get(0).getMunicipio());
        assertEquals(zona.getCiudad(), resultado.get(0).getCiudad());
        assertEquals(zona.getDepartamento(), resultado.get(0).getDepartamento());
        assertEquals(zona.getPais(), resultado.get(0).getPais());
        verify(zonaRepository).findAll();
    }

    @Test
    void deleteById_DeberiaEliminarZonaPorId() {
        // Arrange
        doNothing().when(zonaRepository).deleteById(anyLong());

        // Act
        zonaRepositoryAdapter.deleteById(1L);

        // Assert
        verify(zonaRepository).deleteById(1L);
    }

    @Test
    void existsById_CuandoExisteZona_DeberiaRetornarTrue() {
        // Arrange
        when(zonaRepository.existsById(anyLong())).thenReturn(true);

        // Act
        boolean resultado = zonaRepositoryAdapter.existsById(1L);

        // Assert
        assertTrue(resultado);
        verify(zonaRepository).existsById(1L);
    }

    @Test
    void existsById_CuandoNoExisteZona_DeberiaRetornarFalse() {
        // Arrange
        when(zonaRepository.existsById(anyLong())).thenReturn(false);

        // Act
        boolean resultado = zonaRepositoryAdapter.existsById(1L);

        // Assert
        assertFalse(resultado);
        verify(zonaRepository).existsById(1L);
    }
}