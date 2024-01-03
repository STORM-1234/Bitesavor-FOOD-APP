package com.example.bitesavor;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;



public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private DBHelper dbHelper;
    private Button clear_btn,payment;

    private TextView total_price;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        dbHelper = new DBHelper(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);
        clear_btn = view.findViewById(R.id.clear_cart_btn);
        payment = view.findViewById(R.id.payment_cart_btn);
        total_price = view.findViewById(R.id.total);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        double totalPrice = dbHelper.calculateTotalPriceForAllItems();
        total_price.setText(String.valueOf(totalPrice));

        dbHelper = new DBHelper(getActivity());
        List<CartItem> cartItems = dbHelper.getAllCartItems();

        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(cartItems);
            recyclerView.setAdapter(cartAdapter);
        } else {
            cartAdapter.setCartItems(cartItems);
            cartAdapter.notifyDataSetChanged();
        }

        clear_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.clearCart();
                cartAdapter.setCartItems(new ArrayList<>());
                cartAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getActivity(), Bottom_navi.class);
                startActivity(intent);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.clearCart();
                cartAdapter.setCartItems(new ArrayList<>());
                cartAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getActivity(), PaymentActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }


    public void totalUpdate() {
        double totalPrice = dbHelper.calculateTotalPriceForAllItems();
        total_price.setText(String.valueOf(totalPrice));
    }

    private class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
        private List<CartItem> cartItems;


        public CartAdapter(List<CartItem> cartItems) {
            this.cartItems = cartItems;
        }

        public void setCartItems(List<CartItem> cartItems) {
            this.cartItems = cartItems;
        }

        @Override
        public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            return new CartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CartViewHolder holder, int position) {
            CartItem cartItem = cartItems.get(position);
            holder.itemNameTextView.setText(cartItem.getItemName());
            holder.itemPriceTextView.setText(String.valueOf(cartItem.getItemPrice()));
            holder.itemQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        }



        @Override
        public int getItemCount() {
            return cartItems.size();
        }
    }

    private class CartViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;
        ImageButton DecrementButton, IncrementButton;
        TextView itemPriceTextView;
        TextView itemQuantityTextView;

        public CartViewHolder(View itemView) {
            super(itemView);
            DecrementButton = itemView.findViewById(R.id.decrementButton);
            IncrementButton = itemView.findViewById(R.id.incrementButton);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            itemQuantityTextView = itemView.findViewById(R.id.itemQuantityTextView);


            IncrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleIncrement();
                    totalUpdate();
                }
            });

            DecrementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleDecrement();
                    totalUpdate();

                }
            });
        }


        public void bind(CartItem cartItem) {
            itemNameTextView.setText(cartItem.getItemName());
            itemPriceTextView.setText(String.valueOf(cartItem.getItemPrice()));
            itemQuantityTextView.setText(String.valueOf(cartItem.getQuantity()));
        }

        private void handleIncrement() {
            int currentQuantity = Integer.parseInt(itemQuantityTextView.getText().toString());
            int newQuantity = currentQuantity + 1;

            DBHelper myDB = new DBHelper(itemView.getContext());
            String itemName = itemNameTextView.getText().toString();

            myDB.updateCart(itemName, newQuantity);
            itemQuantityTextView.setText(String.valueOf(newQuantity));

        }

        private void handleDecrement() {
            int currentQuantity = Integer.parseInt(itemQuantityTextView.getText().toString());

            if (currentQuantity > 0) {
                int newQuantity = currentQuantity - 1;

                DBHelper myDB = new DBHelper(itemView.getContext());
                String itemName = itemNameTextView.getText().toString();



                itemQuantityTextView.setText(String.valueOf(newQuantity));
            }
        }
    }




}

