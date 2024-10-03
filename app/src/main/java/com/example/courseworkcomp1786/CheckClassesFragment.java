package com.example.courseworkcomp1786;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.List;

public class CheckClassesFragment extends Fragment implements AddClassAdapter.OnDeleteClickListener {

    private RecyclerView recyclerView;
    private AddClassAdapter adapter;
    private List<AddClass> classList;
    private String courseId;
    private DatabaseReference courseRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_classes, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        classList = new ArrayList<>();
        adapter = new AddClassAdapter(classList, this);
        recyclerView.setAdapter(adapter);

        if (getArguments() != null) {
            courseId = getArguments().getString("courseId");
            loadClasses(courseId);
        }

        ImageView backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> navigateToHome());

        return view;
    }

    private void loadClasses(String courseId) {
        courseRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId);
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                classList.clear();
                for (DataSnapshot classSnapshot : dataSnapshot.getChildren()) {
                    if (classSnapshot.hasChild("teacher") && classSnapshot.hasChild("date") && classSnapshot.hasChild("comments")) {
                        String teacher = classSnapshot.child("teacher").getValue(String.class);
                        String date = classSnapshot.child("date").getValue(String.class);
                        String comments = classSnapshot.child("comments").getValue(String.class);
                        String classId = classSnapshot.getKey();

                        if (teacher != null && date != null && comments != null && classId != null) {
                            AddClass addClass = new AddClass(teacher, date, comments, courseId);
                            addClass.setId(classId);
                            classList.add(addClass);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load classes: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        AddClass classToDelete = classList.get(position);
        String classId = classToDelete.getId();
        
        if (classId != null) {
            courseRef.child(classId).removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Class deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to delete class", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToHome() {
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.replaceFragment(new HomeFragment());
        }
    }
}