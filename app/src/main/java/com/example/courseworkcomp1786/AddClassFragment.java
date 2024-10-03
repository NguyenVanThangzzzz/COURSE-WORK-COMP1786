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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddClassFragment extends Fragment {

    private EditText editTextTeacher;
    private TextView editTextDate; // Thay đổi từ EditText sang TextView
    private EditText editTextComments;
    private Button buttonAdd;
    private String courseId; // Biến để giữ courseId liên kết với lớp này

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

        // Lấy courseId từ arguments hoặc thiết lập giá trị mặc định
        if (getArguments() != null) {
            courseId = getArguments().getString("courseId");
        }

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

        // Kiểm tra các trường thông tin
        if (teacher.isEmpty() || date.isEmpty() || comments.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy courseId từ arguments được truyền vào fragment
        String courseId = getArguments().getString("courseId");

        // Nếu không tìm thấy courseId, hiển thị thông báo lỗi
        if (courseId == null) {
            Toast.makeText(getContext(), "Course ID is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng AddClass với thông tin nhập vào và courseId
        AddClass addClass = new AddClass(teacher, date, comments, courseId);

        // Truy cập vào nút con của courseId mà không tạo thêm nút "addClasses"
        DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("courses").child(courseId);

        // Thêm dữ liệu AddClass trực tiếp vào khóa học
        courseRef.push().setValue(addClass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getContext(), "Class added successfully", Toast.LENGTH_SHORT).show();
                // Optionally, navigate back to the previous fragment or clear input fields
            } else {
                Toast.makeText(getContext(), "Failed to add class", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
