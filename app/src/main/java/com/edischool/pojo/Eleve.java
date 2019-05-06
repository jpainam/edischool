package com.edischool.pojo;

import com.google.gson.annotations.SerializedName;

public class Eleve {
    @SerializedName("idstudent")
    String ideleve;
    @SerializedName("nom")
    String nom;
    @SerializedName("prenom")
    String prenom;
    @SerializedName("genre")
    String genre;
    @SerializedName("matricule")
    String matricule;
    @SerializedName("imagepath")
    String imagepath;
    @SerializedName("idparent")
    String idparent;

    public Eleve() {
    }

    public String getIdeleve() {
        return ideleve;
    }

    public String getIdparent() {
        return idparent;
    }

    public void setIdparent(String idparent) {
        this.idparent = idparent;
    }

    public void setIdeleve(String ideleve) {
        this.ideleve = ideleve;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}
