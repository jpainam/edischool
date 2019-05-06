package com.edischool.absences;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.edischool.pojo.Absence;
import com.edischool.pojo.Note;
import com.edischool.sql.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class AbsenceDao {
    public static final String TABLE_ABSENCE = "absences";
    private static final String TAG = "AbsenceDao";
    public static final String COL_ABSENCE_ID = "ID";
    public static final String COL_STUDENT_ID = "STUDENT_ID";
    public static final String COL_START_HOUR = "START_HOUR";
    public static final String COL_END_HOUR = "END_HOUR";
    public static final String COL_DAY = "DAY";

    DatabaseHelper databaseHelper;

    public AbsenceDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public List<Absence> getAllAbsences() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +
                TABLE_ABSENCE, null);

        List<Absence> absenceList = new ArrayList<>();

        while (res.moveToNext()) {
            Absence a = new Absence();
            a.setIdstudent(res.getInt(res.getColumnIndex(COL_STUDENT_ID)));
            a.setDay(res.getString(res.getColumnIndex(COL_DAY)));
            a.setStartHour(res.getString(res.getColumnIndex(COL_START_HOUR)));
            a.setEndHour(res.getString(res.getColumnIndex(COL_END_HOUR)));
            a.setIdabsence(res.getInt(res.getColumnIndex(COL_ABSENCE_ID)));
            absenceList.add(a);
        }
        return absenceList;
    }


    public List<Absence> getAbsences(int idstudent) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_ABSENCE + " where " + COL_STUDENT_ID + " = '" + idstudent + "'", null);

        List<Absence> absenceList = new ArrayList<>();

        while (res.moveToNext()) {
            Absence a = new Absence();
            a.setIdstudent(res.getInt(res.getColumnIndex(COL_STUDENT_ID)));
            a.setDay(res.getString(res.getColumnIndex(COL_DAY)));
            a.setStartHour(res.getString(res.getColumnIndex(COL_START_HOUR)));
            a.setEndHour(res.getString(res.getColumnIndex(COL_END_HOUR)));
            a.setIdabsence(res.getInt(res.getColumnIndex(COL_ABSENCE_ID)));
            absenceList.add(a);
        }
        return absenceList;
    }

    public boolean insert(Absence a) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ABSENCE_ID, a.getIdabsence());
        contentValues.put(COL_STUDENT_ID, a.getIdstudent());
        contentValues.put(COL_START_HOUR, a.getStartHour());
        contentValues.put(COL_END_HOUR, a.getEndHour());
        contentValues.put(COL_DAY, a.getDay());
        long result = db.insert(TABLE_ABSENCE, null, contentValues);
        Log.i(TAG, result + "");
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public void emptyTable() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_ABSENCE, "1=1", null);
    }
}
