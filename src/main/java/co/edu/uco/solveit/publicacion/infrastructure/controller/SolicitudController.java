package co.edu.uco.solveit.publicacion.infrastructure.controller;

import co.edu.uco.solveit.publicacion.application.dto.CrearSolicitudRequest;
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
    public SolicitudResponse mostraSolicitud(@RequestBody CrearSolicitudRequest request) {
        return solicitudUseCase.mostraSolicitud(request);
    }

    @GetMapping("/publicacion/{publicacionId}")
    public List<SolicitudResponse> listarSolicitudPorPublicacion(@PathVariable Long publicacionId) {
        return solicitudUseCase.listarSolicitudPorPublicacion(publicacionId);
    }

    @GetMapping("/mis-publicaciones")
    public List<SolicitudResponse> listarSolicitudEnMisPublicaciones() {
        return solicitudUseCase.listarSolicitudEnMisPublicaciones();
    }

    @GetMapping("/mis-solicitud")
    public List<SolicitudResponse> listarMisSolicitud() {
        return solicitudUseCase.listarMisSolicitud();
    }

    @PostMapping("/{solicitudId}/aceptar")
    public MessageResponse aceptarSolicitud(@PathVariable Long solicitudId) {
        return solicitudUseCase.aceptarSolicitud(solicitudId);
    }

    @PostMapping("/{solicitudId}/rechazar")
    public MessageResponse rechazarSolicitud(@PathVariable Long solicitudId) {
        return solicitudUseCase.rechazarSolicitud(solicitudId);
    }

    @PostMapping("/{solicitudId}/finalizar")
    public MessageResponse finalizarSolicitud(@PathVariable Long solicitudId) {
        return solicitudUseCase.finalizarSolicitud(solicitudId);
    }

    @GetMapping("/{solicitudId}")
    public SolicitudResponse obtenerSolicitudPorId(@PathVariable Long solicitudId) {
        return solicitudUseCase.obtenerSolicitudPorId(solicitudId);
    }
}
