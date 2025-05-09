package co.edu.uco.solveit.publicacion.domain.port.in;

import co.edu.uco.solveit.publicacion.application.dto.ZonaRequest;
import co.edu.uco.solveit.publicacion.application.dto.ZonaResponse;

import java.util.List;

public interface ZonaUseCase {
    ZonaResponse crearZona(ZonaRequest request);
    ZonaResponse actualizarZona(Long id, ZonaRequest request);
    ZonaResponse obtenerZona(Long id);
    List<ZonaResponse> listarZonas();
    void eliminarZona(Long id);
}