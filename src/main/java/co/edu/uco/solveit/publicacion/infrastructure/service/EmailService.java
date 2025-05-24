package co.edu.uco.solveit.publicacion.infrastructure.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    /**
     * Sends a notification email to the publication owner about a new interest
     * 
     * @param to Email address of the recipient
     * @param publicacionTitulo Title of the publication
     * @param usuarioInteresadoNombre Name of the interested user
     */
    public void enviarNotificacionNuevaSolicitud(String to, String publicacionTitulo, String usuarioInteresadoNombre) {
        String subject = "Nueva Solicitud en tu publicación: " + publicacionTitulo;
        String body = String.format("""
                Hola,

                El usuario %s  en tu publicación "%s".

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
    public void enviarNotificacionSolicitudRechazada(String to, String publicacionTitulo) {
        String subject = "Actualización sobre tu solicitud en: " + publicacionTitulo;
        String body = String.format("""
                Hola,

                Lamentamos informarte que tu solicitud en la publicación "%s" ha sido rechazado por el propietario.

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

        log.info("Enviando email a: {}", to);
        log.info("Asunto: {}", subject);
        log.info("Cuerpo: {}", body);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("juanmagamexzuluaga@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            javaMailSender.send(message);
            log.info("Email enviado correctamente");
        } catch (Exception e) {
            log.error("Error al enviar el email: {}", e.getMessage(), e);
        }
    }
}
