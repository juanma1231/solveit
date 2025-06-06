package co.edu.uco.solveit.publicacion.application.service;

import co.edu.uco.solveit.common.CatalogoDeMensajes;
import co.edu.uco.solveit.publicacion.domain.exception.PublicacionException;
import co.edu.uco.solveit.publicacion.domain.model.Zona;
import co.edu.uco.solveit.publicacion.domain.port.in.ZonaUseCase;
import co.edu.uco.solveit.publicacion.domain.port.out.ZonaRepositoryPort;
import co.edu.uco.solveit.publicacion.application.dto.ZonaRequest;
import co.edu.uco.solveit.publicacion.application.dto.ZonaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ZonaService implements ZonaUseCase {

    private final ZonaRepositoryPort zonaRepositoryPort;

    @Override
    public ZonaResponse crearZona(ZonaRequest request) {
        Zona zona = Zona.builder()
                .corregimiento(request.corregimiento())
                .municipio(request.municipio())
                .ciudad(request.ciudad())
                .departamento(request.departamento())
                .pais(request.pais())
                .build();

        Zona zonaSaved = zonaRepositoryPort.save(zona);
        return mapToZonaResponse(zonaSaved);
    }

    @Override
    public ZonaResponse actualizarZona(Long id, ZonaRequest request) {
        Zona zona = zonaRepositoryPort.findById(id)
                .orElseThrow(() -> new PublicacionException(CatalogoDeMensajes.ZONA_NO_ENCONTRADA));

        zona.setCorregimiento(request.corregimiento());
        zona.setMunicipio(request.municipio());
        zona.setCiudad(request.ciudad());
        zona.setDepartamento(request.departamento());
        zona.setPais(request.pais());

        Zona zonaActualizada = zonaRepositoryPort.save(zona);
        return mapToZonaResponse(zonaActualizada);
    }

    @Override
    public ZonaResponse obtenerZona(Long id) {
        Zona zona = zonaRepositoryPort.findById(id)
                .orElseThrow(() -> new PublicacionException(CatalogoDeMensajes.ZONA_NO_ENCONTRADA));
        return mapToZonaResponse(zona);
    }

    @Override
    public List<ZonaResponse> listarZonas() {
        return zonaRepositoryPort.findAll().stream()
                .map(this::mapToZonaResponse)
                .toList();
    }

    @Override
    public void eliminarZona(Long id) {
        if (!zonaRepositoryPort.existsById(id)) {
            throw new PublicacionException(CatalogoDeMensajes.ZONA_NO_ENCONTRADA);
        }
        zonaRepositoryPort.deleteById(id);
    }

    private ZonaResponse mapToZonaResponse(Zona zona) {
        return ZonaResponse.builder()
                .id(zona.getId())
                .corregimiento(zona.getCorregimiento())
                .municipio(zona.getMunicipio())
                .ciudad(zona.getCiudad())
                .departamento(zona.getDepartamento())
                .pais(zona.getPais())
                .build();
    }
}
