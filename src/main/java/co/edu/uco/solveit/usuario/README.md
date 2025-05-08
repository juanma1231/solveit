# Módulo de Usuarios - UCO SolveIT

Este módulo proporciona funcionalidades de gestión de usuarios para la aplicación UCO SolveIT, incluyendo registro, autenticación, gestión de tokens, edición de datos personales y restablecimiento de contraseñas.

## Características

- Registro de usuarios
- Autenticación (Login) con JWT
- Gestión de tokens
- Cierre de sesión
- Edición de datos personales
- Restablecimiento de contraseña

## Implementación Técnica

El módulo de usuarios está implementado utilizando:

- Spring Security para la autenticación y autorización
- JWT (JSON Web Tokens) para la gestión de tokens
- Spring Data JPA para la persistencia de datos
- BCrypt para el cifrado de contraseñas

## Endpoints de la API

### Autenticación

- `POST /api/v1/auth/register` - Registrar un nuevo usuario
- `POST /api/v1/auth/login` - Iniciar sesión
- `POST /api/v1/auth/logout` - Cerrar sesión

### Gestión de Usuarios

- `PUT /api/v1/usuarios/perfil` - Actualizar datos del perfil
- `POST /api/v1/usuarios/reset-password/solicitar` - Solicitar restablecimiento de contraseña
- `POST /api/v1/usuarios/reset-password/confirmar` - Confirmar restablecimiento de contraseña

## Formato de Solicitudes y Respuestas

### Registro de Usuario

**Solicitud:**
```json
{
  "username": "usuario",
  "password": "contraseña",
  "email": "usuario@ejemplo.com",
  "nombreCompleto": "Nombre Completo"
}
```

**Respuesta:**
```json
{
  "token": "jwt-token",
  "username": "usuario",
  "email": "usuario@ejemplo.com",
  "nombreCompleto": "Nombre Completo"
}
```

### Inicio de Sesión

**Solicitud:**
```json
{
  "username": "usuario",
  "password": "contraseña"
}
```

**Respuesta:**
```json
{
  "token": "jwt-token",
  "username": "usuario",
  "email": "usuario@ejemplo.com",
  "nombreCompleto": "Nombre Completo"
}
```

### Actualización de Perfil

**Solicitud:**
```json
{
  "nombreCompleto": "Nuevo Nombre",
  "email": "nuevo@ejemplo.com",
  "currentPassword": "contraseña-actual",
  "newPassword": "nueva-contraseña"
}
```

**Respuesta:**
```json
{
  "message": "Datos actualizados correctamente",
  "success": true
}
```

### Solicitud de Restablecimiento de Contraseña

**Solicitud:**
```json
{
  "email": "usuario@ejemplo.com"
}
```

**Respuesta:**
```json
{
  "message": "Se ha enviado un correo con instrucciones para restablecer tu contraseña",
  "success": true
}
```

### Confirmación de Restablecimiento de Contraseña

**Solicitud:**
```json
{
  "token": "token-de-recuperación",
  "newPassword": "nueva-contraseña"
}
```

**Respuesta:**
```json
{
  "message": "Contraseña actualizada correctamente",
  "success": true
}
```

## Seguridad

- Todas las contraseñas se almacenan cifradas utilizando BCrypt
- La autenticación se realiza mediante tokens JWT
- Los tokens JWT tienen una validez de 24 horas
- Las rutas protegidas requieren un token JWT válido en el encabezado de autorización

## Integración con Frontend

El frontend debe:

1. Almacenar el token JWT recibido tras el inicio de sesión
2. Incluir el token en el encabezado de autorización de todas las solicitudes a rutas protegidas
3. Implementar la lógica para cerrar sesión eliminando el token almacenado

### Ejemplo de Uso del Token

```javascript
// Incluir el token en las solicitudes
fetch('http://api-url/api/v1/usuarios/perfil', {
  method: 'PUT',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${token}`
  },
  body: JSON.stringify(data)
})
```

## Mejoras Futuras

Posibles mejoras futuras podrían incluir:

- Autenticación de dos factores
- Integración con proveedores de identidad externos (OAuth)
- Gestión de roles y permisos más avanzada
- Auditoría de acciones de usuarios