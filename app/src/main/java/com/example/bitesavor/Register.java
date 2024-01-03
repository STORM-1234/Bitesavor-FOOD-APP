package com.example.bitesavor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Patterns;

public class Register extends AppCompatActivity {
    EditText username, password, repassword, email;
    Button btnSignIn, register;
    DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.edit_text_username_register);
        password = findViewById(R.id.edit_text_password_register);
        email = findViewById(R.id.edit_text_email_register);
        repassword = findViewById(R.id.edit_text_repassword_register);
        btnSignIn = findViewById(R.id.login_old_button);
        register = findViewById(R.id.register_button);
        myDB = new DBHelper(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String userEmail = email.getText().toString();

                if (user.equals("") || pass.equals("") || repass.equals("") || userEmail.equals("")) {
                    Toast.makeText(Register.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                } else {

                    if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                        Toast.makeText(Register.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (pass.equals(repass)) {
                        Boolean usercheckResult = myDB.checkUsername(user);
                        if (usercheckResult == false) {
                            Boolean regResult = myDB.insertData(user, pass, userEmail);

                            if (regResult == true) {
                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                                username.setText("");
                                password.setText("");
                                repassword.setText("");
                                email.setText("");

                                Intent intent = new Intent(getApplicationContext(), login.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Register.this, "User already exists. \n Please sign in", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Register.this, "Password not matching.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });
    }
}