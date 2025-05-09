package co.edu.uco.solveit.publicacion.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Publicacion {
    private Long id;
    private String titulo;
    private String descripcion;
    private Long usuarioId;
    private String nombreUsuario;
    private TipoPublicacion tipoPublicacion;
    private String categoriaServicio;
    private Long zonaId;
    private Zona zona;
    private EstadoPublicacion estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}