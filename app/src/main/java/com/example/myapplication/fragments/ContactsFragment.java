package com.example.myapplication.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.myapplication.adapters.EmergencyContactAdapter;
import com.example.myapplication.databinding.FragmentContactsBinding;
import com.example.myapplication.viewmodels.ContactViewModel;

public class ContactsFragment extends Fragment {
    private FragmentContactsBinding binding;
    private ContactViewModel viewModel;
    private EmergencyContactAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        setupRecyclerView();
        setupClickListeners();
        observeContacts();
    }

    private void setupRecyclerView() {
        adapter = new EmergencyContactAdapter();
        binding.recyclerContacts.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerContacts.setAdapter(adapter);
    }

    private void setupClickListeners() {
        binding.btnCall911.setOnClickListener(v -> makeCall("911"));
        binding.btnCallPolice.setOnClickListener(v -> makeCall("911"));
        binding.btnCallFire.setOnClickListener(v -> makeCall("911"));
        binding.btnCallMedical.setOnClickListener(v -> makeCall("911"));
        
        binding.fabAddContact.setOnClickListener(v -> {
            // TODO: Implement add contact dialog
            Toast.makeText(requireContext(), "Add contact functionality coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void observeContacts() {
        int userId = requireActivity().getSharedPreferences("app_prefs", 0).getInt("user_id", 0);
        if (userId != 0) {
            viewModel.getContactsByUserId(userId).observe(getViewLifecycleOwner(), contacts -> {
                if (contacts != null) {
                    adapter.submitList(contacts);
                }
            });
        }
    }

    private void makeCall(String phoneNumber) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        } catch (SecurityException e) {
            // If CALL_PHONE permission is not granted, use ACTION_DIAL instead
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(dialIntent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}