package com.portafolio.Taeha.controller.admin;

import com.portafolio.Taeha.model.Perfil;
import com.portafolio.Taeha.repository.PerfilRepository;
import com.portafolio.Taeha.service.ArchivoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/perfil")
public class PerfilAdminController {

    private final PerfilRepository perfilRepository;
    private final ArchivoService archivoService;

    public PerfilAdminController(
            PerfilRepository perfilRepository,
            ArchivoService archivoService) {

        this.perfilRepository = perfilRepository;
        this.archivoService = archivoService;
    }


    // ==========================================
    // MOSTRAR PERFIL
    // ==========================================

    @GetMapping
    public String mostrarPerfil(Model model) {

        Perfil perfil;

        if (perfilRepository.count() == 0) {

            perfil = new Perfil();

        } else {

            perfil = perfilRepository.findAll().get(0);
        }

        model.addAttribute("perfil", perfil);

        return "admin/perfil";
    }


    // ==========================================
    // GUARDAR PERFIL
    // ==========================================

    @PostMapping("/guardar")
    public String guardarPerfil(

            @ModelAttribute Perfil perfil,

            @RequestParam(value = "archivoFoto", required = false)
            MultipartFile archivoFoto) {


        Perfil perfilGuardar;


        // ======================================
        // BUSCAR PERFIL EXISTENTE
        // ======================================

        if (perfilRepository.count() > 0) {

            perfilGuardar = perfilRepository.findAll().get(0);

        } else {

            perfilGuardar = new Perfil();
        }


        // ======================================
        // DATOS PERSONALES
        // ======================================

        perfilGuardar.setNombre(
                perfil.getNombre()
        );

        perfilGuardar.setApellidos(
                perfil.getApellidos()
        );

        perfilGuardar.setCarrera(
                perfil.getCarrera()
        );

        perfilGuardar.setInstituto(
                perfil.getInstituto()
        );


        // ======================================
        // PRESENTACIÓN
        // ======================================

        perfilGuardar.setDescripcion(
                perfil.getDescripcion()
        );

        perfilGuardar.setSobreMi(
                perfil.getSobreMi()
        );


        // ======================================
        // CONTACTO
        // ======================================

        perfilGuardar.setCorreo(
                perfil.getCorreo()
        );

        perfilGuardar.setTelefono(
                perfil.getTelefono()
        );


        // ======================================
        // REDES
        // ======================================

        perfilGuardar.setGithub(
                perfil.getGithub()
        );

        perfilGuardar.setLinkedin(
                perfil.getLinkedin()
        );


        // ======================================
        // FOTO
        // ======================================

        if (archivoFoto != null && !archivoFoto.isEmpty()) {

            // Si ya existe una foto,
            // eliminarla físicamente

            if (perfilGuardar.getFoto() != null
                    && !perfilGuardar.getFoto().isBlank()) {

                archivoService.eliminarImagen(
                        perfilGuardar.getFoto()
                );
            }


            // Guardar nueva foto

            String nombreFoto =
                    archivoService.guardarImagen(archivoFoto);


            // Guardar nombre en MySQL

            perfilGuardar.setFoto(nombreFoto);
        }


        // ======================================
        // GUARDAR EN MYSQL
        // ======================================

        perfilRepository.save(perfilGuardar);


        // ======================================
        // VOLVER AL PERFIL
        // ======================================

        return "redirect:/admin/perfil";
    }
}
