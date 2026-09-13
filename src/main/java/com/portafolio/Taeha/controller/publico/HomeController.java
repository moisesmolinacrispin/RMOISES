package com.portafolio.Taeha.controller.publico;

import com.portafolio.Taeha.model.Perfil;
import com.portafolio.Taeha.repository.PerfilRepository;
import com.portafolio.Taeha.repository.SemanaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final SemanaRepository semanaRepository;
    private final PerfilRepository perfilRepository;

    public HomeController(
            SemanaRepository semanaRepository,
            PerfilRepository perfilRepository) {

        this.semanaRepository = semanaRepository;
        this.perfilRepository = perfilRepository;
    }

    @GetMapping("/")
    public String inicio(Model model) {

        // Obtener las 16 semanas
        model.addAttribute(
                "semanas",
                semanaRepository.findAllByOrderByNumeroAsc()
        );

        // Obtener el perfil
        Perfil perfil;

        if (perfilRepository.count() > 0) {

            perfil = perfilRepository.findAll().get(0);

        } else {

            perfil = new Perfil();
        }

        model.addAttribute("perfil", perfil);

        return "publico/index";
    }
}