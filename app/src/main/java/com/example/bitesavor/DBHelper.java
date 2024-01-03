package com.example.bitesavor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String ITEM_TABLE = "items";
    private static final String COL_ITEM_ID = "id";

    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table users(username Text primary key, password Text,email Text)");
        sqLiteDatabase.execSQL("create Table session(username Text primary key)");
        sqLiteDatabase.execSQL("create Table  items (id INTEGER PRIMARY KEY, i_name TEXT, i_price REAL, i_image Blob,i_description TEXT)");
        sqLiteDatabase.execSQL("create Table  admin (username Text primary key, password Text,email Text)");
        sqLiteDatabase.execSQL("create Table  cart (c_name Text primary key,c_price TEXT,quantity INTEGER)");
        sqLiteDatabase.execSQL("create Table  Bookings (COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,COLUMN_DATE TEXT,COLUMN_TABLE_NUMBER INTEGER,COLUMN_USER_NAME TEXT,COLUMN_COMMENTS Text)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists users");
        sqLiteDatabase.execSQL("drop Table if exists session");
        sqLiteDatabase.execSQL("drop Table if exists items");
        sqLiteDatabase.execSQL("drop Table if exists admin");
        sqLiteDatabase.execSQL("drop Table if exists cart");
        sqLiteDatabase.execSQL("drop Table if exists Bookings");

    }

    public boolean insertDefaultAdmin() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", "Admin");
        contentValues.put("password", "00968");
        contentValues.put("email", "admin@gg.com");

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();


        long result = sqLiteDatabase.insertWithOnConflict("admin", null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);


        return result != -1;
    }


    public boolean insertData(String username, String password, String email) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);

        long result = sqLiteDatabase.insert("users", null, contentValues);
        return result != -1;
    }

    public boolean insertData2(String username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);

        long result = sqLiteDatabase.insert("session", null, contentValues);
        return result != -1;

    }
    public void updateUser(String currentUsername, String newName, String newEmail, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", newName);
        values.put("email", newEmail);
        values.put("password", newPassword);


        db.update("users", values, "username = ?", new String[]{currentUsername});

        db.close();
    }
    public boolean insertItem(Context context, Integer id, String i_name, Double i_price, int imageResourceId,String i_description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("i_name", i_name);
        contentValues.put("i_price", i_price);
        contentValues.put("i_description",i_description);


        BitmapDrawable bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(imageResourceId);
        Bitmap bitmap = bitmapDrawable.getBitmap();


        byte[] imageBytes = getBytesFromBitmap(bitmap);

        contentValues.put("i_image", imageBytes);

        long result = sqLiteDatabase.insert("items", null, contentValues);
        return result != -1;
    }
    public long addBooking(String date, String tableNumber, String userName, String comments) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("COLUMN_DATE", date);
        values.put("COLUMN_TABLE_NUMBER", tableNumber);
        values.put("COLUMN_USER_NAME", userName);
        values.put("COLUMN_COMMENTS", comments);
        long result = sqLiteDatabase.insert("Bookings", null, values);
        sqLiteDatabase.close();
        return result;
    }
    public List<String> getAllBookedTables() {
        List<String> bookedTables = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        String[] columns = {"COLUMN_TABLE_NUMBER"};

        try (Cursor cursor = db.query(true, "Bookings", columns, null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                int columnIndex = cursor.getColumnIndex("COLUMN_TABLE_NUMBER");

                if (columnIndex >= 0) {
                    String tableNumber = cursor.getString(columnIndex);
                    bookedTables.add(tableNumber);
                } else {

                }
            }
        }

        return bookedTables;
    }

    public boolean isTableBooked(String date, String tableNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Bookings", null,
                "COLUMN_DATE "+ "=? AND " + "COLUMN_TABLE_NUMBER "+ "=?",
                new String[]{date, tableNumber}, null, null, null);

        boolean isBooked = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return isBooked;
    }


    public void updateCart(String item_name, int newQuantity) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("quantity", newQuantity);

        sqLiteDatabase.update("cart", values, "c_name = ?", new String[]{item_name});
    }


    public void removeFromCart(String item_name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("cart", "c_name = ?", new String[]{item_name});
    }


    public boolean isItemInCart(String item_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT * FROM cart WHERE c_name = ?";
            cursor = db.rawQuery(query, new String[]{item_name});

            return cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }



    public void addToCart(String itemName, double itemPrice, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("c_name", itemName);
        values.put("c_price", String.valueOf(itemPrice));
        values.put("quantity", quantity);


        db.insert("cart", null, values);
    }

        public double calculateTotalPriceForAllItems() {
        SQLiteDatabase db = this.getWritableDatabase();


        String query = "SELECT c_price, quantity FROM cart";
        Cursor cursor = db.rawQuery(query, null);

        double totalPrice = 0.0;

        while (cursor.moveToNext()) {

            int itemPriceIndex = cursor.getColumnIndex("c_price");
            int quantityIndex = cursor.getColumnIndex("quantity");


            if (itemPriceIndex != -1 && quantityIndex != -1) {

                double itemPrice = cursor.getDouble(itemPriceIndex);
                int quantity = cursor.getInt(quantityIndex);


                double itemTotal = itemPrice * quantity;
                totalPrice += itemTotal;
            }
        }

        cursor.close();
        db.close();

        return totalPrice;
    }






    public List<CartItem> getAllCartItems() {
        List<CartItem> cartItemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM cart", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int nameIndex = cursor.getColumnIndex("c_name");
                int priceIndex = cursor.getColumnIndex("c_price");
                int quantityIndex = cursor.getColumnIndex("quantity");


                String itemName = cursor.getString(nameIndex);
                double itemPrice = Double.parseDouble(cursor.getString(priceIndex));
                int quantity = cursor.getInt(quantityIndex);

                CartItem cartItem = new CartItem(itemName, itemPrice, quantity);
                cartItemList.add(cartItem);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            Log.d("DBHelper", "No items in the cart");

            cursor.close();
        }

        return cartItemList;
    }






    private static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }


    public boolean insertItemAD(Integer id, String i_name, Double i_price, byte[] i_image,String i_description) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("i_name", i_name);
        contentValues.put("i_price", i_price);
        contentValues.put("i_image", i_image);
        contentValues.put("i_description",i_description);

        long result = sqLiteDatabase.insert("items", null, contentValues);
        return result != -1;
    }



    private static final String COL_ITEM_NAME = "i_name";
    public boolean isItemExists(String itemName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + ITEM_TABLE + " WHERE " + COL_ITEM_NAME + " = ?", new String[]{itemName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }


    public List<Item> getAllItems() {
        List<Item> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM items", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int nameIndex = cursor.getColumnIndex("i_name");
                int priceIndex = cursor.getColumnIndex("i_price");
                int imageIndex = cursor.getColumnIndex("i_image");
                int i_description= cursor.getColumnIndex("i_description");

                int id = cursor.getInt(idIndex);
                String itemName = cursor.getString(nameIndex);
                double itemPrice = cursor.getDouble(priceIndex);
                String descriptionIndex = cursor.getString(i_description);

                byte[] imageBytes = cursor.getBlob(imageIndex);
                Bitmap itemImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                Item item = new Item(id, itemName, itemPrice,itemImage,descriptionIndex);
                itemList.add(item);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return itemList;
    }


    public boolean insertAdmin(String username, String password, String email) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("email", email);

        long result = sqLiteDatabase.insert("admin", null, contentValues);
        return result != -1;
    }
    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", null, null);
        db.close();
    }
    public void clearSession() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete("session", null, null);
    }

    public boolean checkUsername(String username) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where username=?", new String[]{username});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }


    public boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from users where username=? and password=?", new String[]{username, password});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public boolean checkAdmin(String username, String password) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from admin where username=? and password=?", new String[]{username, password});
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public String getCurrentUserName() {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT username FROM session", null);

        String name = null;
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }

        cursor.close();
        return name;
    }

    public Cursor getUser() {
        String name = getCurrentUserName();
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM users WHERE username=?", new String[]{name});
    }

    public Cursor getItem() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM items ", null);
    }
    public int deleteItemById(long itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("items", "id=?", new String[]{String.valueOf(itemId)});
        db.close();
        return rowsDeleted;
    }

}
