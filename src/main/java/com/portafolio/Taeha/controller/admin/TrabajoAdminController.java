package com.portafolio.Taeha.controller.admin;

import com.portafolio.Taeha.model.Semana;
import com.portafolio.Taeha.model.Trabajo;
import com.portafolio.Taeha.repository.SemanaRepository;
import com.portafolio.Taeha.repository.TrabajoRepository;
import com.portafolio.Taeha.service.ArchivoService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    // GUARDAR / ACTUALIZAR TRABAJO (EN MYSQL DIRECTO)
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
            MultipartFile archivoTrabajo) throws IOException {


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
            // NUEVA IMAGEN (LONGBLOB EN MYSQL)
            // -------------------------------------------------

            if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
                trabajoExistente.setImagen(imagenArchivo.getBytes());
            }


            // -------------------------------------------------
            // NUEVO ARCHIVO (LONGBLOB EN MYSQL)
            // -------------------------------------------------

            if (archivoTrabajo != null && !archivoTrabajo.isEmpty()) {
                trabajoExistente.setArchivo(archivoTrabajo.getBytes());
            }


            // -------------------------------------------------
            // GUARDAR CAMBIOS
            // -------------------------------------------------

            trabajoRepository.save(
                    trabajoExistente
            );


            return "redirect:/admin/trabajos/semana/"
                    + trabajoExistente
                    .getSemana()
                    .getId();
        }


        // =====================================================
        // CREAR NUEVO TRABAJO
        // =====================================================


        // -----------------------------------------------------
        // GUARDAR IMAGEN EN MYSQL
        // -----------------------------------------------------

        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            trabajo.setImagen(imagenArchivo.getBytes());
        }


        // -----------------------------------------------------
        // GUARDAR ARCHIVO EN MYSQL
        // -----------------------------------------------------

        if (archivoTrabajo != null && !archivoTrabajo.isEmpty()) {
            trabajo.setArchivo(archivoTrabajo.getBytes());
        }


        // -----------------------------------------------------
        // GUARDAR EN MYSQL
        // -----------------------------------------------------

        trabajoRepository.save(
                trabajo
        );


        return "redirect:/admin/trabajos/semana/"
                + trabajo.getSemana().getId();
    }


    // =========================================================
    // EDITAR TRABAJO
    // =========================================================

    @GetMapping("/editar/{id}")
    public String editarTrabajo(
            @PathVariable Long id,
            Model model) {

        Trabajo trabajo =
                trabajoRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Trabajo no encontrado: "
                                                + id
                                )
                        );

        model.addAttribute(
                "trabajo",
                trabajo
        );

        model.addAttribute(
                "semana",
                trabajo.getSemana()
        );

        model.addAttribute(
                "nuevo",
                false
        );

        return "admin/trabajo-form";
    }


    // =========================================================
    // ELIMINAR TRABAJO
    // =========================================================

    @GetMapping("/eliminar/{id}")
    public String eliminarTrabajo(
            @PathVariable Long id) {

        Trabajo trabajo =
                trabajoRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Trabajo no encontrado: "
                                                + id
                                )
                        );

        Long semanaId =
                trabajo.getSemana().getId();


        // -----------------------------------------------------
        // ELIMINAR REGISTRO DE MYSQL (FOTO Y ARCHIVO SE BORRAN SOLOS)
        // -----------------------------------------------------

        trabajoRepository.deleteById(
                id
        );


        return "redirect:/admin/trabajos/semana/"
                + semanaId;
    }


    // =========================================================
    // SERVIR IMAGEN DESDE MYSQL POR ID
    // =========================================================

    @GetMapping("/imagen/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> verImagen(@PathVariable Long id) {

        Trabajo trabajo = trabajoRepository.findById(id).orElse(null);

        if (trabajo != null && trabajo.getImagen() != null && trabajo.getImagen().length > 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(trabajo.getImagen(), headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    // =========================================================
    // SERVIR ARCHIVO DESDE MYSQL POR ID
    // =========================================================

    @GetMapping("/archivo/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> verArchivo(@PathVariable Long id) {

        Trabajo trabajo = trabajoRepository.findById(id).orElse(null);

        if (trabajo != null && trabajo.getArchivo() != null && trabajo.getArchivo().length > 0) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"documento.pdf\"");

            return new ResponseEntity<>(trabajo.getArchivo(), headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
