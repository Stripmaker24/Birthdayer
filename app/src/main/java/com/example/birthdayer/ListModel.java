package com.example.birthdayer;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoField;

public class ListModel implements Parcelable {
    String name = "";
    int age = 0;
    LocalDate birthday;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    String location = "";

    public ListModel(String name, LocalDate birthday, String location) {
        setName(name);
        setBirthday(birthday);
        setLocation(location);
    }

    protected ListModel(Parcel in) {
        name = in.readString();
        age = in.readInt();
        birthday = LocalDate.ofEpochDay(in.readLong());
        location = in.readString();
    }

    public static final Creator<ListModel> CREATOR = new Creator<ListModel>() {
        @Override
        public ListModel createFromParcel(Parcel in) {
            return new ListModel(in);
        }

        @Override
        public ListModel[] newArray(int size) {
            return new ListModel[size];
        }
    };

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        setAge(birthday);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(LocalDate birthday) {
        this.age = Period.between(birthday, LocalDate.now()).getYears();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(age);
        parcel.writeLong(birthday.getLong(ChronoField.EPOCH_DAY));
        parcel.writeString(location);
    }
}
