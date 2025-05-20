package co.edu.uco.solveit.publicacion.infrastructure.entity;

import co.edu.uco.solveit.publicacion.domain.model.EstadoInteres;
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
@Table(name = "intereses")
public class InteresEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_interesado_id", nullable = false)
    private Usuario usuarioInteresado;

    @Column(name = "nombre_usuario_interesado", nullable = false)
    private String nombreUsuarioInteresado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInteres estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (estado == null) {
            estado = EstadoInteres.PENDIENTE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}