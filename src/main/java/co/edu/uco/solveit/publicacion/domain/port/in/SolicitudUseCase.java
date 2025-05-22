package co.edu.uco.solveit.publicacion.domain.port.in;

import co.edu.uco.solveit.publicacion.application.dto.CrearSolicitudRequest;
import co.edu.uco.solveit.publicacion.application.dto.SolicitudResponse;
import co.edu.uco.solveit.usuario.dto.MessageResponse;

import java.util.List;

public interface SolicitudUseCase {
    /**
     * Expresses interest in a publication
     * @param request The request containing the publication ID
     * @return The created interest
     */
    SolicitudResponse mostraSolicitud(CrearSolicitudRequest request);

    /**
     * Lists interests for a publication
     * @param publicacionId The publication ID
     * @return The list of interests
     */
    List<SolicitudResponse> listarSolicitudPorPublicacion(Long publicacionId);

    /**
     * Lists interests for the current user's publications
     * @return The list of interests
     */
    List<SolicitudResponse> listarSolicitudEnMisPublicaciones();

    /**
     * Lists interests expressed by the current user
     * @return The list of interests
     */
    List<SolicitudResponse> listarMisSolicitud();

    /**
     * Accepts an interest
     * @param interesId The interest ID
     * @return A message response
     */
    MessageResponse aceptarSolicitud(Long interesId);

    /**
     * Rejects an interest
     * @param interesId The interest ID
     * @return A message response
     */
    MessageResponse rechazarSolicitud(Long interesId);

    /**
     * Finalizes an interest that has been accepted
     * @param interesId The interest ID
     * @return A message response
     */
    MessageResponse finalizarSolicitud(Long interesId);

    /**
     * Gets a solicitud by its ID
     * @param solicitudId The solicitud ID
     * @return The solicitud response
     */
    SolicitudResponse obtenerSolicitudPorId(Long solicitudId);
}
