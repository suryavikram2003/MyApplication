package com.example.myapplication.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentReportBinding;
import com.example.myapplication.models.AccidentReport;
import com.example.myapplication.utils.LocationHelper;
import com.example.myapplication.viewmodels.ReportViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ReportFragment extends Fragment {
    private FragmentReportBinding binding;
    private ReportViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private double currentLatitude = 0.0;
    private double currentLongitude = 0.0;
    private String currentAddress = "";

    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentReportBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        setupSpinners();
        setupClickListeners();
        getCurrentLocation();
    }

    private void setupSpinners() {
        // Accident types
        String[] accidentTypes = {"Vehicle Accident", "Medical Emergency", "Fire", "Natural Disaster", "Crime", "Other"};
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, accidentTypes);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerAccidentType.setAdapter(typeAdapter);

        // Severity levels
        String[] severityLevels = {"Minor", "Moderate", "Severe", "Critical"};
        ArrayAdapter<String> severityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, severityLevels);
        severityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSeverity.setAdapter(severityAdapter);
    }

    private void setupClickListeners() {
        binding.btnGetLocation.setOnClickListener(v -> getCurrentLocation());

        binding.btnTakePhoto.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });

        binding.btnSubmitReport.setOnClickListener(v -> submitReport());

        binding.btnEmergencyCall.setOnClickListener(v -> makeEmergencyCall());
    }

    private void getCurrentLocation() {
        if (checkLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            currentLatitude = location.getLatitude();
                            currentLongitude = location.getLongitude();
                            getAddressFromLocation(location);
                            binding.tvLocation.setText(String.format("Location: %.6f, %.6f", currentLatitude, currentLongitude));
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireContext(), "Failed to get location", Toast.LENGTH_SHORT).show();
                    });
        } else {
            requestLocationPermission();
        }
    }

    private void getAddressFromLocation(Location location) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                currentAddress = address.getAddressLine(0);
                binding.tvAddress.setText("Address: " + currentAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void submitReport() {
        String title = binding.etTitle.getText().toString().trim();
        String description = binding.etDescription.getText().toString().trim();
        String accidentType = binding.spinnerAccidentType.getSelectedItem().toString();
        String severity = binding.spinnerSeverity.getSelectedItem().toString();

        if (validateInput(title, description)) {
            int userId = requireActivity().getSharedPreferences("app_prefs", 0).getInt("user_id", 0);

            AccidentReport report = new AccidentReport(userId, title, description, accidentType,
                    severity, currentLatitude, currentLongitude, currentAddress);

            viewModel.insertReport(report);

            Toast.makeText(requireContext(), "Report submitted successfully", Toast.LENGTH_SHORT).show();
            clearForm();
        }
    }

    private boolean validateInput(String title, String description) {
        if (title.isEmpty()) {
            binding.etTitle.setError("Title is required");
            return false;
        }
        if (description.isEmpty()) {
            binding.etDescription.setError("Description is required");
            return false;
        }
        if (currentLatitude == 0.0 && currentLongitude == 0.0) {
            Toast.makeText(requireContext(), "Please get your location first", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void clearForm() {
        binding.etTitle.setText("");
        binding.etDescription.setText("");
        binding.spinnerAccidentType.setSelection(0);
        binding.spinnerSeverity.setSelection(0);
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }

    private void makeEmergencyCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:911"));

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, retry the action
                getCurrentLocation();
            } else {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}