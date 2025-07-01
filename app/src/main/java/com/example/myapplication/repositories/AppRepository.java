package com.example.myapplication.repositories;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.myapplication.database.AppDatabase;
import com.example.myapplication.database.UserDao;
import com.example.myapplication.database.AccidentReportDao;
import com.example.myapplication.database.EmergencyContactDao;
import com.example.myapplication.models.User;
import com.example.myapplication.models.AccidentReport;
import com.example.myapplication.models.EmergencyContact;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppRepository {
    private UserDao userDao;
    private AccidentReportDao accidentReportDao;
    private EmergencyContactDao emergencyContactDao;
    private ExecutorService executor;

    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDao = database.userDao();
        accidentReportDao = database.accidentReportDao();
        emergencyContactDao = database.emergencyContactDao();
        executor = Executors.newFixedThreadPool(4);
    }

    // User operations
    public void insertUser(User user, OnUserInsertedCallback callback) {
        executor.execute(() -> {
            long id = userDao.insert(user);
            if (callback != null) {
                callback.onUserInserted(id);
            }
        });
    }

    public void loginUser(String username, String password, OnLoginCallback callback) {
        executor.execute(() -> {
            User user = userDao.login(username, password);
            if (callback != null) {
                callback.onLoginResult(user);
            }
        });
    }

    public LiveData<User> getUserById(int id) {
        return userDao.getUserById(id);
    }

    // Accident Report operations
    public void insertAccidentReport(AccidentReport report, OnReportInsertedCallback callback) {
        executor.execute(() -> {
            long id = accidentReportDao.insert(report);
            if (callback != null) {
                callback.onReportInserted(id);
            }
        });
    }

    public LiveData<List<AccidentReport>> getReportsByUserId(int userId) {
        return accidentReportDao.getReportsByUserId(userId);
    }

    // Emergency Contact operations
    public void insertEmergencyContact(EmergencyContact contact, OnContactInsertedCallback callback) {
        executor.execute(() -> {
            long id = emergencyContactDao.insert(contact);
            if (callback != null) {
                callback.onContactInserted(id);
            }
        });
    }

    public LiveData<List<EmergencyContact>> getContactsByUserId(int userId) {
        return emergencyContactDao.getContactsByUserId(userId);
    }

    // Callback interfaces
    public interface OnUserInsertedCallback {
        void onUserInserted(long userId);
    }

    public interface OnLoginCallback {
        void onLoginResult(User user);
    }

    public interface OnReportInsertedCallback {
        void onReportInserted(long reportId);
    }

    public interface OnContactInsertedCallback {
        void onContactInserted(long contactId);
    }
}