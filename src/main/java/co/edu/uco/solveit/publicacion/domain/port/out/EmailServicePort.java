package co.edu.uco.solveit.publicacion.domain.port.out;


public interface EmailServicePort {

    void enviarNotificacionNuevaSolicitud(String to, String publicacionTitulo, String usuarioInteresadoNombre);

    void enviarNotificacionSolicitudRechazada(String to, String publicacionTitulo);

}
