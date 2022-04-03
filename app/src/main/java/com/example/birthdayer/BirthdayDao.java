package com.example.birthdayer;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BirthdayDao {
    @Query("SELECT * FROM birthday")
    List<Birthday> getBirthdayList();
    @Insert
    void insertBirthday(Birthday birthday);
    @Update
    void updateBirthday(Birthday birthday);
    @Delete
    void deleteBirthday(Birthday birthday);
    @Query("SELECT address FROM birthday")
    List<String> getBirthdayLocations();
    @Query("DELETE FROM birthday")
    void emptyTable();
}
