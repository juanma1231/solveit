package co.edu.uco.solveit.usuario.service;

import co.edu.uco.solveit.usuario.dto.ActualizarUsuarioRequest;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import co.edu.uco.solveit.usuario.dto.ResetPasswordRequest;
import co.edu.uco.solveit.usuario.dto.SolicitudResetPasswordRequest;
import co.edu.uco.solveit.usuario.entity.Usuario;
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

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    //TOdo Aquí se inyectar un servicio de email para enviar correos de recuperación

    public MessageResponse actualizarDatosUsuario(ActualizarUsuarioRequest request) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (request.nombreCompleto() != null && !request.nombreCompleto().isEmpty()) {
            usuario.setNombreCompleto(request.nombreCompleto());
        }

        if (request.email() != null && !request.email().isEmpty() && !request.email().equals(usuario.getEmail())) {

            if (usuarioRepository.existsByEmail(request.email())) {
                throw new RuntimeException("El email ya está registrado por otro usuario");
            }
            usuario.setEmail(request.email());
        }

        if (request.currentPassword() != null && !request.currentPassword().isEmpty()
                && request.newPassword() != null && !request.newPassword().isEmpty()) {
            
            // Verificar contraseña actual
            if (!passwordEncoder.matches(request.currentPassword(), usuario.getPassword())) {
                throw new RuntimeException("La contraseña actual es incorrecta");
            }

            usuario.setPassword(passwordEncoder.encode(request.newPassword()));
        }

        usuarioRepository.save(usuario);

        return MessageResponse.builder()
                .message("Datos actualizados correctamente")
                .success(true)
                .build();
    }

    public MessageResponse solicitarResetPassword(SolicitudResetPasswordRequest request) {
        // Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("No existe un usuario con ese email"));

        // Generar token de recuperación
        String token = UUID.randomUUID().toString();
        usuario.setTokenRecuperacion(token);
        usuario.setExpiracionTokenRecuperacion(LocalDateTime.now().plusHours(1)); // Token válido por 1 hora

        // Guardar token
        usuarioRepository.save(usuario);

        // Aquí se enviaría un email con el token de recuperación
        // emailService.enviarEmailRecuperacion(usuario.getEmail(), token);

        return MessageResponse.builder()
                .message("Se ha enviado un correo con instrucciones para restablecer tu contraseña")
                .success(true)
                .build();
    }

    public MessageResponse resetPassword(ResetPasswordRequest request) {

        Usuario usuario = usuarioRepository.findByTokenRecuperacion(request.token())
                .orElseThrow(() -> new RuntimeException("Token inválido o expirado"));

        // Verificar que el token no haya expirado
        if (usuario.getExpiracionTokenRecuperacion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("El token ha expirado");
        }

        usuario.setPassword(passwordEncoder.encode(request.newPassword()));

        usuario.setTokenRecuperacion(null);
        usuario.setExpiracionTokenRecuperacion(null);

        usuarioRepository.save(usuario);

        return MessageResponse.builder()
                .message("Contraseña actualizada correctamente")
                .success(true)
                .build();
    }
}