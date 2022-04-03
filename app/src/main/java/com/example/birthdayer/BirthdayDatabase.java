package com.example.birthdayer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Birthday.class, exportSchema = false, version = 1)
public abstract class BirthdayDatabase extends RoomDatabase {
    private static final String DB_NAME = "birthday_db";
    private static BirthdayDatabase instance;

    public static synchronized BirthdayDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), BirthdayDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract BirthdayDao birthdayDao();
}
