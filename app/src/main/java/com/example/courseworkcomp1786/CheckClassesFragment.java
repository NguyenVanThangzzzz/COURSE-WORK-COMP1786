package com.example.courseworkcomp1786;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckClassesFragment extends Fragment implements AddClassAdapter.OnDeleteClickListener {

    private RecyclerView recyclerView;
    private AddClassAdapter adapter;
    private List<AddClass> classList;
    private List<AddClass> allClassList; // Add this line
    private String courseId;
    private DatabaseReference courseRef;
    private EditText searchEditText;
    private ListView searchHistoryListView;
    private ArrayAdapter<String> searchHistoryAdapter;
    private List<String> searchHistory;
    private SharedPreferences sharedPreferences;

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

        searchEditText = view.findViewById(R.id.searchEditText);
        searchHistoryListView = view.findViewById(R.id.searchHistoryListView);
        setupSearchListener();
        setupSearchHistory();

        return view;
    }

    private void setupSearchListener() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterClasses(s.toString());
                if (s.length() == 0) {
                    searchHistoryListView.setVisibility(View.VISIBLE);
                } else {
                    searchHistoryListView.setVisibility(View.GONE);
                }
            }
        });

        searchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    addToSearchHistory(query);
                    filterClasses(query);
                }
                return true;
            }
            return false;
        });

        searchEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && searchEditText.getText().length() == 0) {
                searchHistoryListView.setVisibility(View.VISIBLE);
            } else {
                searchHistoryListView.setVisibility(View.GONE);
            }
        });
    }

    private void setupSearchHistory() {
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        searchHistory = new ArrayList<>(Arrays.asList(sharedPreferences.getString("search_history", "").split(",")));
        searchHistory.removeAll(Arrays.asList("", null));

        searchHistoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, searchHistory);
        searchHistoryListView.setAdapter(searchHistoryAdapter);

        searchHistoryListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = searchHistory.get(position);
            searchEditText.setText(selectedItem);
            searchEditText.setSelection(selectedItem.length());
            filterClasses(selectedItem);
            searchHistoryListView.setVisibility(View.GONE);
        });
    }

    private void addToSearchHistory(String query) {
        if (!query.isEmpty() && !searchHistory.contains(query)) {
            searchHistory.add(0, query);
            if (searchHistory.size() > 3) {
                searchHistory.remove(searchHistory.size() - 1);
            }
            searchHistoryAdapter.notifyDataSetChanged();
            saveSearchHistory();
        }
    }

    private void filterClasses(String query) {
        if (allClassList != null) {
            List<AddClass> filteredList = SearchClass.searchClasses(allClassList, query);
            adapter.updateList(filteredList);
        }
    }

    private void saveSearchHistory() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("search_history", String.join(",", searchHistory));
        editor.apply();
    }

    private void loadClasses(String courseId) {
        courseRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId);
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allClassList = new ArrayList<>();
                for (DataSnapshot classSnapshot : dataSnapshot.getChildren()) {
                    if (classSnapshot.hasChild("teacher") && classSnapshot.hasChild("date") && classSnapshot.hasChild("comments")) {
                        String teacher = classSnapshot.child("teacher").getValue(String.class);
                        String date = classSnapshot.child("date").getValue(String.class);
                        String comments = classSnapshot.child("comments").getValue(String.class);
                        String classId = classSnapshot.getKey();

                        if (teacher != null && date != null && comments != null && classId != null) {
                            AddClass addClass = new AddClass(teacher, date, comments, courseId);
                            addClass.setId(classId);
                            allClassList.add(addClass);
                        }
                    }
                }
                classList = new ArrayList<>(allClassList);
                adapter.updateList(classList);
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