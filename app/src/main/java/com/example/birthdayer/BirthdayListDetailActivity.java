package com.example.birthdayer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class BirthdayListDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_list_detail);
        displayDetails();
    }

    private void displayDetails() {
        if (getIntent().hasExtra("selected_person")) {
            Calendar calendar = Calendar.getInstance();
            ListModel listModel = getIntent().getParcelableExtra("selected_person");
            TextView name = findViewById(R.id.name_detail);
            TextView age = findViewById(R.id.age_detail);
            TextView daysUntilBirthday = findViewById(R.id.days_detail);
            TextView location = findViewById(R.id.location_detail);
            CalendarView nextBirthday = findViewById(R.id.next_birthday_detail);

            name.setText(listModel.getName());
            age.setText(String.valueOf(listModel.getAge()));
            location.setText(listModel.getLocation());
            LocalDate nextBirthdayDate = calculateNextBirthday(listModel.getBirthday());
            daysUntilBirthday.setText(calculateDays(nextBirthdayDate));
            calendar.set(nextBirthdayDate.getYear(), nextBirthdayDate.getMonthValue() - 1, nextBirthdayDate.getDayOfMonth());
            long nextBirthdayLong = calendar.getTimeInMillis();
            nextBirthday.setDate(nextBirthdayLong, true, true);
        }
    }

    private String calculateDays(LocalDate nextBirthdayDate) {
        long days = ChronoUnit.DAYS.between(LocalDate.now(), nextBirthdayDate);
        return String.valueOf(days);
    }

    private LocalDate calculateNextBirthday(LocalDate birthday) {
        LocalDate now = LocalDate.now();
        LocalDate nextBirthday;
        if (now.isAfter(LocalDate.of(now.getYear(), birthday.getMonthValue(), birthday.getDayOfMonth()))) {
            nextBirthday = LocalDate.of(now.getYear() + 1, birthday.getMonthValue(), birthday.getDayOfMonth());
        } else {
            nextBirthday = LocalDate.of(now.getYear(), birthday.getMonthValue(), birthday.getDayOfMonth());
        }
        return nextBirthday;
    }
}