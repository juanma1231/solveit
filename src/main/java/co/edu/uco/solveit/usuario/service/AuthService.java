package co.edu.uco.solveit.usuario.service;

import co.edu.uco.solveit.usuario.dto.AuthResponse;
import co.edu.uco.solveit.usuario.dto.LoginRequest;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import co.edu.uco.solveit.usuario.dto.RegistroRequest;
import co.edu.uco.solveit.usuario.entity.Role;
import co.edu.uco.solveit.usuario.entity.Usuario;
import co.edu.uco.solveit.usuario.exception.AuthenticationException;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import co.edu.uco.solveit.usuario.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static co.edu.uco.solveit.usuario.service.UsuarioService.USUARIO_NO_ENCONTRADO;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegistroRequest request) {

        if (usuarioRepository.existsByUsername(request.username())) {
            throw new AuthenticationException("El nombre de usuario ya está en uso");
        }

        if (usuarioRepository.existsByEmail(request.email())) {
            throw new AuthenticationException("El email ya está registrado");
        }

        var usuario = Usuario.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .nombreCompleto(request.nombreCompleto())
                .numeroIdentificacion(request.numeroIdentificacion())
                .tipoIdentificacion(request.tipoIdentificacion())
                .descripcionPerfil(request.descripcionPerfil())
                .telefono(request.telefono())
                .role(Role.USER)
                .build();

        usuario = usuarioRepository.save(usuario);

        var jwtToken = jwtService.generateToken(usuario);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .nombreCompleto(usuario.getNombreCompleto())
                .numeroIdentificacion(usuario.getNumeroIdentificacion())
                .tipoIdentificacion(usuario.getTipoIdentificacion())
                .descripcionPerfil(usuario.getDescripcionPerfil())
                .telefono(usuario.getTelefono())
                .build();
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException(USUARIO_NO_ENCONTRADO));

        usuario.setUltimoLogin(java.time.LocalDateTime.now());
        usuario = usuarioRepository.save(usuario);

        var jwtToken = jwtService.generateToken(usuario);


        return AuthResponse.builder()
                .token(jwtToken)
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .nombreCompleto(usuario.getNombreCompleto())
                .numeroIdentificacion(usuario.getNumeroIdentificacion())
                .tipoIdentificacion(usuario.getTipoIdentificacion())
                .descripcionPerfil(usuario.getDescripcionPerfil())
                .telefono(usuario.getTelefono())
                .build();
    }

    public MessageResponse logout() {
        return MessageResponse.builder()
                .message("Sesión cerrada correctamente")
                .success(true)
                .build();
    }
}
