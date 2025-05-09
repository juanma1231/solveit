package co.edu.uco.solveit.usuario;

import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.usuario.entity.Usuario;

import java.util.Optional;

/**
 * Port for user authentication operations
 */
public interface UsuarioApi {
    
    /**
     * Gets the ID of the currently authenticated user
     * @return the user ID
     * @throws PublicacionException if no user is authenticated or the user cannot be found
     */
    Long getCurrentUserId() throws PublicacionException;
    
    /**
     * Gets the username of the currently authenticated user
     * @return the username
     * @throws PublicacionException if no user is authenticated
     */
    String getCurrentUsername() throws PublicacionException;
    
    /**
     * Gets the full name of the currently authenticated user
     * @return the full name
     * @throws PublicacionException if no user is authenticated or the user cannot be found
     */
    String getCurrentUserFullName() throws PublicacionException;

    Optional<Usuario> findById(Long usuarioId);
}