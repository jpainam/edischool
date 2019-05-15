package com.edischool.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Mark {
    private String studentId;
    private List<Grade> grades;

    public Mark(){

    }




    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

}
