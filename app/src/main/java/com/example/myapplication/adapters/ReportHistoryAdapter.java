package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.databinding.ItemReportHistoryBinding;
import com.example.myapplication.models.AccidentReport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportHistoryAdapter extends ListAdapter<AccidentReport, ReportHistoryAdapter.ReportViewHolder> {

    public ReportHistoryAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<AccidentReport> DIFF_CALLBACK = new DiffUtil.ItemCallback<AccidentReport>() {
        @Override
        public boolean areItemsTheSame(@NonNull AccidentReport oldItem, @NonNull AccidentReport newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AccidentReport oldItem, @NonNull AccidentReport newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                   oldItem.getDescription().equals(newItem.getDescription()) &&
                   oldItem.getAccidentType().equals(newItem.getAccidentType()) &&
                   oldItem.getSeverity().equals(newItem.getSeverity());
        }
    };

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReportHistoryBinding binding = ItemReportHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ReportViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {
        private final ItemReportHistoryBinding binding;

        public ReportViewHolder(ItemReportHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AccidentReport report) {
            binding.tvTitle.setText(report.getTitle());
            binding.tvType.setText(report.getAccidentType());
            binding.tvSeverity.setText("Severity: " + report.getSeverity());
            
            // Format date
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
            binding.tvDate.setText(sdf.format(new Date(report.getCreatedAt())));
            
            // Format location
            if (report.getAddress() != null && !report.getAddress().isEmpty()) {
                binding.tvLocation.setText(report.getAddress());
            } else {
                binding.tvLocation.setText(String.format(Locale.getDefault(), 
                    "%.6f, %.6f", report.getLatitude(), report.getLongitude()));
            }
        }
    }
}