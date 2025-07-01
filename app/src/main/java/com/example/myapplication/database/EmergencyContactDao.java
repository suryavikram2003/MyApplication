package com.example.myapplication.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.myapplication.models.EmergencyContact;
import java.util.List;

@Dao
public interface EmergencyContactDao {
    @Insert
    long insert(EmergencyContact contact);

    @Update
    void update(EmergencyContact contact);

    @Delete
    void delete(EmergencyContact contact);

    @Query("SELECT * FROM emergency_contacts WHERE id = :id")
    LiveData<EmergencyContact> getContactById(int id);

    @Query("SELECT * FROM emergency_contacts WHERE userId = :userId")
    LiveData<List<EmergencyContact>> getContactsByUserId(int userId);

    @Query("SELECT * FROM emergency_contacts WHERE userId = :userId AND isDefault = 1")
    LiveData<EmergencyContact> getDefaultContact(int userId);

    @Query("UPDATE emergency_contacts SET isDefault = 0 WHERE userId = :userId")
    void clearDefaultContacts(int userId);
}