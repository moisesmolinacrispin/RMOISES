package com.portafolio.Taeha.repository;

import com.portafolio.Taeha.model.Semana;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemanaRepository extends JpaRepository<Semana, Long> {

    List<Semana> findAllByOrderByNumeroAsc();
}