package com.example.microservicio.model;

import com.google.gson.annotations.SerializedName;

// Al igual que la clase Usuario, esta clase Rol mapea la entidad Rol de tu backend.
// Esto permite manejar los roles de usuario que vienen desde la API.
public class Rol {

    @SerializedName("id")
    private Long id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("estado")
    private String estado;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}