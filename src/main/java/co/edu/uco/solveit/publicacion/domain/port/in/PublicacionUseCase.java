package co.edu.uco.solveit.publicacion.domain.port.in;

import co.edu.uco.solveit.publicacion.dto.ActualizarPublicacionRequest;
import co.edu.uco.solveit.publicacion.dto.CrearPublicacionRequest;
import co.edu.uco.solveit.publicacion.dto.PublicacionResponse;
import co.edu.uco.solveit.publicacion.dto.ReportarPublicacionRequest;
import co.edu.uco.solveit.publicacion.entity.TipoPublicacion;
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