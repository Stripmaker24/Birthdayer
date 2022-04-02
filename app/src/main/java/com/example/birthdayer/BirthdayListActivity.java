package com.example.birthdayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.nio.channels.InterruptedByTimeoutException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BirthdayListActivity extends AppCompatActivity implements SelectListener {
    RecyclerView recyclerView;
    List<ListModel> listModelList;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday_list);

        displayItems();
    }

    private void displayItems() {
        recyclerView = findViewById(R.id.recycler_List);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));
        listModelList = new ArrayList<>();
        listModelList.add(new ListModel("Michelle", LocalDate.of(2001,4,24)));
        customAdapter = new CustomAdapter(this, listModelList, this);
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    public void onItemClicked(ListModel listModel) {
        Intent detailActivity = new Intent(this, BirthdayListDetailActivity.class);
        detailActivity.putExtra("selected_person", listModel);
        startActivity(detailActivity);
    }
}