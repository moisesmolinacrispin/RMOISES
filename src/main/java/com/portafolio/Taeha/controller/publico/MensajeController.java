
        package com.portafolio.Taeha.controller.publico;

import com.portafolio.Taeha.model.Mensaje;
import com.portafolio.Taeha.repository.MensajeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/mensaje")
public class MensajeController {

    private final MensajeRepository mensajeRepository;

    public MensajeController(MensajeRepository mensajeRepository) {
        this.mensajeRepository = mensajeRepository;
    }

    @PostMapping("/enviar")
    @ResponseBody
    public Map<String, Object> enviarMensaje(
            @ModelAttribute("mensajeForm") Mensaje mensaje) {

        mensaje.setFecha(LocalDateTime.now());

        mensajeRepository.save(mensaje);

        Map<String, Object> respuesta = new HashMap<>();

        respuesta.put("exito", true);
        respuesta.put(
                "mensaje",
                "¡Mensaje enviado correctamente!"
        );

        return respuesta;
    }
}

