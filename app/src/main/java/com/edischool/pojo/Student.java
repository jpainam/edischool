package com.edischool.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Student implements Serializable {

    public Student(){

    }
    public Student(int id, String firstName, String lastName, String sexe, String classe, String etablissement) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.classe = classe;
        this.etablissement = etablissement;
        this.sexe = sexe;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", classe='" + classe + '\'' +
                ", etablissement='" + etablissement + '\'' +
                ", sexe='" + sexe + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    @SerializedName("id")
    private int id;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("classe")
    private String classe;

    @SerializedName("etablissement")
    private String etablissement;

    @SerializedName("sexe")
    private String sexe;

    @SerializedName("photo")
    private String photo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSexe(){
        return this.sexe;
    }
    public void setSexe(String sexe){
        this.sexe = sexe;
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

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
