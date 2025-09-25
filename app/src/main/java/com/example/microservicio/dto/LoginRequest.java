package com.example.microservicio.dto;

// DTO corregido para coincidir con el backend Spring Boot
public class LoginRequest {
    private String username;  // ✅ CAMBIADO: era "email", ahora "username"
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}