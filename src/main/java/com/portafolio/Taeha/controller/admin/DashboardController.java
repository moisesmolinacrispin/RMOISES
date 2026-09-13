package com.portafolio.Taeha.controller.admin;

import com.portafolio.Taeha.repository.MensajeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    private final MensajeRepository mensajeRepository;

    public DashboardController(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    @GetMapping
    public String dashboard(Model model) {

        // ==========================================
        // DATOS DEL DASHBOARD
        // ==========================================

        // Por ahora trabajamos con las 16 semanas
        // definidas para el portafolio.
        long totalSemanas = 16;

        // Mensajes registrados en MySQL
        long totalMensajes = mensajeRepository.count();

        // Mensajes que todavía no han sido leídos
        long mensajesNoLeidos = mensajeRepository.countByLeidoFalse();

        // ==========================================
        // ENVIAR DATOS A THYMELEAF
        // ==========================================

        model.addAttribute(
                "totalSemanas",
                totalSemanas
        );

        model.addAttribute(
                "totalTrabajos",
                0
        );

        model.addAttribute(
                "totalMensajes",
                totalMensajes
        );

        model.addAttribute(
                "mensajesNoLeidos",
                mensajesNoLeidos
        );

        return "admin/dashboard";
    }
}