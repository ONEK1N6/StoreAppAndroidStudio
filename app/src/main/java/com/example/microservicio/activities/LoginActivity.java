package com.example.microservicio.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.example.microservicio.R;
import com.example.microservicio.dto.AuthResponse;
import com.example.microservicio.dto.LoginRequest;
import com.example.microservicio.interfaces.AuthInterface;
import com.example.microservicio.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;

    // ✅ CORREGIDO: Interfaz para llamadas a la API
    private AuthInterface authInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Se enlazan los componentes de la vista (XML) con las variables de Java.
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        // ✅ CORREGIDO: Asignar a authInterface, no a una variable local
        authInterface = RetrofitClient.createService(
                AuthInterface.class,
                RetrofitClient.USER_SERVICE_BASE_URL
        );

        // Se configura el listener para el botón de login.
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        // Listener para el texto de registro
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí iría la lógica para navegar a la pantalla de registro.
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser() {
        // ✅ CAMBIADO: Ahora capturamos como "username" en lugar de "email"
        String username = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validaciones básicas de los campos de entrada.
        if (username.isEmpty()) {
            editTextEmail.setError("El usuario es requerido");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("La contraseña es requerida");
            editTextPassword.requestFocus();
            return;
        }

        // ✅ AGREGADO: CONEXIÓN REAL CON EL BACKEND
        Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show();

        // Crear el objeto LoginRequest con username (NO email)
        LoginRequest loginRequest = new LoginRequest(username, password);

        // ✅ CORREGIDO: Usar authInterface (ya no es null)
        Call<AuthResponse> call = authInterface.login(loginRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    if (authResponse != null) {
                        // ✅ Login exitoso
                        String token = authResponse.getAccessToken();
                        String user = authResponse.getUsername();

                        Toast.makeText(LoginActivity.this,
                                "Bienvenido " + user, Toast.LENGTH_SHORT).show();

                        // ✅ CAMBIADO: Navegar a InventoryActivity en lugar de HomeActivity
                        Intent inventoryIntent = new Intent(LoginActivity.this, InventoryActivity.class);
                        // Opcional: pasar el token y datos del usuario
                        inventoryIntent.putExtra("token", token);
                        inventoryIntent.putExtra("username", user);
                        startActivity(inventoryIntent);
                        finish(); // Cerrar LoginActivity
                    }
                } else {
                    // Error en la autenticación (401, 400, etc.)
                    String errorMessage = "Credenciales incorrectas";
                    try {
                        if (response.errorBody() != null) {
                            errorMessage = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // Error de conexión (sin internet, servidor caído, etc.)
                Toast.makeText(LoginActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}