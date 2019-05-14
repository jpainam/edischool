package com.edischool.pojo;

import java.util.List;

public class WeekListWrapper {
    private String formId;
    private List<Week> timetables;

    public WeekListWrapper(){

    }
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public List<Week> getTimetables() {
        return timetables;
    }

    public void setTimetables(List<Week> timetables) {
        this.timetables = timetables;
    }
}
