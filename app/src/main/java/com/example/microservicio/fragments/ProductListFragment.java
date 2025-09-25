package com.example.microservicio.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.microservicio.R;
import com.example.microservicio.activities.ProductDetailActivity;
import com.example.microservicio.adapters.ShopAdapter;
import com.example.microservicio.interfaces.ProductApi;
import com.example.microservicio.model.Category;
import com.example.microservicio.model.Product;
import com.example.microservicio.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductListFragment extends Fragment implements ShopAdapter.OnProductClickListener {

    private static final String TAG = "ProductListFragment";

    // UI Components
    private RecyclerView recyclerViewShop;
    private ProgressBar progressBar;
    private SearchView searchView;

    // Adapter
    private ShopAdapter shopAdapter;

    // Data
    private final List<Object> shopList = new ArrayList<>(); // Lista que contiene Categorías y Productos
    private final List<Object> originalShopList = new ArrayList<>(); // Para el filtro de búsqueda

    // Service
    private ProductApi productApi;

    public ProductListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize API Service
        productApi = RetrofitClient.createService(ProductApi.class, RetrofitClient.INVENTORY_SERVICE_BASE_URL);

        // Find views by ID
        recyclerViewShop = view.findViewById(R.id.recyclerViewShop);
        progressBar = view.findViewById(R.id.progressBar);
        searchView = view.findViewById(R.id.searchView);

        // Setup UI
        setupRecyclerView();
        setupSearchView();

        // Load data from API
        Log.d(TAG, "onViewCreated: Cargando datos de la tienda...");
        loadShopData();
    }

    private void setupRecyclerView() {
        shopAdapter = new ShopAdapter(getContext(), shopList, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (shopAdapter.getItemViewType(position) == ShopAdapter.TYPE_HEADER) {
                    return 2; // Ocupa 2 columnas (ancho completo)
                }
                return 1; // Ocupa 1 columna
            }
        });

        recyclerViewShop.setLayoutManager(layoutManager);
        recyclerViewShop.setAdapter(shopAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void loadShopData() {
        showProgressBar(true);
        productApi.getAllProducts().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                showProgressBar(false);
                if (response.isSuccessful() && response.body() != null) {
                    // ¡LOG AÑADIDO!
                    Log.d(TAG, "onResponse: Éxito. Productos recibidos: " + response.body().size());
                    organizeData(response.body());
                } else {
                    // ¡LOG AÑADIDO!
                    Log.e(TAG, "onResponse: Error, pero la llamada fue exitosa. Código: " + response.code());
                    handleApiError("Error al cargar los productos (código: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                // ¡LOG AÑADIDO!
                Log.e(TAG, "onFailure: Fallo en la conexión.", t);
                handleApiError("Fallo en la conexión: " + t.getMessage());
            }
        });
    }

    private void organizeData(List<Product> products) {
        Map<Category, List<Product>> groupedData = new LinkedHashMap<>();
        for (Product product : products) {
            Category category = product.getCategory();
            // ¡LOG AÑADIDO! Para verificar si las categorías están llegando
            if (category == null) {
                Log.w(TAG, "organizeData: Producto con ID " + product.getId() + " tiene categoría NULL.");
                continue; // Saltar este producto si no tiene categoría
            }

            List<Product> productList = groupedData.get(category);
            if (productList == null) {
                productList = new ArrayList<>();
                groupedData.put(category, productList);
            }
            productList.add(product);
        }

        shopList.clear();
        originalShopList.clear();
        for (Map.Entry<Category, List<Product>> entry : groupedData.entrySet()) {
            shopList.add(entry.getKey());
            shopList.addAll(entry.getValue());
        }

        originalShopList.addAll(shopList);
        shopAdapter.notifyDataSetChanged();

        // ¡LOG AÑADIDO! El log final y más importante
        Log.d(TAG, "organizeData: Proceso finalizado. Total de items en la lista (headers + products): " + shopList.size());
    }

    private void filter(String text) {
        shopList.clear();
        if (text.isEmpty()) {
            shopList.addAll(originalShopList);
        } else {
            text = text.toLowerCase();
            Category currentCategory = null;
            List<Product> filteredProducts = new ArrayList<>();

            for (Object item : originalShopList) {
                if (item instanceof Category) {
                    if (currentCategory != null && !filteredProducts.isEmpty()) {
                        shopList.add(currentCategory);
                        shopList.addAll(filteredProducts);
                    }
                    currentCategory = (Category) item;
                    filteredProducts.clear();
                } else if (item instanceof Product) {
                    Product product = (Product) item;
                    if (product.getName().toLowerCase().contains(text)) {
                        filteredProducts.add(product);
                    }
                }
            }
            if (currentCategory != null && !filteredProducts.isEmpty()) {
                shopList.add(currentCategory);
                shopList.addAll(filteredProducts);
            }
        }
        shopAdapter.notifyDataSetChanged();
    }

    private void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void handleApiError(String message) {
        showProgressBar(false);
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProductClick(Product product) {
        // 1. Crea un Intent para ir a la pantalla de detalle.
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);

        // 2. "Empaqueta" el objeto del producto en el Intent para enviarlo a la otra actividad.
        //    Usamos una constante de ProductDetailActivity para la clave, es una buena práctica.
        intent.putExtra(ProductDetailActivity.PRODUCT_EXTRA, product);

        // 3. Inicia la nueva actividad.
        startActivity(intent);
    }
}