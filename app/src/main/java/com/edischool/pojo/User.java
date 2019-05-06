package com.edischool.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class User implements Serializable {
    @SerializedName("iduser")
    String iduser;
    @SerializedName("num_phone")
    String num_phone;
    @SerializedName("password")
    String password;
    @SerializedName("Token")
    String Token;
    @SerializedName("Type")
    String Type;
    @SerializedName("Temps")
    String Temps;
    @SerializedName("Nom")
    String Nom;
    @SerializedName("Prenom")
    String Prenom;
    public User() {
        this.iduser ="0";
        this.num_phone =null;
        this.password = null;
        this.Token = null;
        this.Type=null;
        this.Temps=null;
        this.Nom=null;
        this.Prenom=null;
    }

    public User(String iduser, String num_phone, String password, String token) {
        this.iduser = iduser;
        this.num_phone = num_phone;
        this.password = password;
        this.Token = token;
    }

    public String getType() {
        return Type;
    }

    public String getNom() {
        return Nom;
    }

    public void setNom(String nom) {
        Nom = nom;
    }

    public String getPrenom() {
        return Prenom;
    }

    public void setPrenom(String prenom) {
        Prenom = prenom;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTemps() {
        return Temps;
    }

    public void setTemps(String temps) {
        Temps = temps;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getNum_phone() {
        return num_phone;
    }

    public void setNum_phone(String num_phone) {
        this.num_phone = num_phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        this.Token = token;
    }
}
