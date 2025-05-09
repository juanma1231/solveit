package co.edu.uco.solveit.publicacion.infrastructure.controller;

import co.edu.uco.solveit.publicacion.domain.port.in.ZonaUseCase;
import co.edu.uco.solveit.publicacion.application.dto.ZonaRequest;
import co.edu.uco.solveit.publicacion.application.dto.ZonaResponse;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/zonas")
@RequiredArgsConstructor
public class ZonaController {

    private final ZonaUseCase zonaUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ZonaResponse crearZona(@RequestBody ZonaRequest request) {
        return zonaUseCase.crearZona(request);
    }

    @PutMapping("/{id}")
    public ZonaResponse actualizarZona(@PathVariable Long id, @RequestBody ZonaRequest request) {
        return zonaUseCase.actualizarZona(id, request);
    }

    @GetMapping("/{id}")
    public ZonaResponse obtenerZona(@PathVariable Long id) {
        return zonaUseCase.obtenerZona(id);
    }

    @GetMapping
    public List<ZonaResponse> listarZonas() {
        return zonaUseCase.listarZonas();
    }

    @DeleteMapping("/{id}")
    public MessageResponse eliminarZona(@PathVariable Long id) {
        zonaUseCase.eliminarZona(id);
        return MessageResponse.builder()
                .message("Zona eliminada correctamente")
                .success(true)
                .build();
    }
}