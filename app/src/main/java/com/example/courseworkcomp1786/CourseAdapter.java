package com.example.courseworkcomp1786;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final List<Course> courseList;
    private final MainActivity mainActivity;

    public CourseAdapter(MainActivity mainActivity, List<Course> courseList) {
        this.mainActivity = mainActivity;
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.textViewClassType.setText("Class Type: " + course.getClassType());
        holder.textViewDayOfWeek.setText("Day: " + course.getDayOfWeek());
        holder.textViewTimeOfCourse.setText("Time: " + course.getTimeOfCourse());
        holder.textViewCapacity.setText("Capacity: " + course.getCapacity());
        holder.textViewPricePerClass.setText("Price: " + course.getPricePerClass());



        // Set click listeners for buttons
        holder.buttonAddClass.setOnClickListener(v -> {
            // Tạo AddClassFragment và truyền courseId vào Bundle
            AddClassFragment addClassFragment = new AddClassFragment();
            Bundle args = new Bundle();
            args.putString("courseId", course.getId()); // Truyền courseId vào Bundle
            addClassFragment.setArguments(args); // Đặt arguments cho AddClassFragment
            mainActivity.replaceFragment(addClassFragment); // Điều hướng đến AddClassFragment
        });

        holder.buttonEdit.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Edit clicked for " + course.getClassType(), Toast.LENGTH_SHORT).show();
            EditCourseFragment editCourseFragment = EditCourseFragment.newInstance(course.getId());
            mainActivity.replaceFragment(editCourseFragment);
        });

        holder.buttonDelete.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Delete clicked for " + course.getClassType(), Toast.LENGTH_SHORT).show();
            // Handle Delete functionality
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView textViewClassType;
        TextView textViewDayOfWeek;
        TextView textViewTimeOfCourse;
        TextView textViewCapacity;
        TextView textViewPricePerClass;
        Button buttonAddClass;
        Button buttonEdit;
        Button buttonDelete;


        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewClassType = itemView.findViewById(R.id.textViewClassType);
            textViewDayOfWeek = itemView.findViewById(R.id.textViewDayOfWeek);
            textViewTimeOfCourse = itemView.findViewById(R.id.textViewTimeOfCourse);
            textViewCapacity = itemView.findViewById(R.id.textViewCapacity);
            textViewPricePerClass = itemView.findViewById(R.id.textViewPricePerClass);
            buttonAddClass = itemView.findViewById(R.id.buttonAddClass);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
