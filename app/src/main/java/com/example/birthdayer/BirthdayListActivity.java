package com.example.birthdayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

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
        addPersonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        recyclerView = findViewById(R.id.recycler_List);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        try {
            listModelList = new getBirthdayData(this).execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //listModelList.add(new ListModel("Michelle", LocalDate.of(2001,4,24)));
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
    public void onDialogPositiveClick(String name, LocalDate date) {
        ListModel newListModel = new ListModel(name,date);
        listModelList.add(newListModel);
        new addBirthdayData(this, newListModel).execute();
    }

    private static class addBirthdayData extends AsyncTask<Void, Void, Boolean> {
        private final Context context;
        private final ListModel newListModel;
        public addBirthdayData(Context context, ListModel newListModel){
            this.context = context;
            this.newListModel = newListModel;
        }
        @SuppressLint("NewApi")
        @Override
        protected Boolean doInBackground(Void... params) {
            BirthdayDatabase birthdayDatabase = BirthdayDatabase.getInstance(context);
            birthdayDatabase.birthdayDao().insertBirthday(new Birthday(newListModel.name, newListModel.birthday.getLong(ChronoField.EPOCH_DAY),"Kennedylaan 2 Veghel"));
            return true;
        }
    }

    private static class getBirthdayData extends AsyncTask<Void,Void,List<ListModel>> {
        private final Context context;
        public getBirthdayData(Context context){
            this.context = context;
        }
        @SuppressLint("NewApi")
        @Override
        protected List<ListModel> doInBackground(Void... params) {
            BirthdayDatabase birthdayDatabase = BirthdayDatabase.getInstance(context);
            List<Birthday> DBlist = birthdayDatabase.birthdayDao().getBirthdayList();
            List<ListModel> listModels = new ArrayList<>();
            DBlist.forEach(birthday -> {
                listModels.add(new ListModel(birthday.name, LocalDate.ofEpochDay(birthday.birthDate)));
            });
            return listModels;
        }
    }
}