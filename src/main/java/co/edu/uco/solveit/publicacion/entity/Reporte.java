package co.edu.uco.solveit.publicacion.entity;

import co.edu.uco.solveit.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 1000)
    private String motivo;

    @Column(name = "fecha_reporte")
    private LocalDateTime fechaReporte;

    @Column(name = "estado_procesado")
    private boolean procesado;

    @PrePersist
    protected void onCreate() {
        fechaReporte = LocalDateTime.now();
        procesado = false;
    }
}