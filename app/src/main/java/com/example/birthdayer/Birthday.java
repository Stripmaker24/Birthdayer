package com.example.birthdayer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "birthday")
public class Birthday {
    @PrimaryKey(autoGenerate = true)
    protected int id;
    @ColumnInfo(name = "name")
    protected String name;
    @ColumnInfo(name = "date")
    protected Long birthDate;
    @ColumnInfo(name = "age")
    protected int age;
    @ColumnInfo(name = "daysUntil")
    protected int daysUntil;
    @ColumnInfo(name = "address")
    protected String address;

    public Birthday(int id, String name, Long birthDate, int age, int daysUntil, String address){
        this.id = id;
        this.name= name;
        this.birthDate = birthDate;
        this.age = age;
        this.daysUntil = daysUntil;
        this.address = address;
    }

    @Ignore
    public Birthday(String name, Long birthday, String address) {
        this.name = name;
        this.birthDate = birthday;
        this.address = address;
    }
}
