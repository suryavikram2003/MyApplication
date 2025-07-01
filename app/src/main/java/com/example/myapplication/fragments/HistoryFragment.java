package com.example.myapplication.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.myapplication.adapters.ReportHistoryAdapter;
import com.example.myapplication.databinding.FragmentHistoryBinding;
import com.example.myapplication.viewmodels.ReportViewModel;

public class HistoryFragment extends Fragment {
    private FragmentHistoryBinding binding;
    private ReportViewModel viewModel;
    private ReportHistoryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ReportViewModel.class);
        setupRecyclerView();
        observeReports();
    }

    private void setupRecyclerView() {
        adapter = new ReportHistoryAdapter();
        binding.recyclerReports.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerReports.setAdapter(adapter);
    }

    private void observeReports() {
        int userId = requireActivity().getSharedPreferences("app_prefs", 0).getInt("user_id", 0);
        if (userId != 0) {
            viewModel.getReportsByUserId(userId).observe(getViewLifecycleOwner(), reports -> {
                if (reports != null && !reports.isEmpty()) {
                    adapter.submitList(reports);
                    binding.recyclerReports.setVisibility(View.VISIBLE);
                    binding.tvNoReports.setVisibility(View.GONE);
                } else {
                    binding.recyclerReports.setVisibility(View.GONE);
                    binding.tvNoReports.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}