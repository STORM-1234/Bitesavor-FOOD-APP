package com.example.bitesavor;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class AboutUsFragment extends Fragment {
    ImageButton logobutton1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);


        logobutton1 = view.findViewById(R.id.logo_au_bt);


        logobutton1.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), Contact_us.class);
            startActivity(intent);
        });

        return view;
    }
}
