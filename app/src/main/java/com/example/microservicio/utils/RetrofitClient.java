package com.example.microservicio.utils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // 1. URLs base para cada microservicio
    public static final String USER_SERVICE_BASE_URL = "http://10.0.2.2:8080/";
    public static final String INVENTORY_SERVICE_BASE_URL = "http://10.0.2.2:9001/";
    // ... aquí puedes añadir más URLs en el futuro

    // 2. Caché para las instancias de Retrofit
    private static final Map<String, Retrofit> instances = new HashMap<>();

    // 3. Un único cliente OkHttp con el logger
    // Lo creamos una sola vez y lo reutilizamos para todas las instancias de Retrofit.
    private static final OkHttpClient httpClient = createOkHttpClient();

    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }

    /**
     * Obtiene una instancia de Retrofit para una URL base específica.
     * @param baseUrl La URL del microservicio.
     * @return Una instancia de Retrofit.
     */
    private static Retrofit getClient(String baseUrl) {
        if (instances.containsKey(baseUrl)) {
            return instances.get(baseUrl);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient) // Usamos el cliente OkHttp compartido
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        instances.put(baseUrl, retrofit);
        return retrofit;
    }

    /**
     * Método genérico para crear cualquier servicio de API.
     * @param serviceClass La clase de la interfaz de la API (ej. ProductApi.class).
     * @param baseUrl La URL base del microservicio al que pertenece la API.
     * @return Una implementación de la interfaz de la API.
     */
    public static <T> T createService(Class<T> serviceClass, String baseUrl) {
        return getClient(baseUrl).create(serviceClass);
    }
}