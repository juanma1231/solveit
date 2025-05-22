package co.edu.uco.solveit.publicacion.infrastructure.controller;

import co.edu.uco.solveit.publicacion.application.dto.CrearInteresRequest;
import co.edu.uco.solveit.publicacion.application.dto.SolicitudResponse;
import co.edu.uco.solveit.publicacion.domain.port.in.SolicitudUseCase;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitud")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudUseCase solicitudUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudResponse mostraInteres(@RequestBody CrearInteresRequest request) {
        return solicitudUseCase.mostraInteres(request);
    }

    @GetMapping("/publicacion/{publicacionId}")
    public List<SolicitudResponse> listarInteresesPorPublicacion(@PathVariable Long publicacionId) {
        return solicitudUseCase.listarInteresesPorPublicacion(publicacionId);
    }

    @GetMapping("/mis-publicaciones")
    public List<SolicitudResponse> listarInteresesEnMisPublicaciones() {
        return solicitudUseCase.listarInteresesEnMisPublicaciones();
    }

    @GetMapping("/mis-solicitud")
    public List<SolicitudResponse> listarMisIntereses() {
        return solicitudUseCase.listarMisIntereses();
    }

    @PostMapping("/{interesId}/aceptar")
    public MessageResponse aceptarInteres(@PathVariable Long interesId) {
        return solicitudUseCase.aceptarInteres(interesId);
    }

    @PostMapping("/{interesId}/rechazar")
    public MessageResponse rechazarInteres(@PathVariable Long interesId) {
        return solicitudUseCase.rechazarInteres(interesId);
    }

    @PostMapping("/{interesId}/finalizar")
    public MessageResponse finalizarInteres(@PathVariable Long interesId) {
        return solicitudUseCase.finalizarInteres(interesId);
    }
}
