package com.portafolio.Taeha.repository;

import com.portafolio.Taeha.model.Trabajo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrabajoRepository
        extends JpaRepository<Trabajo, Long> {

    List<Trabajo> findBySemanaId(Long semanaId);

}