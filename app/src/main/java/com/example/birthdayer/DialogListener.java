package com.example.birthdayer;

import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;

public interface DialogListener {
    public void onDialogPositiveClick(String name, LocalDate date);
}
