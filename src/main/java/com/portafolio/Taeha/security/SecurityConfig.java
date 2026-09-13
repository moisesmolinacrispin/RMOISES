package com.portafolio.Taeha.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

                // ======================================
                // CSRF
                // ======================================

                .csrf(csrf -> csrf.disable())


                // ======================================
                // AUTORIZACIONES
                // ======================================

                .authorizeHttpRequests(auth -> auth

                        // ==================================
                        // PORTAFOLIO PÚBLICO
                        // ==================================

                        .requestMatchers(
                                "/",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/login",
                                "/publico/**",

                                // IMÁGENES DE LOS TRABAJOS
                                "/trabajos/imagen/**",

                                // ARCHIVOS DE LOS TRABAJOS
                                "/trabajos/archivo/**"
                        ).permitAll()


                        // ==================================
                        // PANEL ADMIN
                        // ==================================

                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")


                        // ==================================
                        // RESTO
                        // ==================================

                        .anyRequest()
                        .permitAll()
                )


                // ======================================
                // LOGIN
                // ======================================

                .formLogin(form -> form

                        .loginPage("/login")

                        .defaultSuccessUrl("/admin", true)

                        .permitAll()
                )


                // ======================================
                // LOGOUT
                // ======================================

                .logout(logout -> logout

                        .logoutSuccessUrl("/login?logout")

                        .permitAll()
                );


        return http.build();
    }


    // ==========================================
    // PASSWORD ENCODER
    // ==========================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}