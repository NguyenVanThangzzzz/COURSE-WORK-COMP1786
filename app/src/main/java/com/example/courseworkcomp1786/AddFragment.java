package com.example.courseworkcomp1786;

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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddFragment extends Fragment {

    private TextView dobControl;
    private TextView timeControl;
    private EditText editTextCapacity;
    private EditText editTextDuration;
    private EditText editTextPricePerClass;
    private EditText editTextDescription;
    private RadioGroup radioGroupClassType;
    private Button addButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Liên kết các thành phần giao diện
        dobControl = view.findViewById(R.id.dob_control);
        timeControl = view.findViewById(R.id.time_control);
        editTextCapacity = view.findViewById(R.id.editTextCapacity);
        editTextDuration = view.findViewById(R.id.editTextDuration);
        editTextPricePerClass = view.findViewById(R.id.editTextPricePerClass);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        radioGroupClassType = view.findViewById(R.id.radioGroupClassType);
        addButton = view.findViewById(R.id.add_button);

        // Khởi tạo Firebase với URL tùy chỉnh
        firebaseDatabase = FirebaseDatabase.getInstance("https://course-work-comp1786-f7483-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("courses"); // Đường dẫn tới "courses"

        // Sự kiện click cho dobControl
        dobControl.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment(dobControl);
            datePicker.show(getParentFragmentManager(), "datePicker");
        });

        // Sự kiện click cho timeControl
        timeControl.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(getContext(),
                    (view1, hourOfDay, minuteOfHour) -> {
                        timeControl.setText(String.format("%02d:%02d", hourOfDay, minuteOfHour));
                    }, hour, minute, true);
            timePicker.show();
        });

        // Sự kiện click cho nút Add
        addButton.setOnClickListener(v -> addNewCourse());

        return view;
    }

    private void addNewCourse() {
        // Lấy dữ liệu từ các EditText và các thành phần khác
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

        // Tạo ID cho khóa học (có thể sử dụng UUID hoặc ID từ Firebase)
        String courseId = databaseReference.push().getKey(); // Tạo ID mới từ Firebase

        // Tạo đối tượng Course để thêm vào Firebase
        Course newCourse = new Course(courseId, dayOfWeek, timeOfCourse, capacity, duration, pricePerClass, classType, description);

        // Thêm khóa học vào Firebase Realtime Database
        databaseReference.child(courseId).setValue(newCourse) // Sử dụng ID tạo được để lưu khóa học
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Khóa học đã được thêm thành công!", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Không thể thêm khóa học: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
