package com.example.myapplication.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myapplication.models.EmergencyContact;
import com.example.myapplication.repositories.AppRepository;
import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private AppRepository repository;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public void insertContact(EmergencyContact contact) {
        repository.insertEmergencyContact(contact, null);
    }

    public LiveData<List<EmergencyContact>> getContactsByUserId(int userId) {
        return repository.getContactsByUserId(userId);
    }
}