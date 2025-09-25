package com.example.microservicio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.microservicio.R;
import com.example.microservicio.model.Category;
import com.example.microservicio.model.Product;

import java.util.List;
import java.util.Locale;

public class ShopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // CORRECCIÓN: Cambiado de private a public
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_PRODUCT = 1;

    private final List<Object> items;
    private final Context context;
    private final OnProductClickListener productClickListener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ShopAdapter(Context context, List<Object> items, OnProductClickListener listener) {
        this.context = context;
        this.items = items;
        this.productClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Category) {
            return TYPE_HEADER;
        } else {
            return TYPE_PRODUCT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
            return new ProductViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            Category category = (Category) items.get(position);
            ((HeaderViewHolder) holder).bind(category);
        } else {
            Product product = (Product) items.get(position);
            ((ProductViewHolder) holder).bind(product);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    // ViewHolder para el encabezado de la categoría
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategoryHeader;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoryHeader = itemView.findViewById(R.id.textViewCategoryHeader);
        }

        void bind(Category category) {
            textViewCategoryHeader.setText(category.getName());
        }
    }

    // ViewHolder para el producto
    class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewProductName;
        TextView textViewProductPrice;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
        }

        void bind(final Product product) {
            textViewProductName.setText(product.getName());
            textViewProductPrice.setText(String.format(Locale.getDefault(), "S/ %.2f", product.getPrice()));

            // Usar Glide para cargar la imagen
            if (product.getImageUrls() != null && !product.getImageUrls().isEmpty()) {
                Glide.with(context)
                        .load(product.getImageUrls().get(0)) // Cargar la primera imagen
                        .placeholder(R.drawable.ic_placeholder)
                        .into(imageViewProduct);
            } else {
                // Si no hay imagen, mostrar el placeholder
                Glide.with(context).clear(imageViewProduct);
                imageViewProduct.setImageResource(R.drawable.ic_placeholder);
            }

            itemView.setOnClickListener(v -> {
                if (productClickListener != null) {
                    productClickListener.onProductClick(product);
                }
            });
        }
    }
}
