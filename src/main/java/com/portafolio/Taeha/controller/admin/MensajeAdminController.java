package com.portafolio.Taeha.controller.admin;

import com.portafolio.Taeha.model.Mensaje;
import com.portafolio.Taeha.repository.MensajeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/mensajes")
public class MensajeAdminController {

    private final MensajeRepository mensajeRepository;

    public MensajeAdminController(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    // =====================================================
    // LISTAR MENSAJES
    // =====================================================

    @GetMapping
    public String listarMensajes(Model model) {

        model.addAttribute(
                "mensajes",
                mensajeRepository.findAll()
        );

        // Contador de mensajes no leídos
        model.addAttribute(
                "mensajesNoLeidos",
                mensajeRepository.countByLeidoFalse()
        );

        return "admin/mensajes";
    }

    // =====================================================
    // VER UN MENSAJE
    // =====================================================

    @GetMapping("/ver/{id}")
    public String verMensaje(
            @PathVariable Long id,
            Model model) {

        Mensaje mensaje = mensajeRepository
                .findById(id)
                .orElse(null);

        if (mensaje == null) {
            return "redirect:/admin/mensajes";
        }

        // Marcar como leído
        if (!mensaje.isLeido()) {

            mensaje.setLeido(true);

            mensajeRepository.save(mensaje);
        }

        model.addAttribute(
                "mensaje",
                mensaje
        );

        // Actualizar contador
        model.addAttribute(
                "mensajesNoLeidos",
                mensajeRepository.countByLeidoFalse()
        );

        return "admin/mensaje-ver";
    }

    // =====================================================
    // ELIMINAR MENSAJE
    // =====================================================

    @PostMapping("/eliminar/{id}")
    public String eliminarMensaje(
            @PathVariable Long id) {

        if (mensajeRepository.existsById(id)) {

            mensajeRepository.deleteById(id);
        }

        return "redirect:/admin/mensajes";
    }
}