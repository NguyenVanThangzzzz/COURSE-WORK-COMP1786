package com.example.courseworkcomp1786;

public class Course {
    private String id; // Thêm trường id
    private String dayOfWeek;
    private String timeOfCourse;
    private String capacity;
    private String duration;
    private String pricePerClass;
    private String classType;
    private String description;

    public Course() {
        // Constructor mặc định
    }

    public Course(String id, String dayOfWeek, String timeOfCourse, String capacity, String duration, String pricePerClass, String classType, String description) {
        this.id = id; // Khởi tạo id
        this.dayOfWeek = dayOfWeek;
        this.timeOfCourse = timeOfCourse;
        this.capacity = capacity;
        this.duration = duration;
        this.pricePerClass = pricePerClass;
        this.classType = classType;
        this.description = description;
    }

    // Các getter và setter
    public String getId() {
        return id; // Getter cho id
    }

    public void setId(String id) {
        this.id = id; // Setter cho id
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getTimeOfCourse() {
        return timeOfCourse;
    }

    public void setTimeOfCourse(String timeOfCourse) {
        this.timeOfCourse = timeOfCourse;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPricePerClass() {
        return pricePerClass;
    }

    public void setPricePerClass(String pricePerClass) {
        this.pricePerClass = pricePerClass;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
