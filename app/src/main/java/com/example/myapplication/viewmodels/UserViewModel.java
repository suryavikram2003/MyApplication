package com.example.myapplication.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myapplication.models.User;
import com.example.myapplication.repositories.AppRepository;

public class UserViewModel extends AndroidViewModel {
    private AppRepository repository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public LiveData<User> getUserById(int id) {
        return repository.getUserById(id);
    }

    public void insertUser(User user) {
        repository.insertUser(user, null);
    }
}