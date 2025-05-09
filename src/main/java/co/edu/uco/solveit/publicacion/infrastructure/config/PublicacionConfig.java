package co.edu.uco.solveit.publicacion.infrastructure.config;

import co.edu.uco.solveit.publicacion.application.service.PublicacionService;
import co.edu.uco.solveit.publicacion.application.service.ZonaService;
import co.edu.uco.solveit.publicacion.domain.port.in.PublicacionUseCase;
import co.edu.uco.solveit.publicacion.domain.port.in.ZonaUseCase;
import co.edu.uco.solveit.publicacion.domain.port.out.PublicacionRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.ReporteRepositoryPort;
import co.edu.uco.solveit.publicacion.domain.port.out.ZonaRepositoryPort;
import co.edu.uco.solveit.publicacion.infrastructure.adapter.PublicacionRepositoryAdapter;
import co.edu.uco.solveit.publicacion.infrastructure.adapter.ReporteRepositoryAdapter;
import co.edu.uco.solveit.publicacion.infrastructure.adapter.ZonaRepositoryAdapter;
import co.edu.uco.solveit.publicacion.infrastructure.repository.PublicacionRepository;
import co.edu.uco.solveit.publicacion.infrastructure.repository.ReporteRepository;
import co.edu.uco.solveit.publicacion.infrastructure.repository.ZonaRepository;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PublicacionConfig {

    @Bean
    public ZonaRepositoryPort zonaRepositoryPort(ZonaRepository zonaRepository) {
        return new ZonaRepositoryAdapter(zonaRepository);
    }

    @Bean
    public ZonaUseCase zonaUseCase(ZonaRepositoryPort zonaRepositoryPort) {
        return new ZonaService(zonaRepositoryPort);
    }

    @Bean
    public PublicacionRepositoryPort publicacionRepositoryPort(PublicacionRepository publicacionRepository) {
        return new PublicacionRepositoryAdapter(publicacionRepository);
    }

    @Bean
    public ReporteRepositoryPort reporteRepositoryPort(ReporteRepository reporteRepository) {
        return new ReporteRepositoryAdapter(reporteRepository);
    }

    @Bean
    public PublicacionUseCase publicacionUseCase(
            PublicacionRepositoryPort publicacionRepositoryPort,
            ZonaRepositoryPort zonaRepositoryPort,
            ReporteRepositoryPort reporteRepositoryPort,
            UsuarioRepository usuarioRepository) {
        return new PublicacionService(
                publicacionRepositoryPort,
                zonaRepositoryPort,
                reporteRepositoryPort,
                usuarioRepository);
    }
}
