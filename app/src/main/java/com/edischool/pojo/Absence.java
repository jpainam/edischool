package com.edischool.pojo;


import java.io.Serializable;

public class Absence implements Serializable {
    private String startHour;
    private String endHour;
    private String day;

    public Absence(){

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

}
