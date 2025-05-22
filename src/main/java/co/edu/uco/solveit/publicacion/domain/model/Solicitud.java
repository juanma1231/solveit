package co.edu.uco.solveit.publicacion.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Solicitud {
    private Long id;
    private Long publicacionId;
    private Publicacion publicacion;
    private Long usuarioInteresadoId;
    private String nombreUsuarioInteresado;
    private EstadoInteres estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}