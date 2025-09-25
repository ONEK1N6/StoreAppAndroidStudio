package com.example.microservicio.interfaces;

import com.example.microservicio.model.Category;
import com.example.microservicio.model.Product;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {

    // --- Category Endpoints ---

    @GET("api/categories")
    Call<List<Category>> getAllCategories();

    @POST("api/categories")
    Call<Category> createCategory(@Body Category category);


    // --- Product Endpoints ---

    @GET("api/products")
    Call<List<Product>> getAllProducts();

    @GET("api/products/category/{id}")
    Call<List<Product>> getProductsByCategory(@Path("id") Long categoryId);

    @POST("api/products")
    Call<Product> createProduct(@Body Product product);

    @PATCH("api/products/variants/{variantId}/stock")
    Call<Void> updateStock(@Path("variantId") Long variantId, @Body Map<String, Integer> body);
}
