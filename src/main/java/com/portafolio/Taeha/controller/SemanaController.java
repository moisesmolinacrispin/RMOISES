package com.portafolio.Taeha.controller;

import com.portafolio.Taeha.model.Semana;
import com.portafolio.Taeha.repository.SemanaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/semanas")
public class SemanaController {

    private final SemanaRepository semanaRepository;

    public SemanaController(SemanaRepository semanaRepository) {
        this.semanaRepository = semanaRepository;
    }

    // LISTAR SEMANAS
    @GetMapping
    public String listarSemanas(Model model) {

        model.addAttribute(
                "semanas",
                semanaRepository.findAllByOrderByNumeroAsc()
        );

        return "admin/semanas";
    }

    // MOSTRAR FORMULARIO PARA CREAR
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {

        model.addAttribute("semana", new Semana());

        return "admin/semana-form";
    }

    // GUARDAR SEMANA
    @PostMapping("/guardar")
    public String guardarSemana(@ModelAttribute Semana semana) {

        semanaRepository.save(semana);

        return "redirect:/admin/semanas";
    }

    // MOSTRAR FORMULARIO PARA EDITAR
    @GetMapping("/editar/{id}")
    public String editarSemana(
            @PathVariable Long id,
            Model model) {

        Semana semana = semanaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Semana no encontrada: " + id
                        ));

        model.addAttribute("semana", semana);

        return "admin/semana-form";
    }

    // ELIMINAR SEMANA
    @GetMapping("/eliminar/{id}")
    public String eliminarSemana(@PathVariable Long id) {

        semanaRepository.deleteById(id);

        return "redirect:/admin/semanas";
    }
}