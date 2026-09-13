package com.portafolio.Taeha;

import com.portafolio.Taeha.model.Semana;
import com.portafolio.Taeha.model.Usuario;
import com.portafolio.Taeha.repository.SemanaRepository;
import com.portafolio.Taeha.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner inicializarDatos(
            UsuarioRepository usuarioRepository,
            SemanaRepository semanaRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // ==============================
            // CREAR ADMINISTRADOR
            // ==============================

            if (usuarioRepository.findByUsuario("admin").isEmpty()) {

                Usuario usuario = new Usuario();

                usuario.setUsuario("admin");
                usuario.setPassword(
                        passwordEncoder.encode("Admin123*")
                );
                usuario.setNombre("RIVAS PIZARRO Sebastian");
                usuario.setRol("ADMIN");
                usuario.setEstado(true);

                usuarioRepository.save(usuario);

                System.out.println("=================================");
                System.out.println("ADMINISTRADOR CREADO");
                System.out.println("Usuario: admin");
                System.out.println("Contraseña: Admin123*");
                System.out.println("=================================");
            }


            // ==============================
            // CREAR LAS 16 SEMANAS
            // ==============================

            if (semanaRepository.count() == 0) {

                for (int i = 1; i <= 16; i++) {

                    Semana semana = new Semana();

                    semana.setNumero(i);
                    semana.setTitulo("Semana " + i);
                    semana.setDescripcion(
                            "Contenido académico de la semana " + i
                    );

                    semanaRepository.save(semana);
                }

                System.out.println("=================================");
                System.out.println("16 SEMANAS CREADAS");
                System.out.println("=================================");
            }
        };
    }
}