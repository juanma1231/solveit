package co.edu.uco.solveit.usuario.controller;

import co.edu.uco.solveit.usuario.dto.*;
import co.edu.uco.solveit.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PutMapping("/perfil")
    public MessageResponse actualizarPerfil(@RequestBody ActualizarUsuarioRequest request) {
        return usuarioService.actualizarDatosUsuario(request);
    }

    @PostMapping("/reset-password/solicitar")
    public MessageResponse solicitarResetPassword(@RequestBody SolicitudResetPasswordRequest request) {
        return usuarioService.solicitarResetPassword(request);
    }

    @PostMapping("/reset-password/confirmar")
    public MessageResponse resetPassword(@RequestBody ResetPasswordRequest request) {
        return usuarioService.resetPassword(request);
    }
    @PostMapping("/calificar")
    public MessageResponse calificarPublicacion(
            @RequestBody CalificarUsuarioRequest request
    ){
        return usuarioService.calificarUsuario(request);
    }
}