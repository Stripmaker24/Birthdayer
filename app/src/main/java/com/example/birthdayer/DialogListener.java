package com.example.birthdayer;

import java.time.LocalDate;

public interface DialogListener {
    public void onDialogPositiveClick(String name, LocalDate date, String locationStr);
}
