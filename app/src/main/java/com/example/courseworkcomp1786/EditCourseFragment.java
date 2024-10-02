package com.example.courseworkcomp1786;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class EditCourseFragment extends Fragment {

    private TextView dobControl;
    private TextView timeControl;
    private EditText editTextCapacity;
    private EditText editTextDuration;
    private EditText editTextPricePerClass;
    private EditText editTextDescription;
    private RadioGroup radioGroupClassType;
    private Button saveButton;

    private DatabaseReference databaseReference;
    private String courseId; // ID của khóa học cần chỉnh sửa

    // Constructor mặc định cho Fragment
    public EditCourseFragment() {
        // Required empty public constructor
    }

    public static EditCourseFragment newInstance(String courseId) {
        EditCourseFragment fragment = new EditCourseFragment();
        Bundle args = new Bundle();
        args.putString("courseId", courseId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseId = getArguments().getString("courseId"); // Nhận ID khóa học từ CourseAdapter
        }
        // Khởi tạo Firebase
        databaseReference = FirebaseDatabase.getInstance("https://course-work-comp1786-f7483-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("courses");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_course, container, false);

        // Liên kết các thành phần giao diện
        dobControl = view.findViewById(R.id.dob_control);
        timeControl = view.findViewById(R.id.time_control);
        editTextCapacity = view.findViewById(R.id.editTextCapacity);
        editTextDuration = view.findViewById(R.id.editTextDuration);
        editTextPricePerClass = view.findViewById(R.id.editTextPricePerClass);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        radioGroupClassType = view.findViewById(R.id.radioGroupClassType);
        saveButton = view.findViewById(R.id.save_button);

        // Thiết lập sự kiện click cho nút lưu
        saveButton.setOnClickListener(v -> updateCourse());

        // Thiết lập sự kiện click cho dobControl
        dobControl.setOnClickListener(v -> showDatePicker());

        // Thiết lập sự kiện click cho timeControl
        timeControl.setOnClickListener(v -> showTimePicker());

        // Tải dữ liệu khóa học vào các trường
        loadCourseData();

        return view;
    }

    // Phương thức hiển thị DatePicker
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            // Cập nhật dobControl với ngày đã chọn
            dobControl.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
        }, year, month, day);

        datePickerDialog.show();
    }

    // Phương thức hiển thị TimePicker
    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (view, selectedHour, selectedMinute) -> {
            // Cập nhật timeControl với giờ đã chọn
            timeControl.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void loadCourseData() {
        databaseReference.child(courseId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Course course = dataSnapshot.getValue(Course.class);
                    if (course != null) {
                        dobControl.setText(course.getDayOfWeek());
                        timeControl.setText(course.getTimeOfCourse());
                        editTextCapacity.setText(course.getCapacity());
                        editTextDuration.setText(course.getDuration());
                        editTextPricePerClass.setText(course.getPricePerClass());
                        editTextDescription.setText(course.getDescription());

                        // Thiết lập RadioButton cho classType
                        setSelectedClassType(course.getClassType());
                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy khóa học", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSelectedClassType(String classType) {
        switch (classType) {
            case "Flow Yoga":
                radioGroupClassType.check(R.id.radioFlowYoga);
                break;
            case "Aerial Yoga":
                radioGroupClassType.check(R.id.radioAerialYoga);
                break;
            case "Family Yoga":
                radioGroupClassType.check(R.id.radioFamilyYoga);
                break;
            default:
                radioGroupClassType.clearCheck(); // Nếu không có loại nào khớp
                break;
        }
    }

    private void updateCourse() {
        // Lấy dữ liệu từ các EditText
        String dayOfWeek = dobControl.getText().toString().trim();
        String timeOfCourse = timeControl.getText().toString().trim();
        String capacity = editTextCapacity.getText().toString().trim();
        String duration = editTextDuration.getText().toString().trim();
        String pricePerClass = editTextPricePerClass.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        // Lấy loại lớp học từ RadioGroup
        int selectedTypeId = radioGroupClassType.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = getView().findViewById(selectedTypeId);
        String classType = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

        // Kiểm tra nếu một số trường còn trống
        if (dayOfWeek.isEmpty() || timeOfCourse.isEmpty() || capacity.isEmpty() || duration.isEmpty() || pricePerClass.isEmpty() || description.isEmpty() || classType.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin khóa học vào Firebase
        Course updatedCourse = new Course(courseId, dayOfWeek, timeOfCourse, capacity, duration, pricePerClass, classType, description);
        databaseReference.child(courseId).setValue(updatedCourse)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Khóa học đã được cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    // Quay lại trang HomeFragment
                    ((MainActivity) getActivity()).replaceFragment(new HomeFragment());
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Không thể cập nhật khóa học: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
