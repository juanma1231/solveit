package co.edu.uco.solveit.publicacion.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "zonas")
public class Zona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String corregimiento;

    @Column(nullable = false)
    private String municipio;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String departamento;

    @Column(nullable = false)
    private String pais;
}