package com.edischool.pojo;

import com.google.gson.annotations.SerializedName;

public class Emploi {

    @SerializedName("idemploi")
    int idemploi;

    @SerializedName("matiere")
    String matiere;

    @SerializedName("idstudent")
    int idstudent;

    @SerializedName("day")
    String day;

    @SerializedName("hour")
    String hour;


    public int getIdemploi() {
        return idemploi;
    }

    public void setIdemploi(int idemploi) {
        this.idemploi = idemploi;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public int getIdstudent() {
        return idstudent;
    }

    public void setIdstudent(int idstudent) {
        this.idstudent = idstudent;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
