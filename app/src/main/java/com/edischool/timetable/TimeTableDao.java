package com.edischool.timetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edischool.sql.DatabaseHelper;
import com.edischool.pojo.Week;
import java.util.ArrayList;

public class TimeTableDao {

    public DatabaseHelper databaseHelper;
    public static final String TIMETABLE = "timetable";
    public static final String WEEK_ID = "id";
    public static final String WEEK_SUBJECT = "subject";
    public static final String WEEK_FRAGMENT = "fragment";
    public static final String WEEK_TEACHER = "teacher";
    public static final String WEEK_ROOM = "room";
    public static final String WEEK_FROM_TIME = "fromtime";
    public static final String WEEK_TO_TIME = "totime";
    public static final String WEEK_COLOR = "color";


    public TimeTableDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }


    public void insertWeek(Week week) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_SUBJECT, week.getSubject());
        contentValues.put(WEEK_FRAGMENT, week.getFragment());
        contentValues.put(WEEK_TEACHER, week.getTeacher());
        contentValues.put(WEEK_ROOM, week.getRoom());
        contentValues.put(WEEK_FROM_TIME, week.getFromTime());
        contentValues.put(WEEK_TO_TIME, week.getToTime());
        contentValues.put(WEEK_COLOR, week.getColor());
        db.insert(TIMETABLE, null, contentValues);
        //db.update(TIMETABLE, contentValues, WEEK_FRAGMENT, null);
        db.close();
    }

    public void deleteWeekById(Week week) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TIMETABLE, WEEK_ID + " = ? ", new String[]{String.valueOf(week.getId())});
        db.close();
    }

    public void updateWeek(Week week) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_SUBJECT, week.getSubject());
        contentValues.put(WEEK_TEACHER, week.getTeacher());
        contentValues.put(WEEK_ROOM, week.getRoom());
        contentValues.put(WEEK_FROM_TIME, week.getFromTime());
        contentValues.put(WEEK_TO_TIME, week.getToTime());
        contentValues.put(WEEK_COLOR, week.getColor());
        db.update(TIMETABLE, contentValues, WEEK_ID + " = " + week.getId(), null);
        db.close();
    }

    public ArrayList<Week> getWeek(String fragment) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ArrayList<Week> weeklist = new ArrayList<>();
        Week week;
        Cursor cursor = db.rawQuery("SELECT * FROM ( SELECT * FROM " + TIMETABLE + " ORDER BY " + WEEK_FROM_TIME + " ) WHERE " + WEEK_FRAGMENT + " LIKE '" + fragment + "%'", null);
        while (cursor.moveToNext()) {
            week = new Week();
            week.setId(cursor.getInt(cursor.getColumnIndex(WEEK_ID)));
            week.setSubject(cursor.getString(cursor.getColumnIndex(WEEK_SUBJECT)));
            week.setTeacher(cursor.getString(cursor.getColumnIndex(WEEK_TEACHER)));
            week.setRoom(cursor.getString(cursor.getColumnIndex(WEEK_ROOM)));
            week.setFromTime(cursor.getString(cursor.getColumnIndex(WEEK_FROM_TIME)));
            week.setToTime(cursor.getString(cursor.getColumnIndex(WEEK_TO_TIME)));
            week.setColor(cursor.getInt(cursor.getColumnIndex(WEEK_COLOR)));
            weeklist.add(week);
        }
        return weeklist;
    }
    public void emptyTable() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TIMETABLE, "1=1", null);
    }
}







