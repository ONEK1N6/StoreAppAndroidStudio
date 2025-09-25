package com.example.microservicio.model;

import com.google.gson.annotations.SerializedName;

import java.util.Set;

// Esta clase es un "POJO" (Plain Old Java Object) que representa el modelo de Usuario en la app Android.
// Sus campos coinciden con los de tu entidad en el backend para que la librería de networking (Retrofit/Gson)
// pueda convertir automáticamente el JSON de la API a un objeto Java.
public class Usuario {

    @SerializedName("id")
    private Long id;

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    // No necesitamos el password en la app, pero lo mapeamos por si acaso
    @SerializedName("password")
    private String password;

    @SerializedName("codigoReset")
    private String codigoReset;

    // Se cambia LocalDateTime a String para que Gson lo pueda procesar fácilmente
    @SerializedName("resetExpira")
    private String resetExpira;

    @SerializedName("estado")
    private String estado;

    @SerializedName("roles")
    private Set<Rol> roles;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCodigoReset() { return codigoReset; }
    public void setCodigoReset(String codigoReset) { this.codigoReset = codigoReset; }
    public String getResetExpira() { return resetExpira; }
    public void setResetExpira(String resetExpira) { this.resetExpira = resetExpira; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Set<Rol> getRoles() { return roles; }
    public void setRoles(Set<Rol> roles) { this.roles = roles; }
}
