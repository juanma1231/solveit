package co.edu.uco.solveit.publicacion.infrastructure.controller;

import co.edu.uco.solveit.publicacion.application.dto.CrearInteresRequest;
import co.edu.uco.solveit.publicacion.application.dto.InteresResponse;
import co.edu.uco.solveit.publicacion.domain.port.in.InteresUseCase;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/intereses")
@RequiredArgsConstructor
public class InteresController {

    private final InteresUseCase interesUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InteresResponse mostraInteres(@RequestBody CrearInteresRequest request) {
        return interesUseCase.mostraInteres(request);
    }

    @GetMapping("/publicacion/{publicacionId}")
    public List<InteresResponse> listarInteresesPorPublicacion(@PathVariable Long publicacionId) {
        return interesUseCase.listarInteresesPorPublicacion(publicacionId);
    }

    @GetMapping("/mis-publicaciones")
    public List<InteresResponse> listarInteresesEnMisPublicaciones() {
        return interesUseCase.listarInteresesEnMisPublicaciones();
    }

    @GetMapping("/mis-intereses")
    public List<InteresResponse> listarMisIntereses() {
        return interesUseCase.listarMisIntereses();
    }

    @PostMapping("/{interesId}/aceptar")
    public MessageResponse aceptarInteres(@PathVariable Long interesId) {
        return interesUseCase.aceptarInteres(interesId);
    }

    @PostMapping("/{interesId}/rechazar")
    public MessageResponse rechazarInteres(@PathVariable Long interesId) {
        return interesUseCase.rechazarInteres(interesId);
    }
}