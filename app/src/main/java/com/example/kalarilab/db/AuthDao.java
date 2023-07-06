package com.example.kalarilab.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao

public interface AuthDao {
    @Insert
    void insert(AuthEntry authEntry);
    @Update
    void update(AuthEntry authEntry);

    @Query("SELECT * FROM User WHERE `index` = (SELECT MAX(`index`) FROM User) ")
    LiveData<AuthEntry> getAuthData();
    @Query("DELETE From  User")
    void deleteAuthData();
}
