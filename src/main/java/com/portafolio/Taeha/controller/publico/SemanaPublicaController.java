package com.portafolio.Taeha.controller.publico;

import com.portafolio.Taeha.model.Perfil;
import com.portafolio.Taeha.model.Semana;
import com.portafolio.Taeha.repository.PerfilRepository;
import com.portafolio.Taeha.repository.SemanaRepository;
import com.portafolio.Taeha.repository.TrabajoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/semana")
public class SemanaPublicaController {

    private final SemanaRepository semanaRepository;
    private final TrabajoRepository trabajoRepository;
    private final PerfilRepository perfilRepository;

    public SemanaPublicaController(
            SemanaRepository semanaRepository,
            TrabajoRepository trabajoRepository,
            PerfilRepository perfilRepository) {

        this.semanaRepository = semanaRepository;
        this.trabajoRepository = trabajoRepository;
        this.perfilRepository = perfilRepository;
    }

    @GetMapping("/{id}")
    public String verSemana(
            @PathVariable Long id,
            Model model) {

        Semana semana = semanaRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Semana no encontrada: " + id
                        )
                );

        model.addAttribute("semana", semana);

        model.addAttribute(
                "trabajos",
                trabajoRepository.findBySemanaId(id)
        );

        // Cargar perfil
        Perfil perfil = perfilRepository.findAll()
                .stream()
                .findFirst()
                .orElse(null);

        model.addAttribute("perfil", perfil);

        return "publico/semana";
    }
}