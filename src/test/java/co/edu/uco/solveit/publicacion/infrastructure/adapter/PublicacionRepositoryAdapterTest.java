package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.model.EstadoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ZonaEntity;
import co.edu.uco.solveit.publicacion.infrastructure.repository.PublicacionRepository;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicacionRepositoryAdapterTest {

    @Mock
    private PublicacionRepository publicacionRepository;

    @Mock
    private UsuarioApi usuarioApi;

    @InjectMocks
    private PublicacionRepositoryAdapter publicacionRepositoryAdapter;

    private PublicacionEntity publicacionEntity;
    private Publicacion publicacion;
    private Usuario usuario;
    private ZonaEntity zonaEntity;
    private Zona zona;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");
        usuario.setNombreCompleto("Test User");

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

        publicacionEntity = PublicacionEntity.builder()
                .id(1L)
                .titulo("Título Test")
                .descripcion("Descripción Test")
                .usuario(usuario)
                .tipoPublicacion(TipoPublicacion.OFERTA)
                .categoriaServicio("Categoría Test")
                .zonaEntity(zonaEntity)
                .estado(EstadoPublicacion.PUBLICADA)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        publicacion = Publicacion.builder()
                .id(1L)
                .titulo("Título Test")
                .descripcion("Descripción Test")
                .usuarioId(1L)
                .nombreUsuario("Test User")
                .tipoPublicacion(TipoPublicacion.OFERTA)
                .categoriaServicio("Categoría Test")
                .zonaId(1L)
                .zona(zona)
                .estado(EstadoPublicacion.PUBLICADA)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

    @Test
    void save_DeberiaGuardarPublicacionCorrectamente() {
        // Arrange
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(publicacionRepository.save(any(PublicacionEntity.class))).thenReturn(publicacionEntity);

        // Act
        Publicacion resultado = publicacionRepositoryAdapter.save(publicacion);

        // Assert
        assertNotNull(resultado);
        assertEquals(publicacion.getId(), resultado.getId());
        assertEquals(publicacion.getTitulo(), resultado.getTitulo());
        assertEquals(publicacion.getDescripcion(), resultado.getDescripcion());
        assertEquals(publicacion.getUsuarioId(), resultado.getUsuarioId());
        assertEquals(publicacion.getNombreUsuario(), resultado.getNombreUsuario());
        assertEquals(publicacion.getTipoPublicacion(), resultado.getTipoPublicacion());
        assertEquals(publicacion.getCategoriaServicio(), resultado.getCategoriaServicio());
        assertEquals(publicacion.getZonaId(), resultado.getZonaId());
        assertEquals(publicacion.getEstado(), resultado.getEstado());
        verify(publicacionRepository).save(any(PublicacionEntity.class));
    }

    @Test
    void findById_CuandoExistePublicacion_DeberiaRetornarPublicacion() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.of(publicacionEntity));

        // Act
        Optional<Publicacion> resultado = publicacionRepositoryAdapter.findById(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(publicacion.getId(), resultado.get().getId());
        assertEquals(publicacion.getTitulo(), resultado.get().getTitulo());
        assertEquals(publicacion.getDescripcion(), resultado.get().getDescripcion());
        assertEquals(publicacion.getUsuarioId(), resultado.get().getUsuarioId());
        assertEquals(publicacion.getNombreUsuario(), resultado.get().getNombreUsuario());
        assertEquals(publicacion.getTipoPublicacion(), resultado.get().getTipoPublicacion());
        assertEquals(publicacion.getCategoriaServicio(), resultado.get().getCategoriaServicio());
        assertEquals(publicacion.getZonaId(), resultado.get().getZonaId());
        assertEquals(publicacion.getEstado(), resultado.get().getEstado());
        verify(publicacionRepository).findById(1L);
    }

    @Test
    void findById_CuandoNoExistePublicacion_DeberiaRetornarEmpty() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Publicacion> resultado = publicacionRepositoryAdapter.findById(1L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(publicacionRepository).findById(1L);
    }

    @Test
    void findAll_DeberiaRetornarTodasLasPublicaciones() {
        // Arrange
        List<PublicacionEntity> publicacionEntities = Arrays.asList(publicacionEntity);
        when(publicacionRepository.findAll()).thenReturn(publicacionEntities);

        // Act
        List<Publicacion> resultado = publicacionRepositoryAdapter.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(publicacion.getId(), resultado.get(0).getId());
        assertEquals(publicacion.getTitulo(), resultado.get(0).getTitulo());
        assertEquals(publicacion.getDescripcion(), resultado.get(0).getDescripcion());
        assertEquals(publicacion.getUsuarioId(), resultado.get(0).getUsuarioId());
        assertEquals(publicacion.getNombreUsuario(), resultado.get(0).getNombreUsuario());
        assertEquals(publicacion.getTipoPublicacion(), resultado.get(0).getTipoPublicacion());
        assertEquals(publicacion.getCategoriaServicio(), resultado.get(0).getCategoriaServicio());
        assertEquals(publicacion.getZonaId(), resultado.get(0).getZonaId());
        assertEquals(publicacion.getEstado(), resultado.get(0).getEstado());
        verify(publicacionRepository).findAll();
    }

    @Test
    void findByUsuarioId_DeberiaRetornarPublicacionesDelUsuario() {
        // Arrange
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        List<PublicacionEntity> publicacionEntities = Arrays.asList(publicacionEntity);
        when(publicacionRepository.findByUsuario(any(Usuario.class))).thenReturn(publicacionEntities);

        // Act
        List<Publicacion> resultado = publicacionRepositoryAdapter.findByUsuarioId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(publicacion.getId(), resultado.get(0).getId());
        assertEquals(publicacion.getTitulo(), resultado.get(0).getTitulo());
        verify(publicacionRepository).findByUsuario(usuario);
    }

    @Test
    void findByTipoPublicacion_DeberiaRetornarPublicacionesPorTipo() {
        // Arrange
        List<PublicacionEntity> publicacionEntities = Arrays.asList(publicacionEntity);
        when(publicacionRepository.findByTipoPublicacion(any(TipoPublicacion.class))).thenReturn(publicacionEntities);

        // Act
        List<Publicacion> resultado = publicacionRepositoryAdapter.findByTipoPublicacion(TipoPublicacion.OFERTA);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(publicacion.getId(), resultado.get(0).getId());
        assertEquals(publicacion.getTitulo(), resultado.get(0).getTitulo());
        assertEquals(TipoPublicacion.OFERTA, resultado.get(0).getTipoPublicacion());
        verify(publicacionRepository).findByTipoPublicacion(TipoPublicacion.OFERTA);
    }

    @Test
    void findByEstado_DeberiaRetornarPublicacionesPorEstado() {
        // Arrange
        List<PublicacionEntity> publicacionEntities = Arrays.asList(publicacionEntity);
        when(publicacionRepository.findByEstado(any(EstadoPublicacion.class))).thenReturn(publicacionEntities);

        // Act
        List<Publicacion> resultado = publicacionRepositoryAdapter.findByEstado(EstadoPublicacion.PUBLICADA);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(publicacion.getId(), resultado.get(0).getId());
        assertEquals(publicacion.getTitulo(), resultado.get(0).getTitulo());
        assertEquals(EstadoPublicacion.PUBLICADA, resultado.get(0).getEstado());
        verify(publicacionRepository).findByEstado(EstadoPublicacion.PUBLICADA);
    }

    @Test
    void findByUsuarioIdAndEstado_DeberiaRetornarPublicacionesPorUsuarioYEstado() {
        // Arrange
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        List<PublicacionEntity> publicacionEntities = Arrays.asList(publicacionEntity);
        when(publicacionRepository.findByUsuarioAndEstado(any(Usuario.class), any(EstadoPublicacion.class))).thenReturn(publicacionEntities);

        // Act
        List<Publicacion> resultado = publicacionRepositoryAdapter.findByUsuarioIdAndEstado(1L, EstadoPublicacion.PUBLICADA);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(publicacion.getId(), resultado.get(0).getId());
        assertEquals(publicacion.getTitulo(), resultado.get(0).getTitulo());
        assertEquals(publicacion.getUsuarioId(), resultado.get(0).getUsuarioId());
        assertEquals(EstadoPublicacion.PUBLICADA, resultado.get(0).getEstado());
        verify(publicacionRepository).findByUsuarioAndEstado(usuario, EstadoPublicacion.PUBLICADA);
    }

    @Test
    void findByTipoPublicacionAndEstado_DeberiaRetornarPublicacionesPorTipoYEstado() {
        // Arrange
        List<PublicacionEntity> publicacionEntities = Arrays.asList(publicacionEntity);
        when(publicacionRepository.findByTipoPublicacionAndEstado(any(TipoPublicacion.class), any(EstadoPublicacion.class))).thenReturn(publicacionEntities);

        // Act
        List<Publicacion> resultado = publicacionRepositoryAdapter.findByTipoPublicacionAndEstado(TipoPublicacion.OFERTA, EstadoPublicacion.PUBLICADA);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(publicacion.getId(), resultado.get(0).getId());
        assertEquals(publicacion.getTitulo(), resultado.get(0).getTitulo());
        assertEquals(TipoPublicacion.OFERTA, resultado.get(0).getTipoPublicacion());
        assertEquals(EstadoPublicacion.PUBLICADA, resultado.get(0).getEstado());
        verify(publicacionRepository).findByTipoPublicacionAndEstado(TipoPublicacion.OFERTA, EstadoPublicacion.PUBLICADA);
    }

    @Test
    void deleteById_DeberiaEliminarPublicacionPorId() {
        // Arrange
        doNothing().when(publicacionRepository).deleteById(anyLong());

        // Act
        publicacionRepositoryAdapter.deleteById(1L);

        // Assert
        verify(publicacionRepository).deleteById(1L);
    }

    @Test
    void existsById_CuandoExistePublicacion_DeberiaRetornarTrue() {
        // Arrange
        when(publicacionRepository.existsById(anyLong())).thenReturn(true);

        // Act
        boolean resultado = publicacionRepositoryAdapter.existsById(1L);

        // Assert
        assertTrue(resultado);
        verify(publicacionRepository).existsById(1L);
    }

    @Test
    void existsById_CuandoNoExistePublicacion_DeberiaRetornarFalse() {
        // Arrange
        when(publicacionRepository.existsById(anyLong())).thenReturn(false);

        // Act
        boolean resultado = publicacionRepositoryAdapter.existsById(1L);

        // Assert
        assertFalse(resultado);
        verify(publicacionRepository).existsById(1L);
    }
}