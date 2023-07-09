package com.pee.tokopee;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>{
    private ArrayList<History> listHistory;
    private LayoutInflater inflater;

    public HistoryRecyclerViewAdapter(Context context, ArrayList<History> tempListHistory){
        inflater = LayoutInflater.from(context);
        this.listHistory = tempListHistory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        History history = listHistory.get(position);
        Picasso.get().load(history.getItem().getImageUrl()).into(holder.historyImage);
        holder.historyName.setText(history.getItem().getName());
        holder.historyPrice.setText("Rp " + String.format("%,d", history.getItem().getPrice()));
        String day = String.valueOf(history.getDateTime().getDayOfMonth());
        String month = String.valueOf(history.getDateTime().getMonth());
        String year = String.valueOf(history.getDateTime().getYear());
        holder.historyDate.setText(day + " " + month + " " + year);
    }

    @Override
    public int getItemCount() {
        return listHistory.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView historyImage;
        TextView historyName, historyPrice, historyDate;

        public ViewHolder(View historyView){
            super(historyView);
            historyImage = historyView.findViewById(R.id.imageItem);
            historyName = historyView.findViewById(R.id.txtItemName);
            historyPrice = historyView.findViewById(R.id.txtTotalPrice);
            historyDate = historyView.findViewById(R.id.txtDate);
        }
    }
}