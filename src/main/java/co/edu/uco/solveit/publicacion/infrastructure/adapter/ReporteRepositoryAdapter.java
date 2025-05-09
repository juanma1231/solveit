package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.port.out.ReporteRepositoryPort;
import co.edu.uco.solveit.publicacion.entity.Publicacion;
import co.edu.uco.solveit.publicacion.entity.Reporte;
import co.edu.uco.solveit.publicacion.infrastructure.repository.ReporteRepository;
import co.edu.uco.solveit.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReporteRepositoryAdapter implements ReporteRepositoryPort {

    private final ReporteRepository reporteRepository;

    @Override
    public Reporte save(Reporte reporte) {
        return reporteRepository.save(reporte);
    }

    @Override
    public Optional<Reporte> findById(Long id) {
        return reporteRepository.findById(id);
    }

    @Override
    public List<Reporte> findByPublicacion(Publicacion publicacion) {
        return reporteRepository.findByPublicacion(publicacion);
    }

    @Override
    public List<Reporte> findByUsuario(Usuario usuario) {
        return reporteRepository.findByUsuario(usuario);
    }

    @Override
    public List<Reporte> findByProcesado(boolean procesado) {
        return reporteRepository.findByProcesado(procesado);
    }

    @Override
    public long countByPublicacion(Publicacion publicacion) {
        return reporteRepository.countByPublicacion(publicacion);
    }
}