package com.example.birthdayer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_person_dialog_fragment, null);
        EditText name = (EditText) dialogView.findViewById(R.id.add_person_dialog_name);
        EditText date = (EditText) dialogView.findViewById(R.id.add_person_dialog_birthday);
        EditText location = (EditText) dialogView.findViewById(R.id.add_person_dialog_address);
        date.addTextChangedListener(new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
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
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    date.setText(current);
                    date.setSelection(sel < current.length() ? sel : current.length());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!date.getText().toString().matches("^\\d{2}/\\d{2}/\\d{4}$")){
                    date.setError("Not right date format (dd/mm/yyyy)");
                    //alertDialog.getButton(Dialog.BUTTON_POSITIVE).setClickable(false);
                }
            }
        });

        formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
        builder.setView(dialogView);
        builder.setTitle("Add New Birthday");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if(date.getError() == null && name.getText().toString() != null && location.getText().toString() != null){
                    String dateStr = date.getText().toString();
                    LocalDate dateDate = LocalDate.parse(dateStr, formatter);
                    String nameStr = name.getText().toString();
                    String locationStr = location.getText().toString();
                    listener.onDialogPositiveClick(nameStr, dateDate, locationStr);
                } else{
                    Toast.makeText(getActivity(),"Did not add due to invalid input",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        alertDialog = builder.create();
        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            listener = (DialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement DialogListener");
        }
    }
}
