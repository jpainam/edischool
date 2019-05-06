package com.edischool.pojo;

import com.google.gson.annotations.SerializedName;

public class Absence {


    @SerializedName("idabsence")
    int idabsence;


    @SerializedName("startHour")
    String startHour;

    @SerializedName("endHour")
    String endHour;

    @SerializedName("day")
    String day;

    @SerializedName("idstudent")
    int idstudent;

    public int getIdstudent() {
        return idstudent;
    }

    public void setIdstudent(int idstudent) {
        this.idstudent = idstudent;
    }



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

    public int getIdabsence() {
        return idabsence;
    }

    public void setIdabsence(int idabsence) {
        this.idabsence = idabsence;
    }

}
