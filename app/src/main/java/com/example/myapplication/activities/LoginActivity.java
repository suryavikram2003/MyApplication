package com.example.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.databinding.ActivityLoginBinding;
import com.example.myapplication.models.User;
import com.example.myapplication.repositories.AppRepository;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AppRepository repository;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (validateInput(username, password)) {
                performLogin(username, password);
            }
        });

        binding.tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private boolean validateInput(String username, String password) {
        if (username.isEmpty()) {
            binding.etUsername.setError("Username is required");
            return false;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            return false;
        }
        return true;
    }

    private void performLogin(String username, String password) {
        binding.btnLogin.setEnabled(false);

        repository.loginUser(username, password, user -> {
            runOnUiThread(() -> {
                binding.btnLogin.setEnabled(true);

                if (user != null) {
                    // Save user session
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("user_id", user.getId());
                    editor.putString("username", user.getUsername());
                    editor.putBoolean("is_logged_in", true);
                    editor.apply();

                    // Navigate to main activity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}