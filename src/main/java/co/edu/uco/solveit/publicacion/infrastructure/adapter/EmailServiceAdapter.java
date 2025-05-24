package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.port.out.EmailServicePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceAdapter implements EmailServicePort {

    private final JavaMailSender javaMailSender;

    /**
     * Sends a notification email to the publication owner about a new interest
     * 
     * @param to Email address of the recipient
     * @param publicacionTitulo Title of the publication
     * @param usuarioInteresadoNombre Name of the interested user
     */
    @Override
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
    @Override
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


    @Override
    public void enviarNotificacionSolicitudAceptada(String to, String publicacionTitulo) {
        String subject = "¡Buenas noticias sobre tu solicitud en: " + publicacionTitulo;
        String body = String.format("""
                        Hola,
                        
                        Nos complace informarte que tu solicitud en la publicación "%s" ha sido aceptada por el propietario.
                        
                        Puedes proceder a contactar al propietario para coordinar los siguientes pasos.
                        
                        Saludos,
                        El equipo de SolveIT""",
                publicacionTitulo);

        enviarEmail(to, subject, body);
    }


    void enviarEmail(String to, String subject, String body) {

        EmailServiceAdapter.log.info("Enviando email a: {}", to);
        EmailServiceAdapter.log.info("Asunto: {}", subject);
        EmailServiceAdapter.log.info("Cuerpo: {}", body);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("juanmagamexzuluaga@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        try {
            javaMailSender.send(message);
            EmailServiceAdapter.log.info("Email enviado correctamente");
        } catch (Exception e) {
            EmailServiceAdapter.log.error("Error al enviar el email: {}", e.getMessage(), e);
        }
    }

}
