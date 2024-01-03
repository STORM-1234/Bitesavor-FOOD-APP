package com.example.bitesavor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import java.util.List;

public class BookFragment extends Fragment {

    private CalendarView calendarView;
    private Spinner spinnerTables;
    private EditText editTextName;
    private Button btnBookTable;

    private DBHelper dbHelper;
    private List<String> bookedTables;

    private String selectedDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        dbHelper = new DBHelper(requireContext());
        bookedTables = dbHelper.getAllBookedTables();

        calendarView = view.findViewById(R.id.calendarView);
        spinnerTables = view.findViewById(R.id.spinnerTables);
        editTextName = view.findViewById(R.id.editTextName);
        btnBookTable = view.findViewById(R.id.btnBookTable);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.table_numbers,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTables.setAdapter(adapter);


        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {

            selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
        });

        btnBookTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookTable();
            }
        });

        return view;
    }

    private void bookTable() {

        String selectedTable = spinnerTables.getSelectedItem().toString();
        String userName = editTextName.getText().toString();

        if (selectedDate == null || selectedDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.isTableBooked(selectedDate, selectedTable)) {
            Toast.makeText(requireContext(), "Table already booked", Toast.LENGTH_SHORT).show();
        } else {
            dbHelper.addBooking(selectedDate, selectedTable, userName, null);
            bookedTables.add(selectedTable);
            Toast.makeText(requireContext(), "Booking successful", Toast.LENGTH_SHORT).show();
        }
    }
}
