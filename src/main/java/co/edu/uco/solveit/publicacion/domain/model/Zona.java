package co.edu.uco.solveit.publicacion.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Zona {
    private Long id;
    private String corregimiento;
    private String municipio;
    private String ciudad;
    private String departamento;
    private String pais;
}