package com.portafolio.Taeha.controller.admin;



import com.portafolio.Taeha.model.Semana;

import com.portafolio.Taeha.model.Trabajo;

import com.portafolio.Taeha.repository.SemanaRepository;

import com.portafolio.Taeha.repository.TrabajoRepository;

import com.portafolio.Taeha.service.ArchivoService;



import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;

import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;



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



                String imagenAnterior =

                        trabajoExistente.getImagen();



                String nuevaImagen =

                        archivoService.guardarImagen(

                                imagenArchivo

                        );



                trabajoExistente.setImagen(

                        nuevaImagen

                );





                // Eliminar imagen anterior



                if (imagenAnterior != null &&

                        !imagenAnterior.isBlank()) {



                    archivoService.eliminarImagen(

                            imagenAnterior

                    );

                }

            }





            // -------------------------------------------------

            // NUEVO ARCHIVO

            // -------------------------------------------------



            if (archivoTrabajo != null &&

                    !archivoTrabajo.isEmpty()) {



                String archivoAnterior =

                        trabajoExistente.getArchivo();



                String nuevoArchivo =

                        archivoService.guardarArchivo(

                                archivoTrabajo

                        );



                trabajoExistente.setArchivo(

                        nuevoArchivo

                );





                // Eliminar archivo anterior



                if (archivoAnterior != null &&

                        !archivoAnterior.isBlank()) {



                    archivoService.eliminarArchivo(

                            archivoAnterior

                    );

                }

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

        // GUARDAR IMAGEN

        // -----------------------------------------------------



        if (imagenArchivo != null &&

                !imagenArchivo.isEmpty()) {



            String nombreImagen =

                    archivoService.guardarImagen(

                            imagenArchivo

                    );



            trabajo.setImagen(

                    nombreImagen

            );

        }





        // -----------------------------------------------------

        // GUARDAR ARCHIVO

        // -----------------------------------------------------



        if (archivoTrabajo != null &&

                !archivoTrabajo.isEmpty()) {



            String nombreArchivo =

                    archivoService.guardarArchivo(

                            archivoTrabajo

                    );



            trabajo.setArchivo(

                    nombreArchivo

            );

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

        // OBTENER ARCHIVOS

        // -----------------------------------------------------



        String imagen =

                trabajo.getImagen();



        String archivo =

                trabajo.getArchivo();





        // -----------------------------------------------------

        // ELIMINAR IMAGEN FÍSICA

        // -----------------------------------------------------



        if (imagen != null &&

                !imagen.isBlank()) {



            archivoService.eliminarImagen(

                    imagen

            );

        }





        // -----------------------------------------------------

        // ELIMINAR ARCHIVO FÍSICO

        // -----------------------------------------------------



        if (archivo != null &&

                !archivo.isBlank()) {



            archivoService.eliminarArchivo(

                    archivo

            );

        }





        // -----------------------------------------------------

        // ELIMINAR REGISTRO DE MYSQL

        // -----------------------------------------------------



        trabajoRepository.deleteById(

                id

        );





        return "redirect:/admin/trabajos/semana/"

                + semanaId;

    }





    // =========================================================

    // MOSTRAR IMAGEN

    // =========================================================



    @GetMapping("/imagen/{nombre}")

    @ResponseBody

    public ResponseEntity<Resource> verImagen(

            @PathVariable String nombre) {



        Resource resource =

                archivoService.cargarImagen(

                        nombre

                );



        if (resource == null ||

                !resource.exists()) {



            return ResponseEntity

                    .notFound()

                    .build();

        }





        MediaType tipo =

                MediaType.IMAGE_JPEG;



        String nombreMinuscula =

                nombre.toLowerCase();





        if (nombreMinuscula.endsWith(".png")) {



            tipo = MediaType.IMAGE_PNG;



        } else if (nombreMinuscula.endsWith(".gif")) {



            tipo = MediaType.IMAGE_GIF;



        } else if (nombreMinuscula.endsWith(".webp")) {



            tipo = MediaType.parseMediaType(

                    "image/webp"

            );

        }





        return ResponseEntity

                .ok()

                .contentType(tipo)

                .body(resource);

    }





    // =========================================================

    // MOSTRAR ARCHIVO

    // =========================================================



    @GetMapping("/archivo/{nombre}")

    @ResponseBody

    public ResponseEntity<Resource> verArchivo(

            @PathVariable String nombre) {



        Resource resource =

                archivoService.cargarArchivo(

                        nombre

                );





        // -----------------------------------------------------

        // ARCHIVO NO ENCONTRADO

        // -----------------------------------------------------



        if (resource == null ||

                !resource.exists()) {



            return ResponseEntity

                    .notFound()

                    .build();

        }





        // -----------------------------------------------------

        // TIPO MIME

        // -----------------------------------------------------



        MediaType tipo =

                MediaType.APPLICATION_OCTET_STREAM;



        String nombreMinuscula =

                nombre.toLowerCase();





        // PDF

        if (nombreMinuscula.endsWith(".pdf")) {



            tipo = MediaType.APPLICATION_PDF;

        }





        // WORD

        else if (nombreMinuscula.endsWith(".doc")) {



            tipo = MediaType.parseMediaType(

                    "application/msword"

            );

        }



        else if (nombreMinuscula.endsWith(".docx")) {



            tipo = MediaType.parseMediaType(

                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

            );

        }





        // EXCEL

        else if (nombreMinuscula.endsWith(".xls")) {



            tipo = MediaType.parseMediaType(

                    "application/vnd.ms-excel"

            );

        }



        else if (nombreMinuscula.endsWith(".xlsx")) {



            tipo = MediaType.parseMediaType(

                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

            );

        }





        // POWERPOINT

        else if (nombreMinuscula.endsWith(".ppt")) {



            tipo = MediaType.parseMediaType(

                    "application/vnd.ms-powerpoint"

            );

        }



        else if (nombreMinuscula.endsWith(".pptx")) {



            tipo = MediaType.parseMediaType(

                    "application/vnd.openxmlformats-officedocument.presentationml.presentation"

            );

        }





        // TXT

        else if (nombreMinuscula.endsWith(".txt")) {



            tipo = MediaType.TEXT_PLAIN;

        }





        // -----------------------------------------------------

        // RESPUESTA

        // -----------------------------------------------------



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
