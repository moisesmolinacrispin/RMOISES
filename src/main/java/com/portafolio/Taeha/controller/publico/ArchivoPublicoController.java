package com.portafolio.Taeha.controller.publico;

import com.portafolio.Taeha.service.ArchivoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/publico")
public class ArchivoPublicoController {

    private final ArchivoService archivoService;

    public ArchivoPublicoController(ArchivoService archivoService) {
        this.archivoService = archivoService;
    }

    // =====================================================
    // MOSTRAR IMAGEN
    // =====================================================

    @GetMapping("/imagen/{nombre}")
    @ResponseBody
    public ResponseEntity<Resource> mostrarImagen(
            @PathVariable String nombre) {

        Resource recurso =
                archivoService.cargarImagen(nombre);

        if (recurso == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(recurso);
    }

    // =====================================================
    // MOSTRAR / DESCARGAR ARCHIVO
    // =====================================================

    @GetMapping("/archivo/{nombre}")
    @ResponseBody
    public ResponseEntity<Resource> mostrarArchivo(
            @PathVariable String nombre) {

        Resource recurso =
                archivoService.cargarArchivo(nombre);

        if (recurso == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + nombre + "\""
                )
                .body(recurso);
    }
}
