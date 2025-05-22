package co.edu.uco.solveit.publicacion.domain.port.in;

import co.edu.uco.solveit.publicacion.application.dto.CrearInteresRequest;
import co.edu.uco.solveit.publicacion.application.dto.SolicitudResponse;
import co.edu.uco.solveit.usuario.dto.MessageResponse;

import java.util.List;

public interface SolicitudUseCase {
    /**
     * Expresses interest in a publication
     * @param request The request containing the publication ID
     * @return The created interest
     */
    SolicitudResponse mostraInteres(CrearInteresRequest request);

    /**
     * Lists interests for a publication
     * @param publicacionId The publication ID
     * @return The list of interests
     */
    List<SolicitudResponse> listarInteresesPorPublicacion(Long publicacionId);

    /**
     * Lists interests for the current user's publications
     * @return The list of interests
     */
    List<SolicitudResponse> listarInteresesEnMisPublicaciones();

    /**
     * Lists interests expressed by the current user
     * @return The list of interests
     */
    List<SolicitudResponse> listarMisIntereses();

    /**
     * Accepts an interest
     * @param interesId The interest ID
     * @return A message response
     */
    MessageResponse aceptarInteres(Long interesId);

    /**
     * Rejects an interest
     * @param interesId The interest ID
     * @return A message response
     */
    MessageResponse rechazarInteres(Long interesId);

    /**
     * Finalizes an interest that has been accepted
     * @param interesId The interest ID
     * @return A message response
     */
    MessageResponse finalizarInteres(Long interesId);
}
