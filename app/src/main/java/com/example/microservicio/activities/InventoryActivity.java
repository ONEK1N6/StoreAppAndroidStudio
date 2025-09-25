package com.example.microservicio.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.microservicio.R;
import com.example.microservicio.fragments.ProductListFragment;

public class InventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Si es la primera vez que se crea la actividad, agregamos el fragmento principal
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProductListFragment())
                    .commit();
        }
    }
}
