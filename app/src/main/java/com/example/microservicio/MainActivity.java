package com.example.microservicio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.microservicio.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ CORREGIDO: Usar Handler con Looper.getMainLooper() (más moderno y seguro)
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Verificar que la activity no haya sido destruida
                if (!isFinishing() && !isDestroyed()) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    // ✅ AGREGADO: Flags para limpiar el stack de activities
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000); // ✅ CAMBIADO: 2 segundos para dar tiempo a que cargue todo
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpiar cualquier callback pendiente si es necesario
    }
}