package com.example.bitesavor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class Admin_Activity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    Button btnSelectImage, logOut, addBt,del;
    ImageView imageView;

    EditText edit_id_text, edit_name_text, edit_price_text,edit_description_text,edit_id_del;

    Uri selectedImageUri;

    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        btnSelectImage = findViewById(R.id.btnSelectImage);
        imageView = findViewById(R.id.imageView_ad);
        logOut = findViewById(R.id.logout_admin);
        addBt = findViewById(R.id.btnadd);
        edit_id_text = findViewById(R.id.edit_id_text);
        edit_name_text = findViewById(R.id.edit_name_text);
        edit_price_text = findViewById(R.id.edit_Price_text);
        edit_description_text= findViewById(R.id.edit_Description_text);
        edit_id_del= findViewById(R.id.id_del_text);
        del=findViewById(R.id.btt_del);

        myDB = new DBHelper(this);

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePicker();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });


            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idToDelete = edit_id_del.getText().toString().trim();

                    if (idToDelete.isEmpty()) {
                        Toast.makeText(Admin_Activity.this, "Please enter an ID to delete", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        long itemId = Long.parseLong(idToDelete);
                        int rowsDeleted = myDB.deleteItemById(itemId);

                        if (rowsDeleted > 0) {

                            edit_id_del.setText("");
                            Toast.makeText(Admin_Activity.this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Admin_Activity.this, "No item found with the given ID", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(Admin_Activity.this, "Invalid ID format", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idText = edit_id_text.getText().toString().trim();
                String name = edit_name_text.getText().toString().trim();
                String priceText = edit_price_text.getText().toString().trim();
                String descriptionItem=edit_description_text.getText().toString().trim();
                if (idText.isEmpty() || name.isEmpty() || priceText.isEmpty() || selectedImageUri == null || descriptionItem.isEmpty()) {
                    Toast.makeText(Admin_Activity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Integer id = Integer.parseInt(idText);
                    Double price = Double.parseDouble(priceText);

                    Bitmap bitmap = getBitmapFromUri(selectedImageUri);

                    if (id == null || price == null || bitmap == null) {
                        Toast.makeText(Admin_Activity.this, "Invalid input data", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();

                    boolean result = myDB.insertItemAD(id, name, price, imageBytes,descriptionItem);

                    if (result) {
                        edit_id_text.setText("");
                        edit_name_text.setText("");
                        edit_price_text.setText("");
                        edit_description_text.setText("");


                        Toast.makeText(Admin_Activity.this, "Item added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Admin_Activity.this, "Error adding item", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(Admin_Activity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
