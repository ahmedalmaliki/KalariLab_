package com.example.kalarilab.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AdminDao {
    @Insert
    void insert(AdminEntry adminEntry);
    @Update
    void update(AdminEntry adminEntry);

    @Query("DELETE From  Admins")
    void deleteAdminsList();
    @Query("SELECT * From Admins ")
    LiveData<List<AdminEntry>> getAdminsList();
}
