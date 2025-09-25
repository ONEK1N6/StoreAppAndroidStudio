package com.example.microservicio.activities;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.microservicio.R;
import com.example.microservicio.dto.RegistroDto;
import com.example.microservicio.interfaces.AuthInterface;
import com.example.microservicio.model.Usuario;
import com.example.microservicio.utils.RetrofitClient;
import com.google.android.material.textfield.TextInputEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText editTextUsername;
    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Button btnRegister;

    private AuthInterface authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();

        // Inicializar el servicio de autenticación
        authService = RetrofitClient.createService(AuthInterface.class, RetrofitClient.USER_SERVICE_BASE_URL);

        btnRegister.setOnClickListener(v -> attemptRegister());
    }

    private void initializeViews() {
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void attemptRegister() {
        String username = editTextUsername.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        RegistroDto registroDto = new RegistroDto(username, email, password);

        authService.register(registroDto).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(@NonNull Call<Usuario> call, @NonNull Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, "¡Registro exitoso! Ya puedes iniciar sesión.", Toast.LENGTH_LONG).show();
                    finish(); // Cierra la actividad de registro y vuelve al login
                } else {
                    // Manejar errores de la API (ej. usuario ya existe)
                    Toast.makeText(RegisterActivity.this, "Error en el registro: " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Usuario> call, @NonNull Throwable t) {
                // Manejar errores de conexión
                Toast.makeText(RegisterActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}