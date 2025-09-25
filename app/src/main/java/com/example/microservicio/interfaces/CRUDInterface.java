package com.example.microservicio.interfaces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

// Creamos una interfaz genérica para operaciones CRUD, como en el ejemplo de tu profesor.
// Aquí irían las llamadas para listar, crear, editar y eliminar productos, por ejemplo.
public interface CRUDInterface<T> {

    @GET("all") // Endpoint de ejemplo para listar todo
    Call<List<T>> all();

    // Aquí podrías agregar:
    // @POST("create")
    // Call<T> create(@Body T t);
    //
    // @PUT("edit/{id}")
    // Call<T> edit(@Path("id") int id, @Body T t);
    //
    // @DELETE("delete/{id}")
    // Call<T> delete(@Path("id") int id);
}