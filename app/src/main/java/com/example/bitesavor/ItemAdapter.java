package com.example.bitesavor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item currentItem = itemList.get(position);

        holder.itemNameTextView.setText(currentItem.getName());
        holder.itemPriceTextView.setText(String.valueOf(currentItem.getPrice()));
        holder.itemDescription.setText(String.valueOf(currentItem.getItemDiscription()));

        if (currentItem.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(currentItem.getImage(), 0, currentItem.getImage().length);
            if (bitmap != null) {
                holder.itemImageView.setImageBitmap(bitmap);
            } else {
                holder.itemImageView.setImageResource(R.drawable.default_image);
            }
        } else {
            holder.itemImageView.setImageResource(R.drawable.default_image);
        }

        DBHelper myDB = new DBHelper(holder.itemView.getContext());
        String itemName = currentItem.getName();

        holder.incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());


                if (!myDB.isItemInCart(itemName)) {

                    double itemPrice = currentItem.getPrice();

                    myDB.addToCart(itemName, itemPrice, 1);
                } else {

                    int newQuantity = currentQuantity + 1;
                    myDB.updateCart(itemName, newQuantity);
                    holder.quantityTextView.setText(String.valueOf(newQuantity));
                }
            }
        });

        holder.decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.quantityTextView.getText().toString());

                if (currentQuantity > 0) {
                    int newQuantity = currentQuantity - 1;

                    if (newQuantity > 0) {
                        myDB.updateCart(itemName, newQuantity);
                    } else {
                        myDB.removeFromCart(itemName);
                    }

                    holder.quantityTextView.setText(String.valueOf(newQuantity));
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemNameTextView;
        TextView itemPriceTextView;
        TextView priceSymbol;
        ImageButton decrementButton;
        ImageButton incrementButton;
        TextView quantityTextView;
        TextView itemDescription;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            priceSymbol = itemView.findViewById(R.id.PriceSymbol);
            decrementButton = itemView.findViewById(R.id.decrementButton);
            incrementButton = itemView.findViewById(R.id.incrementButton);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            itemDescription = itemView.findViewById(R.id.itemDescription);
        }
    }
}
