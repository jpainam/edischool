package com.edischool.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AbsenceListWrapper implements Parcelable {
    private String studentId;
    private List<Absence> absences;

    public AbsenceListWrapper(){

    }
    protected AbsenceListWrapper(Parcel in) {
        studentId = in.readString();
        absences = in.createTypedArrayList(Absence.CREATOR);
    }

    public static final Creator<AbsenceListWrapper> CREATOR = new Creator<AbsenceListWrapper>() {
        @Override
        public AbsenceListWrapper createFromParcel(Parcel in) {
            return new AbsenceListWrapper(in);
        }

        @Override
        public AbsenceListWrapper[] newArray(int size) {
            return new AbsenceListWrapper[size];
        }
    };

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentId);
        dest.writeTypedList(absences);
    }
}
