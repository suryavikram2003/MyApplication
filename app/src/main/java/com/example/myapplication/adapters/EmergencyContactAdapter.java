package com.example.myapplication.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.databinding.ItemEmergencyContactBinding;
import com.example.myapplication.models.EmergencyContact;

public class EmergencyContactAdapter extends ListAdapter<EmergencyContact, EmergencyContactAdapter.ContactViewHolder> {

    public EmergencyContactAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<EmergencyContact> DIFF_CALLBACK = new DiffUtil.ItemCallback<EmergencyContact>() {
        @Override
        public boolean areItemsTheSame(@NonNull EmergencyContact oldItem, @NonNull EmergencyContact newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull EmergencyContact oldItem, @NonNull EmergencyContact newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                   oldItem.getPhoneNumber().equals(newItem.getPhoneNumber());
        }
    };

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEmergencyContactBinding binding = ItemEmergencyContactBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ContactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder {
        private final ItemEmergencyContactBinding binding;

        public ContactViewHolder(ItemEmergencyContactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(EmergencyContact contact) {
            binding.tvName.setText(contact.getName());
            binding.tvPhone.setText(contact.getPhoneNumber());
            
            binding.btnCall.setOnClickListener(v -> {
                try {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
                    v.getContext().startActivity(callIntent);
                } catch (SecurityException e) {
                    // If CALL_PHONE permission is not granted, use ACTION_DIAL instead
                    Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                    dialIntent.setData(Uri.parse("tel:" + contact.getPhoneNumber()));
                    v.getContext().startActivity(dialIntent);
                }
            });
        }
    }
}