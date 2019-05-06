package com.edischool.pojo;

import com.google.gson.annotations.SerializedName;

public class Manuelscolaire {

    @SerializedName("idmanuel")
    int idmanuel;

    @SerializedName("titre")
    String titre;

    @SerializedName("matiere")
    String matiere;

    @SerializedName("editeurs")
    String editeurs;

    @SerializedName("auteurs")
    String auteurs;

    @SerializedName("prix")
    String prix;

    @SerializedName("idstudent")
    int idstudent;

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
    }

    public int getIdmanuel() {
        return idmanuel;
    }

    public void setIdmanuel(int idmanuel) {
        this.idmanuel = idmanuel;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getEditeurs() {
        return editeurs;
    }

    public void setEditeurs(String editeurs) {
        this.editeurs = editeurs;
    }

    public String getAuteurs() {
        return auteurs;
    }

    public void setAuteurs(String auteurs) {
        this.auteurs = auteurs;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }


    public int getIdstudent() {
        return idstudent;
    }

    public void setIdstudent(int idstudent) {
        this.idstudent = idstudent;
    }


}
