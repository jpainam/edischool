package com.edischool.emplois;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.edischool.pojo.Emploi;
import com.edischool.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class EmploiDao {
    public static final String TABLE_EMPLOI = "emplois";
    private static final String TAG = "EmploiDao";
    public static final String COL_EMPLOI_ID = "ID";
    public static final String COL_MATIERE = "MATIERE";
    public static final String COL_STUDENT_ID = "STUDENT_ID";
    public static final String COL_DAY = "DAY";
    public static final String COL_HOUR = "HOUR";


    DatabaseHelper databaseHelper;

    public EmploiDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }



    public List<Emploi> getEmplois(int idstudent) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_EMPLOI + " where " + COL_STUDENT_ID + " = '" + idstudent + "'", null);

        List<Emploi> emploiList = new ArrayList<>();

        while (res.moveToNext()) {
            Emploi n = new Emploi();
            n.setIdemploi(res.getInt(res.getColumnIndex(COL_EMPLOI_ID)));
            n.setIdstudent(res.getInt(res.getColumnIndex(COL_STUDENT_ID)));
            n.setMatiere(res.getString(res.getColumnIndex(COL_MATIERE)));
            n.setDay(res.getString(res.getColumnIndex(COL_DAY)));
            n.setHour(res.getString(res.getColumnIndex(COL_HOUR)));
            emploiList.add(n);
        }
        return emploiList;
    }

    public boolean insert(Emploi n) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EMPLOI_ID, n.getIdemploi());
        contentValues.put(COL_STUDENT_ID, n.getIdstudent());
        contentValues.put(COL_MATIERE, n.getMatiere());
        contentValues.put(COL_DAY, n.getDay());
        contentValues.put(COL_HOUR, n.getHour());

        long result = db.insert(TABLE_EMPLOI, null, contentValues);
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
        db.delete(TABLE_EMPLOI, "1=1", null);
    }
}
