package com.edischool.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Mark implements Parcelable {
    private String studentId;
    private List<Grade> grades;

    public Mark(){

    }
    protected Mark(Parcel in) {
        studentId = in.readString();
        grades = in.createTypedArrayList(Grade.CREATOR);
    }

    public static final Creator<Mark> CREATOR = new Creator<Mark>() {
        @Override
        public Mark createFromParcel(Parcel in) {
            return new Mark(in);
        }

        @Override
        public Mark[] newArray(int size) {
            return new Mark[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentId);
        dest.writeTypedList(grades);
    }
}
