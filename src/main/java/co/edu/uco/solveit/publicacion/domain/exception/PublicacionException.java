package co.edu.uco.solveit.publicacion.domain.exception;

public class PublicacionException extends RuntimeException {
    
    public PublicacionException(String message) {
        super(message);
    }
    
    public PublicacionException(String message, Throwable cause) {
        super(message, cause);
    }
}