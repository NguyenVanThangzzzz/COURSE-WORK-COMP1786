package com.example.courseworkcomp1786;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class CheckClassesFragment extends Fragment {

    private RecyclerView recyclerView;
    private AddClassAdapter adapter;
    private List<AddClass> classList;
    private String courseId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_classes, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        classList = new ArrayList<>();
        adapter = new AddClassAdapter(classList);
        recyclerView.setAdapter(adapter);

        if (getArguments() != null) {
            courseId = getArguments().getString("courseId");
            loadClasses(courseId);
        }

        return view;
    }

    private void loadClasses(String courseId) {
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId);
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classList.clear();
                for (DataSnapshot classSnapshot : dataSnapshot.getChildren()) {
                    // Check if the child is an AddClass object
                    if (classSnapshot.hasChild("teacher") && classSnapshot.hasChild("date") && classSnapshot.hasChild("comments")) {
                        String teacher = classSnapshot.child("teacher").getValue(String.class);
                        String date = classSnapshot.child("date").getValue(String.class);
                        String comments = classSnapshot.child("comments").getValue(String.class);

                        if (teacher != null && date != null && comments != null) {
                            AddClass addClass = new AddClass(teacher, date, comments, courseId);
                            classList.add(addClass);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
                Toast.makeText(getContext(), "Failed to load classes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}