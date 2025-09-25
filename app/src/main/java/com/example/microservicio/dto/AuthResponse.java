package com.example.microservicio.dto;

import java.util.List;

// DTO corregido para coincidir exactamente con AuthResponseDto del backend
public class AuthResponse {
    private String accessToken;  // ✅ CAMBIADO: era "token", ahora "accessToken"
    private String username;
    private Long userId;
    private List<String> roles;

    // Getters y Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    // Método de conveniencia para obtener el token (por compatibilidad)
    public String getToken() {
        return accessToken;
    }
}