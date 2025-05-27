package co.edu.uco.solveit.common;

public class CatalogoDeMensajes {

    private CatalogoDeMensajes() {

    }

    // Mensajes comunes
    public static final String PUBLICACION_NO_ENCONTRADA = "Publicación no encontrada";
    public static final String SOLICITUD_NO_ENCONTRADA = "Solicitud no encontrada";
    public static final String USUARIO_INTERESADO_NO_ENCONTRADO = "Usuario interesado no encontrado";
    public static final String REPORTE_NO_ENCONTRADO = "Reporte no encontrado";
    public static final String ZONA_NO_ENCONTRADA = "Zona no encontrada";
    public static final String USUARIO_NO_ENCONTRADO = "Usuario no encontrado";

    // Mensajes de PublicacionService
    public static final String SIN_PERMISO_ACTUALIZAR_PUBLICACION = "No tienes permiso para actualizar esta publicación";
    public static final String PUBLICACION_CANCELADA_BLOQUEADA = "No se puede actualizar una publicación cancelada o bloqueada";
    public static final String PUBLICACION_CON_INTERESES_VIGENTES = "No se puede actualizar una publicación que tiene intereses vigentes";
    public static final String SIN_PERMISO_CANCELAR_PUBLICACION = "No tienes permiso para cancelar esta publicación";
    public static final String PUBLICACION_YA_CANCELADA = "La publicación ya está cancelada";
    public static final String NO_CANCELAR_CON_INTERESES_VIGENTES = "No se puede cancelar una publicación que tiene intereses vigentes";
    public static final String PUBLICACION_CANCELADA_CORRECTAMENTE = "Publicación cancelada correctamente";
    public static final String YA_REPORTADO_PUBLICACION = "Ya has reportado esta publicación anteriormente";
    public static final String PUBLICACION_REPORTADA_CORRECTAMENTE = "Publicación reportada correctamente";

    // Mensajes de ReporteService
    public static final String PUBLICACION_NO_REPORTADA = "La publicación no está reportada";
    public static final String REPORTES_CANCELADOS_PUBLICACION_HABILITADA = "Reportes cancelados y publicación habilitada correctamente";
    public static final String PUBLICACION_YA_HABILITADA = "La publicación ya está habilitada";
    public static final String PUBLICACION_HABILITADA_CORRECTAMENTE = "Publicación habilitada correctamente";
    public static final String PUBLICACION_BLOQUEADA_PERMANENTEMENTE = "Publicación bloqueada permanentemente";

    // Mensajes de SolicitudService
    public static final String NO_INTERES_PROPIA_PUBLICACION = "No puedes mostrar interés en tu propia publicación";
    public static final String PUBLICACION_NO_PUBLICADA = "La publicación no está publicada";
    public static final String YA_MOSTRADO_INTERES = "Ya has mostrado interés en esta publicación";
    public static final String USUARIO_PROPIETARIO_NO_ENCONTRADO = "Usuario propietario no encontrado";
    public static final String SIN_PERMISO_VER_INTERESES = "No tienes permiso para ver los intereses de esta publicación";
    public static final String SIN_PERMISO_ACEPTAR_INTERES = "No tienes permiso para aceptar este interés";
    public static final String INTERES_YA_PROCESADO = "Este interés ya ha sido procesado";
    public static final String INTERES_ACEPTADO_CORRECTAMENTE = "Interés aceptado correctamente. Se ha habilitado el chat con el usuario interesado.";
    public static final String SIN_PERMISO_RECHAZAR_INTERES = "No tienes permiso para rechazar este interés";
    public static final String INTERES_RECHAZADO_CORRECTAMENTE = "Interés rechazado correctamente. Se ha notificado al usuario interesado.";
    public static final String SIN_PERMISO_FINALIZAR_INTERES = "No tienes permiso para finalizar este interés";
    public static final String SOLO_FINALIZAR_INTERESES_ACEPTADOS = "Solo se pueden finalizar intereses que estén en estado aceptado";
    public static final String INTERES_FINALIZADO_CORRECTAMENTE = "Interés finalizado correctamente. Se ha notificado al usuario interesado.";

    // Mensajes de AuthService
    public static final String USERNAME_YA_EN_USO = "El nombre de usuario ya está en uso";
    public static final String EMAIL_YA_REGISTRADO = "El email ya está registrado";
    public static final String SESION_CERRADA_CORRECTAMENTE = "Sesión cerrada correctamente";

    // Mensajes de UsuarioService
    public static final String CONTRASENA_INCORRECTA = "La contraseña actual es incorrecta";
    public static final String DATOS_ACTUALIZADOS_CORRECTAMENTE = "Datos actualizados correctamente";
    public static final String USUARIO_EMAIL_NO_EXISTE = "No existe un usuario con ese email";
    public static final String CORREO_INSTRUCCIONES_ENVIADO = "Se ha enviado un correo con instrucciones para restablecer tu contraseña";
    public static final String TOKEN_INVALIDO_EXPIRADO = "Token inválido o expirado";
    public static final String TOKEN_EXPIRADO = "El token ha expirado";
    public static final String CONTRASENA_ACTUALIZADA_CORRECTAMENTE = "Contraseña actualizada correctamente";
    public static final String USUARIO_CALIFICADO_EXITO = "Usuario calificado con exito";

    // Mensajes de ChatController
    public static final String CONECTADO_CHAT_EXITO = "Conectado con exito a la sala de chat";

    // Mensajes de PolizaService
    public static final String POLIZA_NO_ENCONTRADA = "Póliza no encontrada";
    public static final String ERROR_ALMACENAR_ARCHIVO = "No se pudo almacenar el archivo ";
    public static final String SIN_PERMISO_ACTUALIZAR_POLIZA = "No tienes permiso para actualizar esta póliza";
    public static final String SIN_PERMISO_VER_POLIZAS = "No tienes permiso para ver las pólizas de este usuario";
    public static final String SIN_PERMISO_DESCARGAR_POLIZA = "No tienes permiso para descargar esta póliza";
    public static final String POLIZA_SIN_ARCHIVO = "Esta póliza no tiene un archivo adjunto";
    public static final String ERROR_OBTENER_ARCHIVO = "Error al obtener el archivo";
    public static final String SIN_PERMISO_ELIMINAR_POLIZA = "No tienes permiso para eliminar esta póliza";
    public static final String POLIZA_ELIMINADA_CORRECTAMENTE = "Póliza eliminada correctamente";
}
