package com.example.microservicio.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.microservicio.R;
import com.example.microservicio.model.Product;
import com.example.microservicio.model.ProductVariant;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String PRODUCT_EXTRA = "product_extra";

    // --- Vistas de la UI ---
    private ImageView productImage;
    private TextView productPrice;
    private TextView productDescription;
    private ChipGroup chipGroupSizes;
    private ChipGroup chipGroupColors;
    private TextView textViewStock;
    private Toolbar toolbar;
    private TextView textViewSizesLabel;
    private TextView textViewColorsLabel;

    // --- Datos ---
    private Product currentProduct;
    private String selectedSize = null;
    private String selectedColor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initializeViews();

        currentProduct = getIntent().getParcelableExtra(PRODUCT_EXTRA);

        if (currentProduct != null) {
            populateInitialUI(currentProduct);
        } else {
            Toast.makeText(this, "Error al cargar el producto", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        productImage = findViewById(R.id.productImage);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        chipGroupSizes = findViewById(R.id.chipGroupSizes);
        chipGroupColors = findViewById(R.id.chipGroupColors);
        textViewStock = findViewById(R.id.textViewStock);
        // IDs corregidos para que coincidan con el nuevo layout
        textViewSizesLabel = findViewById(R.id.textViewSizesLabel);
        textViewColorsLabel = findViewById(R.id.textViewColorsLabel);
    }

    private void populateInitialUI(Product product) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(product.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
            Glide.with(this).load(product.getImageUrls().get(0)).placeholder(R.drawable.ic_placeholder).into(productImage);
        }

        productPrice.setText(String.format("S/ %.2f", product.getPrice()));
        productDescription.setText(product.getDescription());

        populateVariantChips();
    }

    private void populateVariantChips() {
        if (currentProduct.getVariants() == null || currentProduct.getVariants().isEmpty()) {
            // Si no hay variantes, ocultar todo y mostrar no disponible
            textViewSizesLabel.setVisibility(View.GONE);
            textViewColorsLabel.setVisibility(View.GONE);
            chipGroupSizes.setVisibility(View.GONE);
            chipGroupColors.setVisibility(View.GONE);
            textViewStock.setText("No disponible");
            return;
        }

        // --- LÓGICA CLAVE: Manejar productos con UNA SOLA variante (accesorios) ---
        if (currentProduct.getVariants().size() == 1) {
            ProductVariant singleVariant = currentProduct.getVariants().get(0);
            textViewStock.setText(String.valueOf(singleVariant.getStock()));

            // Ocultar las opciones de Talla y Color ya que no son necesarias
            textViewSizesLabel.setVisibility(View.GONE);
            textViewColorsLabel.setVisibility(View.GONE);
            chipGroupSizes.setVisibility(View.GONE);
            chipGroupColors.setVisibility(View.GONE);
            return; // Salimos del método, no hay más que hacer
        }

        // --- Lógica para productos con MÚLTIPLES variantes ---
        Set<String> allColors = currentProduct.getVariants().stream()
                .map(ProductVariant::getColor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Set<String> allSizes = currentProduct.getVariants().stream()
                .map(ProductVariant::getSize)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        chipGroupColors.removeAllViews();
        for (String color : allColors) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, chipGroupColors, false);
            chip.setText(color);
            chip.setOnClickListener(v -> {
                selectedColor = color;
                updateSelectedVariantUI();
            });
            chipGroupColors.addView(chip);
        }

        chipGroupSizes.removeAllViews();
        for (String size : allSizes) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_choice, chipGroupSizes, false);
            chip.setText(size);
            chip.setOnClickListener(v -> {
                selectedSize = size;
                updateSelectedVariantUI();
            });
            chipGroupSizes.addView(chip);
        }
    }

    private void updateSelectedVariantUI() {
        if (selectedSize == null || selectedColor == null) {
            textViewStock.setText("-");
            return;
        }

        ProductVariant selectedVariant = findVariant(selectedSize, selectedColor);

        if (selectedVariant != null) {
            if (selectedVariant.getImageUrl() != null && !selectedVariant.getImageUrl().isEmpty()) {
                Glide.with(this).load(selectedVariant.getImageUrl()).placeholder(R.drawable.ic_placeholder).into(productImage);
            }
            textViewStock.setText(String.valueOf(selectedVariant.getStock()));
        } else {
            textViewStock.setText("No disponible");
        }
    }

    private ProductVariant findVariant(String size, String color) {
        for (ProductVariant variant : currentProduct.getVariants()) {
            if (Objects.equals(variant.getSize(), size) && Objects.equals(variant.getColor(), color)) {
                return variant;
            }
        }
        return null;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}