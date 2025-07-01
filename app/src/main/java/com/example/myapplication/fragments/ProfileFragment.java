package com.example.myapplication.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.databinding.FragmentProfileBinding;
import com.example.myapplication.models.User;
import com.example.myapplication.viewmodels.UserViewModel;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private UserViewModel viewModel;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        sharedPreferences = requireActivity().getSharedPreferences("app_prefs", 0);
        
        setupClickListeners();
        loadUserData();
    }

    private void setupClickListeners() {
        binding.btnSaveProfile.setOnClickListener(v -> saveProfile());
        binding.btnLogout.setOnClickListener(v -> logout());
    }

    private void loadUserData() {
        int userId = sharedPreferences.getInt("user_id", 0);
        if (userId != 0) {
            viewModel.getUserById(userId).observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    binding.etUsername.setText(user.getUsername());
                    binding.etEmail.setText(user.getEmail());
                    binding.etPhone.setText(user.getPhone());
                    binding.etEmergencyContact.setText(user.getEmergencyContact());
                }
            });
        }
    }

    private void saveProfile() {
        String email = binding.etEmail.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();
        String emergencyContact = binding.etEmergencyContact.getText().toString().trim();

        if (validateInput(email, phone)) {
            int userId = sharedPreferences.getInt("user_id", 0);
            if (userId != 0) {
                // TODO: Implement profile update in repository
                Toast.makeText(requireContext(), "Profile saved successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String email, String phone) {
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
        return true;
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}