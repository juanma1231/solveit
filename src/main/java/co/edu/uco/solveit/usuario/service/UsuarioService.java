package co.edu.uco.solveit.usuario.service;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import co.edu.uco.solveit.usuario.dto.*;
import co.edu.uco.solveit.usuario.entity.Calificacion;
import co.edu.uco.solveit.usuario.entity.Usuario;
import co.edu.uco.solveit.usuario.exception.UsuarioException;
import co.edu.uco.solveit.usuario.repository.CalificacionRepository;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    public static final String USUARIO_NO_ENCONTRADO = CatalogoDeMensajes.USUARIO_NO_ENCONTRADO;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CalificacionRepository calificacionRepository;

    public MessageResponse actualizarDatosUsuario(ActualizarUsuarioRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USUARIO_NO_ENCONTRADO));

        if (request.nombreCompleto() != null && !request.nombreCompleto().isEmpty()) {
            usuario.setNombreCompleto(request.nombreCompleto());
        }

        if (request.numeroIdentificacion() != null && !request.numeroIdentificacion().isEmpty()) {
            usuario.setNumeroIdentificacion(request.numeroIdentificacion());
        }

        if (request.tipoIdentificacion() != null && !request.tipoIdentificacion().isEmpty()) {
            usuario.setTipoIdentificacion(request.tipoIdentificacion());
        }

        if (request.descripcionPerfil() != null && !request.descripcionPerfil().isEmpty()) {
            usuario.setDescripcionPerfil(request.descripcionPerfil());
        }

        if (request.telefono() != null && !request.telefono().isEmpty()) {
            usuario.setTelefono(request.telefono());
        }

        if (request.currentPassword() != null && !request.currentPassword().isEmpty()
                && request.newPassword() != null && !request.newPassword().isEmpty()) {

            if (!passwordEncoder.matches(request.currentPassword(), usuario.getPassword())) {
                throw new UsuarioException(CatalogoDeMensajes.CONTRASENA_INCORRECTA);
            }

            usuario.setPassword(passwordEncoder.encode(request.newPassword()));
        }

        usuarioRepository.save(usuario);

        return MessageResponse.builder()
                .message(CatalogoDeMensajes.DATOS_ACTUALIZADOS_CORRECTAMENTE)
                .success(true)
                .build();
    }

    public MessageResponse solicitarResetPassword(SolicitudResetPasswordRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException(CatalogoDeMensajes.USUARIO_EMAIL_NO_EXISTE));

        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(token);
        usuario.setExpiracionTokenRecuperacion(LocalDateTime.now().plusHours(1)); // Token válido por 1 hora

        usuarioRepository.save(usuario);

        return MessageResponse.builder()
                .message(CatalogoDeMensajes.CORREO_INSTRUCCIONES_ENVIADO)
                .success(true)
                .build();
    }

    public MessageResponse resetPassword(ResetPasswordRequest request) {

        Usuario usuario = usuarioRepository.findByTokenRecuperacion(request.token())
                .orElseThrow(() -> new UsuarioException(CatalogoDeMensajes.TOKEN_INVALIDO_EXPIRADO));

        // Verificar que el token no haya expirado
        if (usuario.getExpiracionTokenRecuperacion().isBefore(LocalDateTime.now())) {
            throw new UsuarioException(CatalogoDeMensajes.TOKEN_EXPIRADO);
        }

        usuario.setPassword(passwordEncoder.encode(request.newPassword()));

        usuario.setTokenRecuperacion(null);
        usuario.setExpiracionTokenRecuperacion(null);

        usuarioRepository.save(usuario);

        return MessageResponse.builder()
                .message(CatalogoDeMensajes.CONTRASENA_ACTUALIZADA_CORRECTAMENTE)
                .success(true)
                .build();
    }

    public MessageResponse calificarUsuario(CalificarUsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(request.id())
                .orElseThrow(() -> new RuntimeException(CatalogoDeMensajes.USUARIO_EMAIL_NO_EXISTE));

        Calificacion calificacion = Calificacion.builder()
                .usuario(usuario)
                .valor(request.calificacion())
                .build();
        calificacionRepository.save(calificacion);

        return MessageResponse.builder()
                .message(CatalogoDeMensajes.USUARIO_CALIFICADO_EXITO)
                .success(true)
                .build();
    }
}
