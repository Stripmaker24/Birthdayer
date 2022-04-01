package com.example.birthdayer;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    public TextView textName, textAge;
    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        textName = itemView.findViewById(R.id.textName);
        textAge = itemView.findViewById(R.id.textAge);
    }
}
