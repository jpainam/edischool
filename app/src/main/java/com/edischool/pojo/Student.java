package com.edischool.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    private String studentId;
    private String firstName;
    private String lastName;
    private String institution;
    private String photo;
    private String form;
    private String sex;
    private String formId;

    public Student(){

    }
    protected Student(Parcel in) {
        studentId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        institution = in.readString();
        photo = in.readString();
        form = in.readString();
        formId = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    public Student(int anInt, String string, String string1, String string2, String string3, String string4) {
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(institution);
        dest.writeString(photo);
        dest.writeString(form);
        dest.writeString(formId);
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
}
