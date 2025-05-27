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

    // Mensajes de Validación
    public static final String USERNAME_REQUERIDO = "El username no puede ser nulo o vacío";
    public static final String PASSWORD_REQUERIDO = "La contraseña no puede ser nula o vacía";
    public static final String EMAIL_REQUERIDO = "El email no puede ser nulo o vacío";
    public static final String NOMBRE_COMPLETO_REQUERIDO = "El nombre completo no puede ser nulo o vacío";
    public static final String NUMERO_IDENTIFICACION_REQUERIDO = "El número de identificación no puede ser nulo o vacío";
    public static final String TIPO_IDENTIFICACION_REQUERIDO = "El tipo de identificación no puede ser nulo o vacío";
    public static final String TELEFONO_REQUERIDO = "El teléfono no puede ser nulo o vacío";
    public static final String TOKEN_REQUERIDO = "El token no puede ser nulo o vacío";
    public static final String NUEVA_PASSWORD_REQUERIDA = "La nueva contraseña no puede ser nula o vacía";
    public static final String PASSWORD_ACTUAL_REQUERIDA = "La contraseña actual no puede ser nula";
    public static final String ID_REQUERIDO = "El id no puede ser nulo";
    public static final String CALIFICACION_MINIMA = "La calificación mínima es 1";
    public static final String CALIFICACION_MAXIMA = "La calificación máxima es 5";

    // Mensajes de Validación para Publicaciones
    public static final String TITULO_REQUERIDO = "El título no puede ser nulo o vacío";
    public static final String DESCRIPCION_REQUERIDA = "La descripción no puede ser nula o vacía";
    public static final String TIPO_PUBLICACION_REQUERIDO = "El tipo de publicación no puede ser nulo";
    public static final String CATEGORIA_SERVICIO_REQUERIDA = "La categoría de servicio no puede ser nula o vacía";
    public static final String ZONA_ID_REQUERIDO = "El id de la zona no puede ser nulo";
    public static final String PUBLICACION_ID_REQUERIDO = "El id de la publicación no puede ser nulo";
    public static final String MOTIVO_REQUERIDO = "El motivo no puede ser nulo o vacío";

    // Mensajes de Validación para Zonas
    public static final String CORREGIMIENTO_REQUERIDO = "El corregimiento no puede ser nulo o vacío";
    public static final String MUNICIPIO_REQUERIDO = "El municipio no puede ser nulo o vacío";
    public static final String CIUDAD_REQUERIDA = "La ciudad no puede ser nula o vacía";
    public static final String DEPARTAMENTO_REQUERIDO = "El departamento no puede ser nulo o vacío";
    public static final String PAIS_REQUERIDO = "El país no puede ser nulo o vacío";

    // Mensajes de Validación para Pólizas
    public static final String NUMERO_POLIZA_REQUERIDO = "El número de póliza no puede ser nulo o vacío";
    public static final String NOMBRE_ASEGURADORA_REQUERIDO = "El nombre de la aseguradora no puede ser nulo o vacío";
    public static final String PRIMA_REQUERIDA = "La prima no puede ser nula";
    public static final String FECHA_VENCIMIENTO_REQUERIDA = "La fecha de vencimiento no puede ser nula";
    public static final String TIPO_POLIZA_REQUERIDO = "El tipo de póliza no puede ser nulo o vacío";
}
