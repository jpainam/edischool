package com.edischool.pojo;

public class GradeBook {
    private String gpa;
    private String obtainedMarks;
    private String outOfMark;
    private String rank;
    private String outOfRank;
    private String status; // Fail or Pass

    public GradeBook() {
    }

    public String getGpa() {
        return gpa;
    }

    public void setGpa(String gpa) {
        this.gpa = gpa;
    }

    public String getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(String obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public String getOutOfMark() {
        return outOfMark;
    }

    public void setOutOfMark(String outOfMark) {
        this.outOfMark = outOfMark;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getOutOfRank() {
        return outOfRank;
    }

    public void setOutOfRank(String outOfRank) {
        this.outOfRank = outOfRank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
