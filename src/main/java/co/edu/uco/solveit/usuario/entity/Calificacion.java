package co.edu.uco.solveit.usuario.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "calificacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario usuario;

    private Integer valor;
}