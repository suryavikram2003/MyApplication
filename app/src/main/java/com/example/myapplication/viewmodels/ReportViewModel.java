package com.example.myapplication.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.myapplication.models.AccidentReport;
import com.example.myapplication.repositories.AppRepository;
import java.util.List;

public class ReportViewModel extends AndroidViewModel {
    private AppRepository repository;

    public ReportViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public void insertReport(AccidentReport report) {
        repository.insertAccidentReport(report, null);
    }

    public LiveData<List<AccidentReport>> getReportsByUserId(int userId) {
        return repository.getReportsByUserId(userId);
    }
}