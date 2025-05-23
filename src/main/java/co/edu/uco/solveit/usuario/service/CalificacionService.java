package co.edu.uco.solveit.usuario.service;



import co.edu.uco.solveit.usuario.entity.Calificacion;
import co.edu.uco.solveit.usuario.repository.CalificacionRepository;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CalificacionService {
    private final CalificacionRepository calificacionRepository;
    private final UsuarioRepository usuarioRepository;
    public Double obtenerCalificacion(String email){

        Long userId = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(USUARIO_NO_ENCONTRADO)).getId();

        List<Calificacion> calificaciones = calificacionRepository.findByUsuarioId(userId);
        return calificaciones.stream()
                .mapToDouble(Calificacion::getValor)
                .average()
                .orElse(0.0);
    }
}
