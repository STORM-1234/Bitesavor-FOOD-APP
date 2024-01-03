package com.example.bitesavor;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class Item {

    private int id;
    private String name;
    private double price;
    private byte[] image;
    private String itemDiscription;

    public Item(int id, String name, double price, Bitmap image,String itemDiscription) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = getBytesFromBitmap(image);
        this.itemDiscription=itemDiscription;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
    public String getItemDiscription() {
        return itemDiscription;
    }

    public byte[] getImage() {
        return image;
    }

    private static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    }

