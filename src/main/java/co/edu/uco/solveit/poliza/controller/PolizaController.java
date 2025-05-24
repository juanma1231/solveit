package co.edu.uco.solveit.poliza.controller;

import co.edu.uco.solveit.poliza.dto.ActualizarPolizaRequest;
import co.edu.uco.solveit.poliza.dto.PolizaResponse;
import co.edu.uco.solveit.poliza.dto.RegistrarPolizaRequest;
import co.edu.uco.solveit.poliza.exception.PolizaException;
import co.edu.uco.solveit.poliza.service.PolizaService;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Controlador para la gestión de pólizas
 * Implementa validaciones de seguridad para la carga de archivos:
 * - Limita el tamaño de archivos a 10MB
 * - Solo permite archivos PDF
 * - Valida nombres de archivo para prevenir ataques de path traversal
 * - Verifica el contenido real del archivo para asegurar que es un PDF válido
 */
@RestController
@RequestMapping("/api/v1/polizas")
@RequiredArgsConstructor
@Slf4j
public class PolizaController {

    // Constantes para validación de archivos
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB en bytes
    private static final String CONTENT_TYPE_PDF = "application/pdf";
    private static final String[] ALLOWED_CONTENT_TYPES = {CONTENT_TYPE_PDF};

    private final PolizaService polizaService;

    /**
     * Valida que el archivo no exceda el tamaño máximo permitido
     * @param archivo El archivo a validar
     * @throws PolizaException Si el archivo excede el tamaño máximo
     */
    private void validarTamanoArchivo(MultipartFile archivo) {
        if (archivo != null && !archivo.isEmpty() && archivo.getSize() > MAX_FILE_SIZE) {
            throw new PolizaException("El archivo excede el tamaño máximo permitido de 10MB");
        }
    }

    /**
     * Valida que el archivo sea de tipo PDF
     * @param archivo El archivo a validar
     * @throws PolizaException Si el archivo no es de tipo PDF
     */
    private void validarTipoArchivo(MultipartFile archivo) {
        if (archivo != null && !archivo.isEmpty()) {
            String contentType = archivo.getContentType();
            if (contentType == null || !Arrays.asList(ALLOWED_CONTENT_TYPES).contains(contentType)) {
                throw new PolizaException("Solo se permiten archivos PDF");
            }

            // Validación adicional del nombre del archivo para prevenir ataques
            String filename = archivo.getOriginalFilename();
            if (filename != null && !filename.toLowerCase().endsWith(".pdf")) {
                throw new PolizaException("El archivo debe tener extensión .pdf");
            }
        }
    }

    /**
     * Valida que el nombre del archivo sea seguro (previene path traversal)
     * @param archivo El archivo a validar
     * @throws PolizaException Si el nombre del archivo contiene caracteres no permitidos
     */
    private void validarNombreArchivo(MultipartFile archivo) {
        if (archivo != null && !archivo.isEmpty()) {
            String filename = archivo.getOriginalFilename();
            if (filename != null && (filename.contains("..") || filename.contains("/") || filename.contains("\\"))) {
                throw new PolizaException("El nombre del archivo contiene caracteres no permitidos");
            }
        }
    }

    /**
     * Verifica el contenido real del archivo para asegurar que es un PDF válido
     * Comprueba la firma de archivo PDF (%PDF) al inicio del archivo
     * @param archivo El archivo a validar
     * @throws PolizaException Si el archivo no es un PDF válido
     */
    private void verificarContenidoPDF(MultipartFile archivo) {
        if (archivo != null && !archivo.isEmpty()) {
            try (InputStream is = archivo.getInputStream()) {
                byte[] pdfHeader = new byte[5];
                if (is.read(pdfHeader) != -1) {
                    String header = new String(pdfHeader);
                    if (!header.startsWith("%PDF")) {
                        log.warn("Intento de subir un archivo con contenido no PDF: {}", archivo.getOriginalFilename());
                        throw new PolizaException("El archivo no es un PDF válido");
                    }
                }
            } catch (IOException e) {
                log.error("Error al verificar el contenido del archivo", e);
                throw new PolizaException("No se pudo verificar el contenido del archivo");
            }
        }
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public PolizaResponse registrarPoliza(
            @RequestPart("poliza") RegistrarPolizaRequest request,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {
        // Validar el archivo si se ha proporcionado
        if (archivo != null && !archivo.isEmpty()) {
            validarTamanoArchivo(archivo);
            validarTipoArchivo(archivo);
            validarNombreArchivo(archivo);
            verificarContenidoPDF(archivo);
        }
        return polizaService.registrarPoliza(request, archivo);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public PolizaResponse actualizarPoliza(
            @PathVariable Long id,
            @RequestPart("poliza") ActualizarPolizaRequest request,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {
        // Validar el archivo si se ha proporcionado
        if (archivo != null && !archivo.isEmpty()) {
            validarTamanoArchivo(archivo);
            validarTipoArchivo(archivo);
            validarNombreArchivo(archivo);
            verificarContenidoPDF(archivo);
        }
        return polizaService.actualizarPoliza(id, request, archivo);
    }

    @GetMapping("/{id}")
    public PolizaResponse obtenerPoliza(@PathVariable Long id) {
        return polizaService.obtenerPoliza(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<PolizaResponse> obtenerPolizasUsuario(@PathVariable Long idUsuario) {
        return polizaService.obtenerPolizasUsuario(idUsuario);
    }

    @GetMapping("/mis-polizas")
    public List<PolizaResponse> obtenerMisPolizas() {
        return polizaService.obtenerMisPolizas();
    }

    @GetMapping("/{id}/descargar")
    public ResponseEntity<Resource> descargarArchivoPoliza(@PathVariable Long id) {
        Resource archivo = polizaService.descargarArchivoPoliza(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getFilename() + "\"")
                .body(archivo);
    }

    @DeleteMapping("/{id}")
    public MessageResponse eliminarPoliza(@PathVariable Long id) {
        return polizaService.eliminarPoliza(id);
    }
}
