package co.edu.uco.solveit.poliza.exception;

public class PolizaException extends RuntimeException {
    
    public PolizaException(String message) {
        super(message);
    }
    
    public PolizaException(String message, Throwable cause) {
        super(message, cause);
    }
}