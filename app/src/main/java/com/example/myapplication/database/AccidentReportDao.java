package com.example.myapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.myapplication.models.AccidentReport;
import java.util.List;

@Dao
public interface AccidentReportDao {
    @Insert
    long insert(AccidentReport report);

    @Update
    void update(AccidentReport report);

    @Delete
    void delete(AccidentReport report);

    @Query("SELECT * FROM accident_reports WHERE id = :id")
    LiveData<AccidentReport> getReportById(int id);

    @Query("SELECT * FROM accident_reports WHERE userId = :userId ORDER BY timestamp DESC")
    LiveData<List<AccidentReport>> getReportsByUserId(int userId);

    @Query("SELECT * FROM accident_reports ORDER BY timestamp DESC")
    LiveData<List<AccidentReport>> getAllReports();

    @Query("SELECT * FROM accident_reports WHERE isSynced = 0")
    List<AccidentReport> getUnsyncedReports();

    @Query("UPDATE accident_reports SET isSynced = 1 WHERE id = :id")
    void markAsSynced(int id);
}