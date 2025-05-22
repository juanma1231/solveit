package co.edu.uco.solveit.publicacion.infrastructure.controller;

import co.edu.uco.solveit.publicacion.domain.model.TipoPublicacion;
import co.edu.uco.solveit.publicacion.domain.port.in.PublicacionUseCase;
import co.edu.uco.solveit.publicacion.application.dto.ActualizarPublicacionRequest;
import co.edu.uco.solveit.publicacion.application.dto.CrearPublicacionRequest;
import co.edu.uco.solveit.publicacion.application.dto.PublicacionResponse;
import co.edu.uco.solveit.publicacion.application.dto.ReportarPublicacionRequest;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/publicaciones")
@RequiredArgsConstructor
public class PublicacionController {

    private final PublicacionUseCase publicacionUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublicacionResponse crearPublicacion(@RequestBody CrearPublicacionRequest request) {
        return publicacionUseCase.crearPublicacion(request);
    }

    @PutMapping("/{id}")
    public PublicacionResponse actualizarPublicacion(
            @PathVariable Long id,
            @RequestBody ActualizarPublicacionRequest request) {
        return publicacionUseCase.actualizarPublicacion(id, request);
    }

    @GetMapping("/{id}")
    public PublicacionResponse obtenerPublicacion(@PathVariable Long id) {
        return publicacionUseCase.obtenerPublicacion(id);
    }

    @GetMapping
    public List<PublicacionResponse> listarPublicaciones(
            @RequestParam(required = false) TipoPublicacion tipoPublicacion) {
        return publicacionUseCase.listarPublicaciones(tipoPublicacion);
    }

    @GetMapping("/mis-publicaciones")
    public List<PublicacionResponse> listarMisPublicaciones() {
        return publicacionUseCase.listarMisPublicaciones();
    }

    @PostMapping("/{id}/cancelar")
    public MessageResponse cancelarPublicacion(@PathVariable Long id) {
        return publicacionUseCase.cancelarPublicacion(id);
    }

    @PostMapping("/{id}/reportar")
    public MessageResponse reportarPublicacion(
            @PathVariable Long id,
            @RequestBody ReportarPublicacionRequest request) {
        return publicacionUseCase.reportarPublicacion(id, request);
    }

}
