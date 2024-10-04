package com.example.courseworkcomp1786;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerViewSearchResults;
    private CourseAdapter courseAdapter;
    private List<Course> allCourses;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewSearchResults = view.findViewById(R.id.recyclerViewSearchResults);
        recyclerViewSearchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        allCourses = new ArrayList<>();
        courseAdapter = new CourseAdapter((MainActivity) getActivity(), allCourses);
        recyclerViewSearchResults.setAdapter(courseAdapter);

        databaseReference = FirebaseDatabase.getInstance("https://course-work-comp1786-f7483-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("courses");

        loadAllCourses();

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterCourses(newText);
                return true;
            }
        });
    }

    private void loadAllCourses() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allCourses.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    if (course != null) {
                        allCourses.add(course);
                    }
                }
                courseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void filterCourses(String query) {
        List<Course> filteredList = new ArrayList<>();
        for (Course course : allCourses) {
            if (course.getClassType().toLowerCase().contains(query.toLowerCase()) ||
                course.getDayOfWeek().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(course);
            }
        }
        courseAdapter = new CourseAdapter((MainActivity) getActivity(), filteredList);
        recyclerViewSearchResults.setAdapter(courseAdapter);
    }
}