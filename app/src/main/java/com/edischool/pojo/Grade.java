package com.edischool.pojo;

import android.os.Parcel;


public class Grade {
    private String mark;
    private String observation;
    private String sequence;
    private String subject;

    public Grade(){

    }



    public String getMark() {
        return mark;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

}
