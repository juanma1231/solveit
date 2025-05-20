package co.edu.uco.solveit.usuario.service;


import co.edu.uco.solveit.usuario.entity.Calificacion;
import co.edu.uco.solveit.usuario.repository.CalificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalificacionService {
    private final CalificacionRepository calificacionRepository;
    public List<Calificacion> buscarCalificacionesPorUsuarioId(Long usuarioId){
        return calificacionRepository.findByUsuarioId(usuarioId);
    }
}
