package com.example.microservicio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.microservicio.R;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Necesitarás crear este layout

        // Inicializar vistas
        tvWelcome = findViewById(R.id.tv_welcome);
        btnLogout = findViewById(R.id.btn_logout);

        // Obtener datos del Intent (enviados desde LoginActivity)
        String token = getIntent().getStringExtra("token");
        String username = getIntent().getStringExtra("username");

        // Mostrar mensaje de bienvenida
        if (username != null) {
            tvWelcome.setText("¡Bienvenido, " + username + "!");
        }

        // Configurar botón de logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        // Limpiar datos de sesión si es necesario
        // Regresar a LoginActivity
        Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}