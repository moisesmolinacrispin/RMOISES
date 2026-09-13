package com.portafolio.Taeha.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String correo;

    private String asunto;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    private LocalDateTime fecha;

    // Indica si el administrador ya leyó el mensaje
    private boolean leido = false;

    // Constructor vacío
    public Mensaje() {
    }

    // GETTER Y SETTER DEL ID
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // GETTER Y SETTER DEL NOMBRE
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // GETTER Y SETTER DEL CORREO
    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    // GETTER Y SETTER DEL ASUNTO
    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    // GETTER Y SETTER DEL MENSAJE
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    // GETTER Y SETTER DE LA FECHA
    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    // GETTER Y SETTER DE LEÍDO
    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }
}