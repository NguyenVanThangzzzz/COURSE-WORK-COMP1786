package com.example.courseworkcomp1786;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddClassFragment extends Fragment {

    private EditText editTextTeacher;
    private TextView editTextDate; // Thay đổi từ EditText sang TextView
    private EditText editTextComments;
    private Button buttonAdd;

    public AddClassFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_class, container, false);

        editTextTeacher = view.findViewById(R.id.editTextTeacher);
        editTextDate = view.findViewById(R.id.editTextDate); // Cập nhật biến để là TextView
        editTextComments = view.findViewById(R.id.editTextComments);
        buttonAdd = view.findViewById(R.id.buttonAdd);

        // Set click listener for the date TextView to open DatePickerFragment
        editTextDate.setOnClickListener(v -> showDatePickerDialog());

        buttonAdd.setOnClickListener(v -> addClass());

        return view;
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment(editTextDate);
        datePickerFragment.show(getChildFragmentManager(), "datePicker");
    }

    private void addClass() {
        String teacher = editTextTeacher.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String comments = editTextComments.getText().toString().trim();

        if (teacher.isEmpty() || date.isEmpty() || comments.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Handle adding the class to Firebase or your data source

        Toast.makeText(getContext(), "Class added: " + teacher + ", " + date + ", " + comments, Toast.LENGTH_SHORT).show();

        // Optionally navigate back to the HomeFragment or clear the fields
    }
}
