package co.edu.uco.solveit.publicacion.domain.port.in;

import co.edu.uco.solveit.publicacion.application.dto.CrearInteresRequest;
import co.edu.uco.solveit.publicacion.application.dto.InteresResponse;
import co.edu.uco.solveit.usuario.dto.MessageResponse;

import java.util.List;

public interface InteresUseCase {
    /**
     * Expresses interest in a publication
     * @param request The request containing the publication ID
     * @return The created interest
     */
    InteresResponse mostraInteres(CrearInteresRequest request);
    
    /**
     * Lists interests for a publication
     * @param publicacionId The publication ID
     * @return The list of interests
     */
    List<InteresResponse> listarInteresesPorPublicacion(Long publicacionId);
    
    /**
     * Lists interests for the current user's publications
     * @return The list of interests
     */
    List<InteresResponse> listarInteresesEnMisPublicaciones();
    
    /**
     * Lists interests expressed by the current user
     * @return The list of interests
     */
    List<InteresResponse> listarMisIntereses();
    
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
}