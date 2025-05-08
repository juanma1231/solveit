package co.edu.uco.solveit.poliza.controller;

import co.edu.uco.solveit.poliza.dto.ActualizarPolizaRequest;
import co.edu.uco.solveit.poliza.dto.PolizaResponse;
import co.edu.uco.solveit.poliza.dto.RegistrarPolizaRequest;
import co.edu.uco.solveit.poliza.service.PolizaService;
import co.edu.uco.solveit.usuario.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/polizas")
@RequiredArgsConstructor
public class PolizaController {

    private final PolizaService polizaService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public PolizaResponse registrarPoliza(
            @RequestPart("poliza") RegistrarPolizaRequest request,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {
        return polizaService.registrarPoliza(request, archivo);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public PolizaResponse actualizarPoliza(
            @PathVariable Long id,
            @RequestPart("poliza") ActualizarPolizaRequest request,
            @RequestPart(value = "archivo", required = false) MultipartFile archivo) {
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