package co.edu.uco.solveit.usuario.service;



import co.edu.uco.solveit.usuario.entity.Calificacion;
import co.edu.uco.solveit.usuario.repository.CalificacionRepository;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static co.edu.uco.solveit.usuario.service.UsuarioService.USUARIO_NO_ENCONTRADO;


@Service
@RequiredArgsConstructor
public class CalificacionService {
    private final CalificacionRepository calificacionRepository;
    private final UsuarioRepository usuarioRepository;
    public Double obtenerCalificacion(Long usuarioId){

        List<Calificacion> calificaciones = calificacionRepository.findByUsuarioId(usuarioId);
        return calificaciones.stream()
                .mapToDouble(Calificacion::getValor)
                .average()
                .orElse(0.0);
    }
}
