package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
import co.edu.uco.solveit.publicacion.domain.model.Publicacion;
import co.edu.uco.solveit.publicacion.domain.model.Solicitud;
import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.infrastructure.entity.PublicacionEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.SolicitudEntity;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ZonaEntity;
import co.edu.uco.solveit.publicacion.infrastructure.repository.PublicacionRepository;
import co.edu.uco.solveit.publicacion.infrastructure.repository.SolicitudRepository;
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
class SolicitudRepositoryAdapterTest {

    @Mock
    private SolicitudRepository solicitudRepository;

    @Mock
    private PublicacionRepository publicacionRepository;

    @Mock
    private UsuarioApi usuarioApi;

    @InjectMocks
    private SolicitudRepositoryAdapter solicitudRepositoryAdapter;

    private SolicitudEntity solicitudEntity;
    private Solicitud solicitud;
    private PublicacionEntity publicacionEntity;
    private Usuario usuario;
    private ZonaEntity zonaEntity;

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

        usuario = Usuario.builder()
                .id(1L)
                .nombreCompleto("Usuario Test")
                .build();

        publicacionEntity = PublicacionEntity.builder()
                .id(1L)
                .titulo("Publicación Test")
                .descripcion("Descripción Test")
                .usuario(usuario)
                .zonaEntity(zonaEntity)
                .build();

        solicitudEntity = SolicitudEntity.builder()
                .id(1L)
                .publicacion(publicacionEntity)
                .usuarioQueSolicita(usuario)
                .nombreUsuarioQueSolicita("Usuario Test")
                .titulo("Solicitud Test")
                .descripcion("Descripción Test")
                .estado(EstadoInteres.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();

        Zona zona = Zona.builder()
                .id(1L)
                .corregimiento("Corregimiento Test")
                .municipio("Municipio Test")
                .ciudad("Ciudad Test")
                .departamento("Departamento Test")
                .pais("País Test")
                .build();

        Publicacion publicacion = Publicacion.builder()
                .id(1L)
                .titulo("Publicación Test")
                .descripcion("Descripción Test")
                .usuarioId(1L)
                .nombreUsuario("Usuario Test")
                .zonaId(1L)
                .zona(zona)
                .build();

        solicitud = Solicitud.builder()
                .id(1L)
                .publicacionId(1L)
                .publicacion(publicacion)
                .usuarioInteresadoId(1L)
                .nombreUsuarioInteresado("Usuario Test")
                .titulo("Solicitud Test")
                .descripcion("Descripción Test")
                .estado(EstadoInteres.PENDIENTE)
                .fechaCreacion(LocalDateTime.now())
                .fechaActualizacion(LocalDateTime.now())
                .build();
    }

    @Test
    void save_DeberiaGuardarSolicitudCorrectamente() {
        // Arrange
        when(solicitudRepository.save(any(SolicitudEntity.class))).thenReturn(solicitudEntity);

        // Act
        Solicitud resultado = solicitudRepositoryAdapter.save(solicitud);

        // Assert
        assertNotNull(resultado);
        assertEquals(solicitud.getId(), resultado.getId());
        assertEquals(solicitud.getTitulo(), resultado.getTitulo());
        assertEquals(solicitud.getDescripcion(), resultado.getDescripcion());
        assertEquals(solicitud.getEstado(), resultado.getEstado());
        verify(solicitudRepository).save(any(SolicitudEntity.class));
    }

    @Test
    void save_CuandoPublicacionNoEstaEstablecida_DeberiaEstablecerlaDesdeId() {
        // Arrange
        Solicitud solicitudSinPublicacion = Solicitud.builder()
                .id(1L)
                .publicacionId(1L)
                .usuarioInteresadoId(1L)
                .titulo("Solicitud Test")
                .descripcion("Descripción Test")
                .estado(EstadoInteres.PENDIENTE)
                .build();

        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.of(publicacionEntity));
        when(solicitudRepository.save(any(SolicitudEntity.class))).thenReturn(solicitudEntity);

        // Act
        Solicitud resultado = solicitudRepositoryAdapter.save(solicitudSinPublicacion);

        // Assert
        assertNotNull(resultado);
        verify(publicacionRepository).findById(1L);
        verify(solicitudRepository).save(any(SolicitudEntity.class));
    }

    @Test
    void save_CuandoUsuarioNoEstaEstablecido_DeberiaEstablecerloDesdeId() {
        // Arrange
        Solicitud solicitudSinUsuario = Solicitud.builder()
                .id(1L)
                .publicacionId(1L)
                .publicacion(solicitud.getPublicacion())
                .usuarioInteresadoId(1L)
                .titulo("Solicitud Test")
                .descripcion("Descripción Test")
                .estado(EstadoInteres.PENDIENTE)
                .build();

        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(solicitudRepository.save(any(SolicitudEntity.class))).thenReturn(solicitudEntity);

        // Act
        Solicitud resultado = solicitudRepositoryAdapter.save(solicitudSinUsuario);

        // Assert
        assertNotNull(resultado);
        verify(usuarioApi).findById(1L);
        verify(solicitudRepository).save(any(SolicitudEntity.class));
    }

    @Test
    void findById_CuandoExisteSolicitud_DeberiaRetornarSolicitud() {
        // Arrange
        when(solicitudRepository.findById(anyLong())).thenReturn(Optional.of(solicitudEntity));

        // Act
        Optional<Solicitud> resultado = solicitudRepositoryAdapter.findById(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(solicitud.getId(), resultado.get().getId());
        assertEquals(solicitud.getTitulo(), resultado.get().getTitulo());
        assertEquals(solicitud.getDescripcion(), resultado.get().getDescripcion());
        assertEquals(solicitud.getEstado(), resultado.get().getEstado());
        verify(solicitudRepository).findById(1L);
    }

    @Test
    void findById_CuandoNoExisteSolicitud_DeberiaRetornarEmpty() {
        // Arrange
        when(solicitudRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Solicitud> resultado = solicitudRepositoryAdapter.findById(1L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(solicitudRepository).findById(1L);
    }

    @Test
    void findByPublicacionId_DeberiaRetornarSolicitudesPorPublicacion() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.of(publicacionEntity));
        when(solicitudRepository.findByPublicacion(any(PublicacionEntity.class))).thenReturn(List.of(solicitudEntity));

        // Act
        List<Solicitud> resultado = solicitudRepositoryAdapter.findByPublicacionId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(solicitud.getId(), resultado.get(0).getId());
        assertEquals(solicitud.getTitulo(), resultado.get(0).getTitulo());
        verify(publicacionRepository).findById(1L);
        verify(solicitudRepository).findByPublicacion(publicacionEntity);
    }

    @Test
    void findByPublicacionIdAndEstado_DeberiaRetornarSolicitudesPorPublicacionYEstado() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.of(publicacionEntity));
        when(solicitudRepository.findByPublicacionAndEstado(any(PublicacionEntity.class), any(EstadoInteres.class)))
                .thenReturn(List.of(solicitudEntity));

        // Act
        List<Solicitud> resultado = solicitudRepositoryAdapter.findByPublicacionIdAndEstado(1L, EstadoInteres.PENDIENTE);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(solicitud.getId(), resultado.get(0).getId());
        assertEquals(solicitud.getEstado(), resultado.get(0).getEstado());
        verify(publicacionRepository).findById(1L);
        verify(solicitudRepository).findByPublicacionAndEstado(publicacionEntity, EstadoInteres.PENDIENTE);
    }

    @Test
    void findByPublicacionIdAndEstado_CuandoNoExistePublicacion_DeberiaRetornarListaVacia() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        List<Solicitud> resultado = solicitudRepositoryAdapter.findByPublicacionIdAndEstado(1L, EstadoInteres.PENDIENTE);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(publicacionRepository).findById(1L);
        verify(solicitudRepository, never()).findByPublicacionAndEstado(any(), any());
    }

    @Test
    void findByPublicacionIdAndEstadoIn_DeberiaRetornarSolicitudesPorPublicacionYEstados() {
        // Arrange
        List<EstadoInteres> estados = Arrays.asList(EstadoInteres.PENDIENTE, EstadoInteres.ACEPTADO);
        when(solicitudRepository.findByPublicacionIdAndEstadoIn(anyLong(), anyList()))
                .thenReturn(List.of(solicitudEntity));

        // Act
        List<Solicitud> resultado = solicitudRepositoryAdapter.findByPublicacionIdAndEstadoIn(1L, estados);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(solicitud.getId(), resultado.get(0).getId());
        verify(solicitudRepository).findByPublicacionIdAndEstadoIn(1L, estados);
    }

    @Test
    void findByUsuarioInteresadoId_DeberiaRetornarSolicitudesPorUsuario() {
        // Arrange
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(solicitudRepository.findByUsuarioQueSolicita(any(Usuario.class))).thenReturn(List.of(solicitudEntity));

        // Act
        List<Solicitud> resultado = solicitudRepositoryAdapter.findByUsuarioInteresadoId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(solicitud.getId(), resultado.get(0).getId());
        verify(usuarioApi).findById(1L);
        verify(solicitudRepository).findByUsuarioQueSolicita(usuario);
    }

    @Test
    void findByUsuarioInteresadoIdAndEstado_DeberiaRetornarSolicitudesPorUsuarioYEstado() {
        // Arrange
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(solicitudRepository.findByUsuarioQueSolicitaAndEstado(any(Usuario.class), any(EstadoInteres.class)))
                .thenReturn(List.of(solicitudEntity));

        // Act
        List<Solicitud> resultado = solicitudRepositoryAdapter.findByUsuarioInteresadoIdAndEstado(1L, EstadoInteres.PENDIENTE);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(solicitud.getId(), resultado.get(0).getId());
        assertEquals(solicitud.getEstado(), resultado.get(0).getEstado());
        verify(usuarioApi).findById(1L);
        verify(solicitudRepository).findByUsuarioQueSolicitaAndEstado(usuario, EstadoInteres.PENDIENTE);
    }

    @Test
    void findByUsuarioInteresadoIdAndEstado_CuandoNoExisteUsuario_DeberiaRetornarListaVacia() {
        // Arrange
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        List<Solicitud> resultado = solicitudRepositoryAdapter.findByUsuarioInteresadoIdAndEstado(1L, EstadoInteres.PENDIENTE);

        // Assert
        assertTrue(resultado.isEmpty());
        verify(usuarioApi).findById(1L);
        verify(solicitudRepository, never()).findByUsuarioQueSolicitaAndEstado(any(), any());
    }

    @Test
    void findByPublicacionIdAndUsuarioInteresadoId_DeberiaRetornarSolicitud() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.of(publicacionEntity));
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(solicitudRepository.findByPublicacionAndUsuarioQueSolicita(any(PublicacionEntity.class), any(Usuario.class)))
                .thenReturn(Optional.of(solicitudEntity));

        // Act
        Optional<Solicitud> resultado = solicitudRepositoryAdapter.findByPublicacionIdAndUsuarioInteresadoId(1L, 1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(solicitud.getId(), resultado.get().getId());
        verify(publicacionRepository).findById(1L);
        verify(usuarioApi).findById(1L);
        verify(solicitudRepository).findByPublicacionAndUsuarioQueSolicita(publicacionEntity, usuario);
    }

    @Test
    void findByPublicacionIdAndUsuarioInteresadoId_CuandoNoExistePublicacion_DeberiaRetornarEmpty() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));

        // Act
        Optional<Solicitud> resultado = solicitudRepositoryAdapter.findByPublicacionIdAndUsuarioInteresadoId(1L, 1L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(publicacionRepository).findById(1L);
        verify(usuarioApi).findById(1L);
        verify(solicitudRepository, never()).findByPublicacionAndUsuarioQueSolicita(any(), any());
    }

    @Test
    void findByPublicacionIdAndUsuarioInteresadoId_CuandoNoExisteUsuario_DeberiaRetornarEmpty() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.of(publicacionEntity));
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Solicitud> resultado = solicitudRepositoryAdapter.findByPublicacionIdAndUsuarioInteresadoId(1L, 1L);

        // Assert
        assertFalse(resultado.isPresent());
        verify(publicacionRepository).findById(1L);
        verify(usuarioApi).findById(1L);
        verify(solicitudRepository, never()).findByPublicacionAndUsuarioQueSolicita(any(), any());
    }

    @Test
    void existsByPublicacionIdAndUsuarioInteresadoId_CuandoExisteSolicitud_DeberiaRetornarTrue() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.of(publicacionEntity));
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));
        when(solicitudRepository.existsByPublicacionAndUsuarioQueSolicita(any(PublicacionEntity.class), any(Usuario.class)))
                .thenReturn(true);

        // Act
        boolean resultado = solicitudRepositoryAdapter.existsByPublicacionIdAndUsuarioInteresadoId(1L, 1L);

        // Assert
        assertTrue(resultado);
        verify(publicacionRepository).findById(1L);
        verify(usuarioApi).findById(1L);
        verify(solicitudRepository).existsByPublicacionAndUsuarioQueSolicita(publicacionEntity, usuario);
    }

    @Test
    void existsByPublicacionIdAndUsuarioInteresadoId_CuandoNoExistePublicacion_DeberiaRetornarFalse() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.of(usuario));

        // Act
        boolean resultado = solicitudRepositoryAdapter.existsByPublicacionIdAndUsuarioInteresadoId(1L, 1L);

        // Assert
        assertFalse(resultado);
        verify(publicacionRepository).findById(1L);
        verify(usuarioApi).findById(1L);
        verify(solicitudRepository, never()).existsByPublicacionAndUsuarioQueSolicita(any(), any());
    }

    @Test
    void existsByPublicacionIdAndUsuarioInteresadoId_CuandoNoExisteUsuario_DeberiaRetornarFalse() {
        // Arrange
        when(publicacionRepository.findById(anyLong())).thenReturn(Optional.of(publicacionEntity));
        when(usuarioApi.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        boolean resultado = solicitudRepositoryAdapter.existsByPublicacionIdAndUsuarioInteresadoId(1L, 1L);

        // Assert
        assertFalse(resultado);
        verify(publicacionRepository).findById(1L);
        verify(usuarioApi).findById(1L);
        verify(solicitudRepository, never()).existsByPublicacionAndUsuarioQueSolicita(any(), any());
    }

    @Test
    void deleteById_DeberiaEliminarSolicitudPorId() {
        // Arrange
        doNothing().when(solicitudRepository).deleteById(anyLong());

        // Act
        solicitudRepositoryAdapter.deleteById(1L);

        // Assert
        verify(solicitudRepository).deleteById(1L);
    }
}