package com.example.microservicio.interfaces;

import com.example.microservicio.model.Usuario;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

// Interfaz para interactuar con UsuarioController
public interface UsuarioInterface {

    @GET("api/usuario")
    Call<List<Usuario>> getAllUsuarios();

    @GET("api/usuario/{id}")
    Call<Usuario> findUsuarioById(@Path("id") Long id);

    @POST("api/usuario")
    Call<Usuario> createUsuario(@Body Usuario usuario);

    @GET("api/usuario/by-username/{username}")
    Call<Usuario> findByUsername(@Path("username") String username);
}
