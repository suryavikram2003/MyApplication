package com.example.myapplication;

import android.app.Application;
import com.example.myapplication.database.AppDatabase;

public class AccidentReportApplication extends Application {
    private static AccidentReportApplication instance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = AppDatabase.getInstance(this);
    }

    public static AccidentReportApplication getInstance() {
        return instance;
    }

    public AppDatabase getDatabase() {
        return database;
    }
}