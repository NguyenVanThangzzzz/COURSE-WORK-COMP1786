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
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditClassFragment extends Fragment {

    private EditText editTextTeacher;
    private TextView editTextDate;
    private EditText editTextComments;
    private Button buttonUpdate;
    private Button buttonBack;

    private String courseId;
    private String classId;
    private DatabaseReference classRef;

    public static EditClassFragment newInstance(String courseId, String classId) {
        EditClassFragment fragment = new EditClassFragment();
        Bundle args = new Bundle();
        args.putString("courseId", courseId);
        args.putString("classId", classId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_class, container, false);

        editTextTeacher = view.findViewById(R.id.editTextTeacher);
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextComments = view.findViewById(R.id.editTextComments);
        buttonUpdate = view.findViewById(R.id.buttonUpdate);
        buttonBack = view.findViewById(R.id.buttonBack);

        if (getArguments() != null) {
            courseId = getArguments().getString("courseId");
            classId = getArguments().getString("classId");
        }

        classRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId).child(classId);

        loadClassData();

        editTextDate.setOnClickListener(v -> showDatePickerDialog());
        buttonUpdate.setOnClickListener(v -> updateClass());
        buttonBack.setOnClickListener(v -> navigateBack());

        return view;
    }

    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment(editTextDate);
        datePickerFragment.show(getChildFragmentManager(), "datePicker");
    }

    private void loadClassData() {
        classRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AddClass addClass = dataSnapshot.getValue(AddClass.class);
                    if (addClass != null) {
                        editTextTeacher.setText(addClass.getTeacher());
                        editTextDate.setText(addClass.getDate());
                        editTextComments.setText(addClass.getComments());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load class data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateClass() {
        String teacher = editTextTeacher.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String comments = editTextComments.getText().toString().trim();

        if (teacher.isEmpty() || date.isEmpty() || comments.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AddClass updatedClass = new AddClass(teacher, date, comments, courseId);
        classRef.setValue(updatedClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Class updated successfully", Toast.LENGTH_SHORT).show();
                navigateToCheckClasses();
            } else {
                Toast.makeText(getContext(), "Failed to update class", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateBack() {
        navigateToCheckClasses();
    }

    private void navigateToCheckClasses() {
        if (getActivity() instanceof MainActivity) {
            CheckClassesFragment checkClassesFragment = new CheckClassesFragment();
            Bundle args = new Bundle();
            args.putString("courseId", courseId);
            checkClassesFragment.setArguments(args);
            ((MainActivity) getActivity()).replaceFragment(checkClassesFragment);
        }
    }
}