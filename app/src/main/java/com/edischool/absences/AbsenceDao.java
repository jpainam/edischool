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





    public boolean insert(Absence a) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(COL_ABSENCE_ID, a.getIdabsence());
        //contentValues.put(COL_STUDENT_ID, a.getIdstudent());
        //contentValues.put(COL_START_HOUR, a.getStartHour());
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
