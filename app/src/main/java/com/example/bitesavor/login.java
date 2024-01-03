package com.example.bitesavor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class login extends AppCompatActivity {
    Button buttonLogin;
    ImageButton backbtn;
    EditText username, password;
    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        buttonLogin = findViewById(R.id.login_button);
        username = findViewById(R.id.edit_text_username_login);
        password = findViewById(R.id.editTextTextPassword_login);
        backbtn=findViewById(R.id.backBtn);
        myDB = new DBHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if (user.equals("") || pass.equals("")) {
                    Toast.makeText(login.this, "Please enter the credentials", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean resultAdmin = myDB.checkAdmin(user, pass);
                    if (resultAdmin) {
                        myDB.clearSession();

                        Boolean regResult = myDB.insertData2(user);
                        Toast.makeText(login.this, "Admin Login!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Admin_Activity.class);
                        startActivity(intent);
                    } else {
                        Boolean resultUser = myDB.checkUsernamePassword(user, pass);
                        if (resultUser) {
                            myDB.clearSession();
                            myDB.clearCart();
                            Boolean regResult = myDB.insertData2(user);
                            Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Bottom_navi.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(login.this, "Register !!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }});
    }
}
