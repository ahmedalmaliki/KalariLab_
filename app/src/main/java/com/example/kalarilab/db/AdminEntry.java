package com.example.kalarilab.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Admins")
public class AdminEntry {
   @PrimaryKey(autoGenerate = true)
    private int id;
    private String email;

    public AdminEntry( String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }



    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }
}
