package co.edu.uco.solveit.poliza.service;

import co.edu.uco.solveit.poliza.dto.ActualizarPolizaRequest;
import co.edu.uco.solveit.poliza.dto.PolizaResponse;
import co.edu.uco.solveit.poliza.dto.RegistrarPolizaRequest;
import co.edu.uco.solveit.poliza.entity.Poliza;
import co.edu.uco.solveit.poliza.exception.PolizaException;
import co.edu.uco.solveit.poliza.repository.PolizaRepository;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import co.edu.uco.solveit.usuario.entity.Usuario;
import co.edu.uco.solveit.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class PolizaService {

    public static final String ADMIN = "ADMIN";
    public static final String POLIZA_NO_ENCONTRADA = "Póliza no encontrada";
    private final PolizaRepository polizaRepository;
    private final UsuarioRepository usuarioRepository;

    public PolizaResponse registrarPoliza(RegistrarPolizaRequest request, MultipartFile archivo) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Poliza poliza = Poliza.builder()
                .titular(usuario)
                .numeroPoliza(request.numeroPoliza())
                .nombreAseguradora(request.nombreAseguradora())
                .prima(request.prima())
                .fechaEmision(request.fechaEmision())
                .fechaVencimiento(request.fechaVencimiento())
                .tipoPoliza(request.tipoPoliza())
                .build();

        if (archivo != null && !archivo.isEmpty()) {
            try {
                poliza.setNombreArchivo(archivo.getOriginalFilename());
                poliza.setTipoArchivo(archivo.getContentType());
                poliza.setArchivoData(archivo.getBytes());
                poliza.setRutaArchivo(UUID.randomUUID().toString());
            } catch (IOException ex) {
                throw new PolizaException("No se pudo almacenar el archivo " + archivo.getOriginalFilename(), ex);
            }
        }

        Poliza polizaGuardada = polizaRepository.save(poliza);
        return mapToPolizaResponse(polizaGuardada);
    }

    public PolizaResponse actualizarPoliza(Long id, ActualizarPolizaRequest request, MultipartFile archivo) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new PolizaException(POLIZA_NO_ENCONTRADA));
        if (!poliza.getTitular().getId().equals(usuario.getId())) {
            throw new PolizaException("No tienes permiso para actualizar esta póliza");
        }

        poliza.setNumeroPoliza(request.numeroPoliza());
        poliza.setNombreAseguradora(request.nombreAseguradora());
        poliza.setPrima(request.prima());
        poliza.setFechaEmision(request.fechaEmision());
        poliza.setFechaVencimiento(request.fechaVencimiento());
        poliza.setTipoPoliza(request.tipoPoliza());

        if (archivo != null && !archivo.isEmpty()) {
            try {
                poliza.setNombreArchivo(archivo.getOriginalFilename());
                poliza.setTipoArchivo(archivo.getContentType());
                poliza.setArchivoData(archivo.getBytes());
                poliza.setRutaArchivo(UUID.randomUUID().toString());
            } catch (IOException ex) {
                throw new PolizaException("No se pudo almacenar el archivo " + archivo.getOriginalFilename(), ex);
            }
        }

        Poliza polizaActualizada = polizaRepository.save(poliza);
        return mapToPolizaResponse(polizaActualizada);
    }

    public PolizaResponse obtenerPoliza(Long id) {
        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new PolizaException(POLIZA_NO_ENCONTRADA));

        return mapToPolizaResponse(poliza);
    }

    public List<PolizaResponse> obtenerPolizasUsuario(Long idUsuario) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioAutenticado = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        if (!usuarioAutenticado.getId().equals(idUsuario) && 
            !usuarioAutenticado.getRole().name().equals(ADMIN)) {
            throw new PolizaException("No tienes permiso para ver las pólizas de este usuario");
        }

        List<Poliza> polizas = polizaRepository.findByTitularId(idUsuario);
        return polizas.stream()
                .map(this::mapToPolizaResponse)
                .toList();
    }

    public List<PolizaResponse> obtenerMisPolizas() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(POLIZA_NO_ENCONTRADA));

        List<Poliza> polizas = polizaRepository.findByTitular(usuario);
        return polizas.stream()
                .map(this::mapToPolizaResponse)
                .toList();
    }

    public Resource descargarArchivoPoliza(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new PolizaException(POLIZA_NO_ENCONTRADA));

        if (!poliza.getTitular().getId().equals(usuario.getId()) && 
            !usuario.getRole().name().equals(ADMIN)) {
            throw new PolizaException("No tienes permiso para descargar esta póliza");
        }

        if (poliza.getArchivoData() == null || poliza.getNombreArchivo() == null) {
            throw new PolizaException("Esta póliza no tiene un archivo adjunto");
        }

        try {
            Path tempFile = Files.createTempFile("poliza_" + id + "_", "_" + poliza.getNombreArchivo());
            Files.write(tempFile, poliza.getArchivoData());

            Resource resource = new UrlResource(tempFile.toUri());

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    log.error("Error al eliminar el archivo temporal", e);
                }
            }));

            return resource;
        } catch (IOException ex) {
            throw new PolizaException("Error al obtener el archivo", ex);
        }
    }

    public MessageResponse eliminarPoliza(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new PolizaException(POLIZA_NO_ENCONTRADA));

        if (!poliza.getTitular().getId().equals(usuario.getId()) && 
            !usuario.getRole().name().equals(ADMIN)) {
            throw new PolizaException("No tienes permiso para eliminar esta póliza");
        }

        polizaRepository.delete(poliza);

        return MessageResponse.builder()
                .message("Póliza eliminada correctamente")
                .success(true)
                .build();
    }

    private PolizaResponse mapToPolizaResponse(Poliza poliza) {
        return PolizaResponse.builder()
                .id(poliza.getId())
                .idTitular(poliza.getTitular().getId())
                .nombreTitular(poliza.getTitular().getNombreCompleto())
                .numeroPoliza(poliza.getNumeroPoliza())
                .nombreAseguradora(poliza.getNombreAseguradora())
                .prima(poliza.getPrima())
                .fechaEmision(poliza.getFechaEmision())
                .fechaVencimiento(poliza.getFechaVencimiento())
                .tipoPoliza(poliza.getTipoPoliza())
                .nombreArchivo(poliza.getNombreArchivo())
                .fechaCreacion(poliza.getFechaCreacion())
                .fechaActualizacion(poliza.getFechaActualizacion())
                .build();
    }
}
