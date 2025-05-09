package co.edu.uco.solveit.publicacion.domain.port.in;

import co.edu.uco.solveit.publicacion.application.dto.ActualizarPublicacionRequest;
import co.edu.uco.solveit.publicacion.application.dto.CrearPublicacionRequest;
import co.edu.uco.solveit.publicacion.application.dto.PublicacionResponse;
import co.edu.uco.solveit.publicacion.application.dto.ReportarPublicacionRequest;
import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.usuario.dto.MessageResponse;

import java.util.List;

public interface PublicacionUseCase {
    PublicacionResponse crearPublicacion(CrearPublicacionRequest request);
    PublicacionResponse actualizarPublicacion(Long id, ActualizarPublicacionRequest request);
    PublicacionResponse obtenerPublicacion(Long id);
    List<PublicacionResponse> listarPublicaciones(TipoPublicacion tipoPublicacion);
    List<PublicacionResponse> listarMisPublicaciones();
    MessageResponse cancelarPublicacion(Long id);
    MessageResponse reportarPublicacion(Long id, ReportarPublicacionRequest request);
}