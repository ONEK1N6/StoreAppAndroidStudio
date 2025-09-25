package com.example.microservicio.interfaces;

import com.example.microservicio.dto.AuthResponse;
import com.example.microservicio.dto.LoginRequest;
import com.example.microservicio.dto.RegistroDto;
import com.example.microservicio.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthInterface {

    // Corregimos la ruta para que sea relativa a la BASE_URL
    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);
    @POST("auth/register")
    Call<Usuario> register(@Body RegistroDto registroDto);
    // Aquí podrías agregar los otros endpoints de tu AuthController
    // como register, solicitar, confirmar...
}
