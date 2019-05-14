package com.edischool.pojo;


import android.os.Parcel;
import android.os.Parcelable;

public class Absence implements Parcelable {
    private String startHour;
    private String endHour;
    private String day;
    private String studentId;


    public Absence(){

    }
    protected Absence(Parcel in) {
        startHour = in.readString();
        endHour = in.readString();
        day = in.readString();
        studentId = in.readString();
    }

    public static final Creator<Absence> CREATOR = new Creator<Absence>() {
        @Override
        public Absence createFromParcel(Parcel in) {
            return new Absence(in);
        }

        @Override
        public Absence[] newArray(int size) {
            return new Absence[size];
        }
    };

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(startHour);
        dest.writeString(endHour);
        dest.writeString(day);
        dest.writeString(studentId);
    }
}
