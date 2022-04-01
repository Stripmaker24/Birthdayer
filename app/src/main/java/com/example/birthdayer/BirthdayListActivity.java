package com.example.birthdayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class BirthdayListActivity extends AppCompatActivity {
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
        listModelList.add(new ListModel("Michelle",20));
        customAdapter = new CustomAdapter(this, listModelList);
        recyclerView.setAdapter(customAdapter);
    }
}