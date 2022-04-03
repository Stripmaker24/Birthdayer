package com.example.birthdayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BirthdayListActivity extends AppCompatActivity implements SelectListener, DialogListener {
    RecyclerView recyclerView;
    FloatingActionButton addPersonButton;
    List<ListModel> listModelList;
    ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_list);

        displayItems();
    }

    private void displayItems() {
        addPersonButton = findViewById(R.id.add_person_button);
        addPersonButton.setOnClickListener(view -> openDialog());
        recyclerView = findViewById(R.id.recycler_List);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        try {
            listModelList = new getBirthdayData(this).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        listAdapter = new ListAdapter(this, listModelList, this);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onItemClicked(ListModel listModel) {
        Intent detailActivity = new Intent(this, BirthdayListDetailActivity.class);
        detailActivity.putExtra("selected_person", listModel);
        startActivity(detailActivity);
    }

    private void openDialog() {
        AddPersonDialogFragment dialog = new AddPersonDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddPersonDialogFragment");
    }

    @Override
    public void onDialogPositiveClick(String name, LocalDate date, String location) {
        ListModel newListModel = new ListModel(name, date, location);
        listModelList.add(newListModel);
        new addBirthdayData(this, newListModel).execute();
    }

    private static class addBirthdayData extends AsyncTask<Void, Void, Boolean> {
        @SuppressLint("StaticFieldLeak")
        private final Context context;
        private final ListModel newListModel;

        @SuppressWarnings("deprecation")
        public addBirthdayData(Context context, ListModel newListModel) {
            this.context = context;
            this.newListModel = newListModel;
        }

        @SuppressLint("NewApi")
        @Override
        protected Boolean doInBackground(Void... params) {
            BirthdayDatabase birthdayDatabase = BirthdayDatabase.getInstance(context);
            birthdayDatabase.birthdayDao().insertBirthday(new Birthday(newListModel.name, newListModel.birthday.getLong(ChronoField.EPOCH_DAY), newListModel.location));
            return true;
        }
    }

    private static class getBirthdayData extends AsyncTask<Void, Void, List<ListModel>> {
        @SuppressLint("StaticFieldLeak")
        private final Context context;

        @SuppressWarnings("deprecation")
        public getBirthdayData(Context context) {
            this.context = context;
        }

        @SuppressLint("NewApi")
        @Override
        protected List<ListModel> doInBackground(Void... params) {
            BirthdayDatabase birthdayDatabase = BirthdayDatabase.getInstance(context);
            List<Birthday> DbList = birthdayDatabase.birthdayDao().getBirthdayList();
            List<ListModel> listModels = new ArrayList<>();
            DbList.forEach(birthday -> listModels.add(new ListModel(birthday.name, LocalDate.ofEpochDay(birthday.birthDate), birthday.address)));
            return listModels;
        }
    }
}