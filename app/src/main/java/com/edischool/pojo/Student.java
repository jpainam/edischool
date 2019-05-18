package com.edischool.pojo;

import java.io.Serializable;
import java.util.List;

public class Student implements Serializable {
    private String studentId;
    private String firstName;
    private String lastName;
    private String institution;
    private String photo;
    private String form;
    private String sex;
    private String formId;

    private List<String> responsables;

    public Student(){

    }


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

    public List<String> getResponsables() {
        return responsables;
    }

    public void setResponsables(List<String> responsables) {
        this.responsables = responsables;
    }

}
