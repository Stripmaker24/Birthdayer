package com.example.birthdayer;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    public TextView textName, textAge;
    public CardView cardView;
    public ImageButton deleteBtn;
    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        textName = itemView.findViewById(R.id.textName);
        textAge = itemView.findViewById(R.id.textAge);
        deleteBtn = itemView.findViewById(R.id.deleteBtn);
        cardView = itemView.findViewById(R.id.list_container);
    }
}
