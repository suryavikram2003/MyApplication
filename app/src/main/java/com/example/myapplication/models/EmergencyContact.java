package com.example.myapplication.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "emergency_contacts")
public class EmergencyContact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int userId;
    private String name;
    private String phoneNumber;
    private String relationship;
    private boolean isDefault;

    // Constructors
    public EmergencyContact() {}

    public EmergencyContact(int userId, String name, String phoneNumber, String relationship) {
        this.userId = userId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.relationship = relationship;
        this.isDefault = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }

    public boolean isDefault() { return isDefault; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
}