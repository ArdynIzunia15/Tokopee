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
import java.util.List;

interface ItemClickListener {
    void onItemClick(View view, int position);
}
public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> implements ItemClickListener{
    private ArrayList<Item> listItem;
    private LayoutInflater inflater;
    private ItemClickListener itemClickListener;

    public ItemRecyclerViewAdapter(Context context, ArrayList<Item> tempListItem){
        inflater = LayoutInflater.from(context);
        this.listItem = tempListItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = listItem.get(position);
        Picasso.get().load(item.getImageUrl()).into(holder.itemImage);
        holder.itemName.setText(item.getName());
        holder.itemPrice.setText("Rp " + String.format("%,d", item.getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ItemDetailActivity.class);
                intent.putExtra("item", listItem.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(view.getContext(), ItemDetailActivity.class);
        intent.putExtra("item", listItem.get(position));
        view.getContext().startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemName, itemPrice;

        public ViewHolder(View itemView){
            super(itemView);
            itemImage = itemView.findViewById(R.id.imageItem);
            itemName = itemView.findViewById(R.id.txtItemName);
            itemPrice = itemView.findViewById(R.id.txtItemPrice);
        }
    }
}
