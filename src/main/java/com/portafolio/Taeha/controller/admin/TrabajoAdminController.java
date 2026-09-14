package com.portafolio.Taeha.controller.admin;

import com.portafolio.Taeha.model.Semana;
import com.portafolio.Taeha.model.Trabajo;
import com.portafolio.Taeha.repository.SemanaRepository;
import com.portafolio.Taeha.repository.TrabajoRepository;
import com.portafolio.Taeha.service.ArchivoService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/trabajos")
public class TrabajoAdminController {

    private final TrabajoRepository trabajoRepository;
    private final SemanaRepository semanaRepository;
    private final ArchivoService archivoService;

    public TrabajoAdminController(
            TrabajoRepository trabajoRepository,
            SemanaRepository semanaRepository,
            ArchivoService archivoService) {

        this.trabajoRepository = trabajoRepository;
        this.semanaRepository = semanaRepository;
        this.archivoService = archivoService;
    }


    // =========================================================
    // INICIO DE TRABAJOS
    // =========================================================

    @GetMapping
    public String inicioTrabajos(Model model) {

        model.addAttribute(
                "semanas",
                semanaRepository.findAllByOrderByNumeroAsc()
        );

        return "admin/trabajos-index";
    }


    // =========================================================
    // LISTAR TRABAJOS DE UNA SEMANA
    // =========================================================

    @GetMapping("/semana/{semanaId}")
    public String listarTrabajos(
            @PathVariable Long semanaId,
            Model model) {

        Semana semana = semanaRepository.findById(semanaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Semana no encontrada: " + semanaId
                        )
                );

        model.addAttribute(
                "semana",
                semana
        );

        model.addAttribute(
                "trabajos",
                trabajoRepository.findBySemanaId(semanaId)
        );

        return "admin/trabajos";
    }


    // =========================================================
    // NUEVO TRABAJO
    // =========================================================

    @GetMapping("/nuevo/{semanaId}")
    public String nuevoTrabajo(
            @PathVariable Long semanaId,
            Model model) {

        Semana semana = semanaRepository.findById(semanaId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Semana no encontrada: " + semanaId
                        )
                );

        Trabajo trabajo = new Trabajo();

        trabajo.setSemana(semana);

        model.addAttribute(
                "trabajo",
                trabajo
        );

        model.addAttribute(
                "semana",
                semana
        );

        model.addAttribute(
                "nuevo",
                true
        );

        return "admin/trabajo-form";
    }


    // =========================================================
    // GUARDAR / ACTUALIZAR TRABAJO
    // =========================================================

    @PostMapping("/guardar")
    public String guardarTrabajo(
            @ModelAttribute Trabajo trabajo,

            @RequestParam(
                    value = "imagenArchivo",
                    required = false
            )
            MultipartFile imagenArchivo,

            @RequestParam(
                    value = "archivoTrabajo",
                    required = false
            )
            MultipartFile archivoTrabajo) {


        // =====================================================
        // EDITAR TRABAJO EXISTENTE
        // =====================================================

        if (trabajo.getId() != null) {

            Trabajo trabajoExistente =
                    trabajoRepository.findById(
                            trabajo.getId()
                    ).orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Trabajo no encontrado: "
                                            + trabajo.getId()
                            )
                    );


            // -------------------------------------------------
            // ACTUALIZAR DATOS
            // -------------------------------------------------

            trabajoExistente.setTitulo(
                    trabajo.getTitulo()
            );

            trabajoExistente.setDescripcion(
                    trabajo.getDescripcion()
            );

            trabajoExistente.setEnlace(
                    trabajo.getEnlace()
            );

            trabajoExistente.setSemana(
                    trabajo.getSemana()
            );


            // -------------------------------------------------
            // NUEVA IMAGEN
            // -------------------------------------------------

            if (imagenArchivo != null &&
                    !imagenArchivo.isEmpty()) {

                try {
                    trabajoExistente.setImagen(imagenArchivo.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            // -------------------------------------------------
            // NUEVO ARCHIVO
            // -------------------------------------------------

            if (archivoTrabajo != null &&
                    !archivoTrabajo.isEmpty()) {

                try {
                    trabajoExistente.setArchivo(archivoTrabajo.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            // -------------------------------------------------
            // GUARDAR CAMBIOS
            // -------------------------------------------------

            trabajoRepository.save(
