package com.edischool.pojo;

import com.google.gson.annotations.SerializedName;

public class Note {

    public Note(){

    }
    @SerializedName("idnote")
    int idnote;

    @SerializedName("idstudent")
    int idstudent;

    @SerializedName("note")
    String note;

    @SerializedName("matiere")
    String matiere;

    @SerializedName("observation")
    String observation;

    @SerializedName("sequence")
    String sequence;

    public int getIdnote() {
        return idnote;
    }

    public void setIdnote(int idnote) {
        this.idnote = idnote;
    }

    public int getIdstudent() {
        return idstudent;
    }

    public void setIdstudent(int idstudent) {
        this.idstudent = idstudent;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMatiere() {
        return matiere;
    }

    public void setMatiere(String matiere) {
        this.matiere = matiere;
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
}
