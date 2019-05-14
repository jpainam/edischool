package com.edischool.pojo;

import java.util.List;

public class PunishementListWrapper {
    private String studentId;
    private List<Punishment> punishments;

    public PunishementListWrapper(){

    }
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<Punishment> getPunishments() {
        return punishments;
    }

    public void setPunishments(List<Punishment> punishments) {
        this.punishments = punishments;
    }
}
