package com.example.myapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.databinding.ActivityRegisterBinding;
import com.example.myapplication.models.User;
import com.example.myapplication.repositories.AppRepository;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private AppRepository repository;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new AppRepository(getApplication());
        sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnRegister.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString().trim();
            String email = binding.etEmail.getText().toString().trim();
            String phone = binding.etPhone.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

            if (validateInput(username, email, phone, password, confirmPassword)) {
                performRegistration(username, email, phone, password);
            }
        });

        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private boolean validateInput(String username, String email, String phone, String password, String confirmPassword) {
        if (username.isEmpty()) {
            binding.etUsername.setError("Username is required");
            return false;
        }
        if (email.isEmpty()) {
            binding.etEmail.setError("Email is required");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.setError("Invalid email format");
            return false;
        }
        if (phone.isEmpty()) {
            binding.etPhone.setError("Phone number is required");
            return false;
        }
        if (password.isEmpty()) {
            binding.etPassword.setError("Password is required");
            return false;
        }
        if (password.length() < 6) {
            binding.etPassword.setError("Password must be at least 6 characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            binding.etConfirmPassword.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    private void performRegistration(String username, String email, String phone, String password) {
        binding.btnRegister.setEnabled(false);

        User user = new User(username, email, phone, password);
        
        repository.insertUser(user, userId -> {
            runOnUiThread(() -> {
                binding.btnRegister.setEnabled(true);

                if (userId > 0) {
                    // Save user session
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("user_id", (int) userId);
                    editor.putString("username", username);
                    editor.putBoolean("is_logged_in", true);
                    editor.apply();

                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();

                    // Navigate to main activity
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}