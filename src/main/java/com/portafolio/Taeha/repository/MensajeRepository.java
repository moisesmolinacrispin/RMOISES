package com.portafolio.Taeha.repository;

import com.portafolio.Taeha.model.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    // Contar mensajes que todavía no han sido leídos
    long countByLeidoFalse();

}