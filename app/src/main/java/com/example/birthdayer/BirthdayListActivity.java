package com.example.birthdayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        listModelList = new ArrayList<>();
        listModelList.add(new ListModel("Michelle", LocalDate.of(2001,4,24)));
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
        listModelList.add(new ListModel(name,date));
    }
}