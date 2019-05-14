package com.edischool.pojo;

public class Week implements Comparable{

    private String subject, fragment, teacher, room, fromTime, toTime, time, color;
    private int id;

    public Week() {}

    public Week(String subject, String teacher, String room, String fromTime, String toTime, String color) {
        this.subject = subject;
        this.teacher = teacher;
        this.room = room;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromtime) {
        this.fromTime = fromtime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String totime) {
        this.toTime = totime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String toString() {
        return subject;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int compareTo(Object o) {
        String from = ((Week)o).getFromTime();
        /* For Ascending order*/
        return this.getFromTime().compareTo(from);
    }
}
