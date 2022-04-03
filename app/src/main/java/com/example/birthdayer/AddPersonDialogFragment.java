package com.example.birthdayer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class AddPersonDialogFragment extends DialogFragment {
    DialogListener listener;
    View dialogView;
    DateTimeFormatter formatter;
    AlertDialog alertDialog;

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_person_dialog_fragment, null);
        EditText name = dialogView.findViewById(R.id.add_person_dialog_name);
        EditText date = dialogView.findViewById(R.id.add_person_dialog_birthday);
        EditText location = dialogView.findViewById(R.id.add_person_dialog_address);
        date.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private final Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("DefaultLocale")
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!date.getText().toString().equals(current)) {
                    String clean = date.getText().toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int j = 2; j <= cl && j < 6; j += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        String ddmmyyyy = "DDMMYYYY";
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int month = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        month = month < 1 ? 1 : Math.min(month, 12);
                        cal.set(Calendar.MONTH, month - 1);
                        year = (year < 1900) ? 1900 : Math.min(year, 2100);
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                        clean = String.format("%02d%02d%02d", day, month, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = Math.max(sel, 0);
                    current = clean;
                    date.setText(current);
                    date.setSelection(Math.min(sel, current.length()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!date.getText().toString().matches("^\\d{2}/\\d{2}/\\d{4}$")) {
                    date.setError("Not right date format (dd/mm/yyyy)");
                    //alertDialog.getButton(Dialog.BUTTON_POSITIVE).setClickable(false);
                }
            }
        });

        formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        builder.setView(dialogView);
        builder.setTitle("Add New Birthday");
        builder.setPositiveButton("Add", (dialog, id) -> {
            if (date.getError() == null) {
                String dateStr = date.getText().toString();
                LocalDate dateDate = LocalDate.parse(dateStr, formatter);
                String nameStr = name.getText().toString();
                String locationStr = location.getText().toString();
                listener.onDialogPositiveClick(nameStr, dateDate, locationStr);
            } else {
                Toast.makeText(getActivity(), "Did not add due to invalid input", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
        alertDialog = builder.create();
        return alertDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(requireActivity() + " must implement DialogListener");
        }
    }
}
