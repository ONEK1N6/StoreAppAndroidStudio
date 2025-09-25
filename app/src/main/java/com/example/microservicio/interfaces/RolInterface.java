package com.example.microservicio.interfaces;

import com.example.microservicio.model.Rol;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

// Interfaz para interactuar con RolController
public interface RolInterface {

    @GET("api/rol")
    Call<List<Rol>> getAllRoles();

    @GET("api/rol/{id}")
    Call<Rol> findRolById(@Path("id") Long id);

    @POST("api/rol")
    Call<Rol> createRol(@Body Rol rol);

    @PUT("api/rol/{id}")
    Call<Rol> updateRol(@Path("id") Long id, @Body Rol rol);

    @DELETE("api/rol/{id}")
    Call<Void> deleteRol(@Path("id") Long id); // Usamos Void porque no esperamos respuesta
}
