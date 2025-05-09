package co.edu.uco.solveit.publicacion.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class Reporte {
    private Long id;
    private Long publicacionId;
    private Publicacion publicacion;
    private Long usuarioId;
    private String nombreUsuario;
    private String motivo;
    private LocalDateTime fechaReporte;
    private boolean procesado;
}