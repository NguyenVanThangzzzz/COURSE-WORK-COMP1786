package com.example.courseworkcomp1786;

public class AddClass {
    private String teacher;
    private String date;
    private String comments;

    public AddClass() {
        // Cần thiết cho Firebase
    }

    public AddClass(String teacher, String date, String comments) {
        this.teacher = teacher;
        this.date = date;
        this.comments = comments;
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
}
