package co.edu.uco.solveit.publicacion.infrastructure.adapter;

import co.edu.uco.solveit.publicacion.domain.model.Reporte;
import co.edu.uco.solveit.publicacion.domain.port.out.ReporteRepositoryPort;
import co.edu.uco.solveit.publicacion.infrastructure.entity.ReporteEntity;
import co.edu.uco.solveit.publicacion.infrastructure.mapper.ReporteMapper;
import co.edu.uco.solveit.publicacion.infrastructure.repository.ReporteRepository;
import co.edu.uco.solveit.publicacion.infrastructure.mapper.PublicacionMapper;
import co.edu.uco.solveit.usuario.UsuarioApi;
import co.edu.uco.solveit.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class ReporteRepositoryAdapter implements ReporteRepositoryPort {

    private final ReporteRepository reporteRepository;
    private final UsuarioApi usuarioApi;

    @Override
    public Reporte save(Reporte reporte) {
        ReporteEntity entity = ReporteMapper.toEntity(reporte);

        if (reporte.getPublicacionId() != null) {
            entity.setPublicacion(PublicacionMapper.toEntity(reporte.getPublicacion()));
        }

        if (reporte.getUsuarioId() != null) {
            usuarioApi.findById(reporte.getUsuarioId())
                    .ifPresent(entity::setUsuario);
        }

        ReporteEntity savedEntity = reporteRepository.save(entity);
        return ReporteMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Reporte> findById(Long id) {
        return reporteRepository.findById(id)
                .map(ReporteMapper::toDomain);
    }

    @Override
    public List<Reporte> findByPublicacionId(Long publicacionId) {
        return reporteRepository.findByPublicacion_Id(publicacionId).stream()
                .map(ReporteMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reporte> findByUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioApi.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return reporteRepository.findByUsuario(usuario).stream()
                .map(ReporteMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reporte> findByProcesado(boolean procesado) {
        return reporteRepository.findByProcesado(procesado).stream()
                .map(ReporteMapper::toDomain)
                .toList();
    }

    @Override
    public long countByPublicacionId(Long publicacionId) {
        return reporteRepository.countByPublicacion_Id(publicacionId);
    }

    @Override
    public boolean existsByPublicacionIdAndUsuarioId(Long publicacionId, Long usuarioId) {
        return reporteRepository.existsByPublicacion_IdAndUsuario_Id( publicacionId,usuarioId);
    }
}