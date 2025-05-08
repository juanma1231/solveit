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
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PolizaService {

    private final PolizaRepository polizaRepository;
    private final UsuarioRepository usuarioRepository;
    private final Path fileStorageLocation = Paths.get("uploads/polizas").toAbsolutePath().normalize();

    public PolizaService(PolizaRepository polizaRepository, UsuarioRepository usuarioRepository) {
        this.polizaRepository = polizaRepository;
        this.usuarioRepository = usuarioRepository;

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new PolizaException("No se pudo crear el directorio para almacenar los archivos.", ex);
        }
    }

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
            String nombreArchivo = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
            Path targetLocation = fileStorageLocation.resolve(nombreArchivo);

            try {
                Files.copy(archivo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                poliza.setNombreArchivo(archivo.getOriginalFilename());
                poliza.setRutaArchivo(targetLocation.toString());
                poliza.setTipoArchivo(archivo.getContentType());
            } catch (IOException ex) {
                throw new PolizaException("No se pudo almacenar el archivo " + nombreArchivo, ex);
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
                .orElseThrow(() -> new PolizaException("Póliza no encontrada"));

        // Verificar que el usuario sea el titular de la póliza
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
            // Eliminar archivo anterior si existe
            if (poliza.getRutaArchivo() != null) {
                try {
                    Files.deleteIfExists(Paths.get(poliza.getRutaArchivo()));
                } catch (IOException ex) {
                    // Log error but continue
                    System.err.println("No se pudo eliminar el archivo anterior: " + ex.getMessage());
                }
            }

            String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
            Path targetLocation = fileStorageLocation.resolve(nombreArchivo);

            try {
                Files.copy(archivo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                poliza.setNombreArchivo(archivo.getOriginalFilename());
                poliza.setRutaArchivo(targetLocation.toString());
                poliza.setTipoArchivo(archivo.getContentType());
            } catch (IOException ex) {
                throw new PolizaException("No se pudo almacenar el archivo " + nombreArchivo, ex);
            }
        }

        Poliza polizaActualizada = polizaRepository.save(poliza);
        return mapToPolizaResponse(polizaActualizada);
    }

    public PolizaResponse obtenerPoliza(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new PolizaException("Póliza no encontrada"));

        // Verificar que el usuario sea el titular de la póliza o un administrador
        if (!poliza.getTitular().getId().equals(usuario.getId()) && 
            !usuario.getRole().name().equals("ADMIN")) {
            throw new PolizaException("No tienes permiso para ver esta póliza");
        }

        return mapToPolizaResponse(poliza);
    }

    public List<PolizaResponse> obtenerPolizasUsuario(Long idUsuario) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioAutenticado = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Verificar que el usuario autenticado sea el mismo que se está consultando o un administrador
        if (!usuarioAutenticado.getId().equals(idUsuario) && 
            !usuarioAutenticado.getRole().name().equals("ADMIN")) {
            throw new PolizaException("No tienes permiso para ver las pólizas de este usuario");
        }

        List<Poliza> polizas = polizaRepository.findByTitularId(idUsuario);
        return polizas.stream()
                .map(this::mapToPolizaResponse)
                .collect(Collectors.toList());
    }

    public List<PolizaResponse> obtenerMisPolizas() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<Poliza> polizas = polizaRepository.findByTitular(usuario);
        return polizas.stream()
                .map(this::mapToPolizaResponse)
                .collect(Collectors.toList());
    }

    public Resource descargarArchivoPoliza(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new PolizaException("Póliza no encontrada"));

        // Verificar que el usuario sea el titular de la póliza o un administrador
        if (!poliza.getTitular().getId().equals(usuario.getId()) && 
            !usuario.getRole().name().equals("ADMIN")) {
            throw new PolizaException("No tienes permiso para descargar esta póliza");
        }

        if (poliza.getRutaArchivo() == null) {
            throw new PolizaException("Esta póliza no tiene un archivo adjunto");
        }

        try {
            Path filePath = Paths.get(poliza.getRutaArchivo());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new PolizaException("El archivo no existe");
            }
        } catch (MalformedURLException ex) {
            throw new PolizaException("Error al obtener el archivo", ex);
        }
    }

    public MessageResponse eliminarPoliza(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        Poliza poliza = polizaRepository.findById(id)
                .orElseThrow(() -> new PolizaException("Póliza no encontrada"));

        // Verificar que el usuario sea el titular de la póliza
        if (!poliza.getTitular().getId().equals(usuario.getId()) && 
            !usuario.getRole().name().equals("ADMIN")) {
            throw new PolizaException("No tienes permiso para eliminar esta póliza");
        }

        // Eliminar archivo si existe
        if (poliza.getRutaArchivo() != null) {
            try {
                Files.deleteIfExists(Paths.get(poliza.getRutaArchivo()));
            } catch (IOException ex) {
                // Log error but continue
                System.err.println("No se pudo eliminar el archivo: " + ex.getMessage());
            }
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
