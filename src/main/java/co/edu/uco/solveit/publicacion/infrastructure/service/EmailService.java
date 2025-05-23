package co.edu.uco.solveit.publicacion.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    /**
     * Sends a notification email to the publication owner about a new interest
     * 
     * @param to Email address of the recipient
     * @param publicacionTitulo Title of the publication
     * @param usuarioInteresadoNombre Name of the interested user
     */
    public void enviarNotificacionNuevoInteres(String to, String publicacionTitulo, String usuarioInteresadoNombre) {
        String subject = "Nuevo interés en tu publicación: " + publicacionTitulo;
        String body = String.format("""
                Hola,

                El usuario %s ha mostrado interés en tu publicación "%s".

                Puedes revisar los detalles en la plataforma.

                Saludos,
                El equipo de SolveIT""",
                usuarioInteresadoNombre, publicacionTitulo);
        
        enviarEmail(to, subject, body);
    }
    
    /**
     * Sends a notification email to the interested user about the rejection of their interest
     * 
     * @param to Email address of the recipient
     * @param publicacionTitulo Title of the publication
     */
    public void enviarNotificacionInteresRechazado(String to, String publicacionTitulo) {
        String subject = "Actualización sobre tu interés en: " + publicacionTitulo;
        String body = String.format("""
                Hola,

                Lamentamos informarte que tu interés en la publicación "%s" ha sido rechazado por el propietario.

                Te animamos a explorar otras publicaciones en la plataforma.

                Saludos,
                El equipo de SolveIT""",
                publicacionTitulo);
        
        enviarEmail(to, subject, body);
    }
    
    /**
     * Sends an email
     * 
     * @param to Email address of the recipient
     * @param subject Subject of the email
     * @param body Body of the email
     */
    private void enviarEmail(String to, String subject, String body) {
        // This is a mock implementation. In a real application, this would use JavaMailSender
        log.info("Enviando email a: {}", to);
        log.info("Asunto: {}", subject);
        log.info("Cuerpo: {}", body);
        
        // In a real implementation, this would be:
        // SimpleMailMessage message = new SimpleMailMessage();
        // message.setTo(to);
        // message.setSubject(subject);
        // message.setText(body);
        // javaMailSender.send(message);
    }
}