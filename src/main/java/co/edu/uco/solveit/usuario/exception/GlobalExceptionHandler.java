package co.edu.uco.solveit.usuario.exception;

import co.edu.uco.solveit.poliza.exception.PolizaException;
import co.edu.uco.solveit.publicacion.exception.PublicacionException;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({RuntimeException.class, AuthenticationException.class})
    public ResponseEntity<MessageResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        MessageResponse errorResponse = MessageResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<MessageResponse> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        MessageResponse errorResponse = MessageResponse.builder()
                .message("Credenciales inválidas")
                .success(false)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<MessageResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        MessageResponse errorResponse = MessageResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PolizaException.class)
    public ResponseEntity<MessageResponse> handlePolizaException(PolizaException ex, WebRequest request) {
        MessageResponse errorResponse = MessageResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PublicacionException.class)
    public ResponseEntity<MessageResponse> handlePublicacionException(PublicacionException ex, WebRequest request) {
        MessageResponse errorResponse = MessageResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGlobalException(Exception ex, WebRequest request) {
        MessageResponse errorResponse = MessageResponse.builder()
                .message("Ha ocurrido un error inesperado")
                .success(false)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
