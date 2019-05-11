package com.edischool.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Grade implements Parcelable {
    private String mark;
    private String observation;
    private String sequence;
    private String subject;

    public Grade(){

    }

    protected Grade(Parcel in) {
        mark = in.readString();
        observation = in.readString();
        sequence = in.readString();
        subject = in.readString();
    }

    public static final Creator<Grade> CREATOR = new Creator<Grade>() {
        @Override
        public Grade createFromParcel(Parcel in) {
            return new Grade(in);
        }

        @Override
        public Grade[] newArray(int size) {
            return new Grade[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mark);
        dest.writeString(observation);
        dest.writeString(sequence);
        dest.writeString(subject);
    }
}
