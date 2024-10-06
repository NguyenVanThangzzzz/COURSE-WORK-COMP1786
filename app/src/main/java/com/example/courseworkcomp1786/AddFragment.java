package com.example.courseworkcomp1786;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

    private boolean isDateSelected = false;
    private boolean isTimeSelected = false;

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

        // Sửa đổi sự kiện click cho dobControl
        dobControl.setOnClickListener(v -> {
            DialogFragment datePicker = new DatePickerFragment(dobControl);
            datePicker.show(getParentFragmentManager(), "datePicker");
        });

        // Sửa đổi sự kiện click cho timeControl
        timeControl.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(getContext(),
                    (view1, hourOfDay, minuteOfHour) -> {
                        timeControl.setText(String.format("%02d:%02d", hourOfDay, minuteOfHour));
                        isTimeSelected = true;
                        updateAddButtonState();
                    }, hour, minute, true);
            timePicker.show();
        });

        // Thêm TextWatcher cho dobControl
        dobControl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                isDateSelected = !s.toString().trim().equals("Click here to select the day of the week");
                updateAddButtonState();
            }
        });

        // Bỏ TextWatcher cho timeControl vì chúng ta đã xử lý trong sự kiện click

        // Disable nút Add ban đầu
        addButton.setEnabled(false);

        // Sự kiện click cho nút Add
        addButton.setOnClickListener(v -> addNewCourse());

        return view;
    }

    // Cập nhật phương thức này
    private void updateAddButtonState() {
        addButton.setEnabled(isDateSelected && isTimeSelected);
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

        // Update this part of the validation
        if (dayOfWeek.isEmpty() || timeOfCourse.isEmpty()) {
            Toast.makeText(getContext(), "Please select both day and time", Toast.LENGTH_SHORT).show();
            return;
        }

        if (capacity.isEmpty() || duration.isEmpty() || pricePerClass.isEmpty() || description.isEmpty() || classType.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo ID cho khóa học (có thể sử dụng UUID hoặc ID từ Firebase)
        String courseId = databaseReference.push().getKey(); // Tạo ID mới từ Firebase

        // Tạo đối tượng Course để thêm vào Firebase
        Course newCourse = new Course(courseId, dayOfWeek, timeOfCourse, capacity, duration, pricePerClass, classType, description);

        // Thêm khóa học vào Firebase Realtime Database
        databaseReference.child(courseId).setValue(newCourse)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Khóa học đã được thêm thành công!", Toast.LENGTH_SHORT).show();
                    clearInputFields(); // Add this line to clear input fields
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Không thể thêm khóa học: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Add this new method to clear all input fields
    private void clearInputFields() {
        dobControl.setText("Click here to select the day of the week");
        timeControl.setText("Click here to select time");
        editTextCapacity.setText("");
        editTextDuration.setText("");
        editTextPricePerClass.setText("");
        editTextDescription.setText("");
        radioGroupClassType.clearCheck();
        
        // Reset the flags and update button state
        isDateSelected = false;
        isTimeSelected = false;
        updateAddButtonState();
    }

}
