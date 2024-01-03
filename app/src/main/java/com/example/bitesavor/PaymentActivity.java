package com.example.bitesavor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private CheckBox checkBoxCash, checkBoxCard;
    private EditText editTextCardNumber, editTextCVV, editTextCardHolderName, editTextExpiry;
    private Button btnPay , btncan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        checkBoxCash = findViewById(R.id.checkBoxCash);
        checkBoxCard = findViewById(R.id.checkBoxCard);
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextCVV = findViewById(R.id.editTextCVV);
        editTextCardHolderName = findViewById(R.id.editTextCardHolderName);
        editTextExpiry = findViewById(R.id.editTextExpiry);
        btnPay = findViewById(R.id.btnPay);
        btncan = findViewById(R.id.btnCancel);

        checkBoxCash.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxCard.setChecked(false);
                btnPay.setText("Continue");
            }
        });

        checkBoxCard.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxCash.setChecked(false);
                btnPay.setText("Pay");
            }
        });


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxCard.isChecked()) {
                    if (areCardFieldsEmpty()) {
                        Toast.makeText(PaymentActivity.this, "Fill all required fields", Toast.LENGTH_SHORT).show();
                    } else {
                        displaySplashScreen();
                    }
                } else if (checkBoxCash.isChecked()) {
                    displaySplashScreen();
                }
            }
        });
        btncan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Bottom_navi.class);
                startActivity(intent);
            }
        });


    }
    private boolean areCardFieldsEmpty() {
        return editTextCardNumber.getText().toString().isEmpty() ||
                editTextCVV.getText().toString().isEmpty() ||
                editTextCardHolderName.getText().toString().isEmpty() ||
                editTextExpiry.getText().toString().isEmpty();
    }

    private void displaySplashScreen() {
        Intent splashIntent = new Intent(this, Splash.class);
        startActivity(splashIntent);
    }
}
