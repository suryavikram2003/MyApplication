package com.example.myapplication.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.myapplication.models.User;
import com.example.myapplication.models.AccidentReport;
import com.example.myapplication.models.EmergencyContact;

@Database(
        entities = {User.class, AccidentReport.class, EmergencyContact.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract AccidentReportDao accidentReportDao();
    public abstract EmergencyContactDao emergencyContactDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "accident_report_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}