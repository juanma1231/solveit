package co.edu.uco.solveit.usuario.controller;



import co.edu.uco.solveit.usuario.service.CalificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {
    private final CalificacionService calificacionService;
    @GetMapping
    public Double obtenerCalificacionPublicacion(
            @RequestParam(required = true) Long userId) {
        return  calificacionService.obtenerCalificacion(userId);
    }



}
