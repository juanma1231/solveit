package co.edu.uco.solveit.usuario.controller;

import co.edu.uco.solveit.usuario.dto.AuthResponse;
import co.edu.uco.solveit.usuario.dto.LoginRequest;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import co.edu.uco.solveit.usuario.dto.RegistroRequest;
import co.edu.uco.solveit.usuario.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegistroRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }

    @PostMapping("/logout")
    public MessageResponse logout() {
        return authService.logout();
    }
}