package com.example.birthdayer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.temporal.ChronoField;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private Context context;
    private List<ListModel> list;
    private SelectListener listener;

    public ListAdapter(Context context, List<ListModel> list, SelectListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.single_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.textName.setText(list.get(holder.getAdapterPosition()).getName());
        holder.textAge.setText(String.valueOf(list.get(holder.getAdapterPosition()).getAge()));

        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View view){
                listener.onItemClicked(list.get(holder.getAdapterPosition()));
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new deleteBirthdayData(holder.cardView.getContext(), list.get(holder.getAdapterPosition())).execute();
                list.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), list.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private static class deleteBirthdayData extends AsyncTask<Void,Void,Void> {
        private final Context context;
        private ListModel listModel;
        public deleteBirthdayData(Context context, ListModel listModel){
            this.context = context;
            this.listModel = listModel;
        }
        @Override
        protected Void doInBackground(Void... params) {
            BirthdayDatabase birthdayDatabase = BirthdayDatabase.getInstance(context);
            List<Birthday> DBlist = birthdayDatabase.birthdayDao().getBirthdayList();

            for (Birthday birthday : DBlist) {
                if(birthday.name.equals(listModel.name)
                        && birthday.birthDate.equals(listModel.birthday.getLong(ChronoField.EPOCH_DAY))
                        && birthday.address.equals("Kennedylaan 2 Veghel")){
                    birthdayDatabase.birthdayDao().deleteBirthday(birthday);
                }
            }

            return null;
        }
    }
}
