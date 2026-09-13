package com.portafolio.Taeha.controller;

import com.portafolio.Taeha.service.ArchivoService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TrabajoPublicController {

    private final ArchivoService archivoService;

    public TrabajoPublicController(ArchivoService archivoService) {
        this.archivoService = archivoService;
    }


    // =====================================================
    // MOSTRAR IMAGEN PÚBLICA
    // =====================================================

    @GetMapping("/trabajos/imagen/{nombre}")
    @ResponseBody
    public ResponseEntity<Resource> verImagen(
            @PathVariable String nombre) {

        Resource resource =
                archivoService.cargarImagen(nombre);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType tipo = MediaType.IMAGE_JPEG;

        String nombreMinuscula = nombre.toLowerCase();

        if (nombreMinuscula.endsWith(".png")) {

            tipo = MediaType.IMAGE_PNG;

        } else if (nombreMinuscula.endsWith(".gif")) {

            tipo = MediaType.IMAGE_GIF;

        } else if (nombreMinuscula.endsWith(".webp")) {

            tipo = MediaType.parseMediaType("image/webp");

        }

        return ResponseEntity
                .ok()
                .contentType(tipo)
                .body(resource);
    }


    // =====================================================
    // MOSTRAR ARCHIVO PÚBLICO
    // =====================================================

    @GetMapping("/trabajos/archivo/{nombre}")
    @ResponseBody
    public ResponseEntity<Resource> verArchivo(
            @PathVariable String nombre) {

        Resource resource =
                archivoService.cargarArchivo(nombre);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        MediaType tipo =
                MediaType.APPLICATION_OCTET_STREAM;

        String nombreMinuscula =
                nombre.toLowerCase();

        if (nombreMinuscula.endsWith(".pdf")) {

            tipo = MediaType.APPLICATION_PDF;

        } else if (nombreMinuscula.endsWith(".doc")) {

            tipo = MediaType.parseMediaType(
                    "application/msword"
            );

        } else if (nombreMinuscula.endsWith(".docx")) {

            tipo = MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );

        } else if (nombreMinuscula.endsWith(".xls")) {

            tipo = MediaType.parseMediaType(
                    "application/vnd.ms-excel"
            );

        } else if (nombreMinuscula.endsWith(".xlsx")) {

            tipo = MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            );

        } else if (nombreMinuscula.endsWith(".ppt")) {

            tipo = MediaType.parseMediaType(
                    "application/vnd.ms-powerpoint"
            );

        } else if (nombreMinuscula.endsWith(".pptx")) {

            tipo = MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"
            );

        }

        return ResponseEntity
                .ok()
                .contentType(tipo)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + nombre + "\""
                )
                .body(resource);
    }
}