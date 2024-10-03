package com.example.courseworkcomp1786;

public class AddClass {
    private String id;
    private String teacher;
    private String date;
    private String comments;
    private String courseId; // Khóa ngoại để liên kết với Course

    public AddClass() {
        // Cần thiết cho Firebase
    }

    public AddClass(String teacher, String date, String comments, String courseId) {
        this.teacher = teacher;
        this.date = date;
        this.comments = comments;
        this.courseId = courseId; // Thêm khóa ngoại courseId
    }

    // Getter and Setter methods
    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCourseId() {
        return courseId; // Getter cho courseId
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId; // Setter cho courseId
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
