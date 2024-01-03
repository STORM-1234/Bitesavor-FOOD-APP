package com.example.bitesavor;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {
    private DBHelper dbHelper;
    private EditText navName;
    private EditText navEmail;
    private EditText navPass;
    private TextView navNameget;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        dbHelper = new DBHelper(requireContext());

        Cursor cursor = dbHelper.getUser();
        if (cursor.moveToFirst()) {
            navName = view.findViewById(R.id.navName);
            navEmail = view.findViewById(R.id.navEmail);
            navPass=view.findViewById(R.id.navPassword);
            navNameget=view.findViewById(R.id.navNameget);

            navNameget.setText("" + cursor.getString(0));
            navName.setText("" + cursor.getString(0));
            navEmail.setText("" + cursor.getString(2));
        }
        String currentUsername = navNameget.getText().toString();


        Button updateButton = view.findViewById(R.id.change);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String newName = navName.getText().toString();
                String newEmail = navEmail.getText().toString();
                String newPassword = navPass.getText().toString();

                if (newPassword.isEmpty()) {

                    Toast.makeText(getActivity(), "Password cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getActivity(), login.class);
                    startActivity(intent);
                    dbHelper.updateUser(currentUsername, newName, newEmail, newPassword);
                }
            }

        });

        return view;
    }
}