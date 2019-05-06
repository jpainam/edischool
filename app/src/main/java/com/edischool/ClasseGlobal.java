package com.edischool;


import android.app.Application;

public class ClasseGlobal extends Application {
    private  String iduser;
    private  String num_phone;
    private  String password;
    private  String Type;
    private  String Temps;
    private  String Nom;
    private  String Prenom;
    private  String token;
    private  int Actif;

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

    public String getType() {
        return Type;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getActif() {
        return Actif;
    }

    public void setActif(int actif) {
        Actif = actif;
    }
}
