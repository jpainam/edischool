package com.edischool.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.edischool.pojo.Note;
import com.edischool.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NotesDao {
    public static final String TABLE_NOTE = "notes";
    private static final String TAG = "NotesDao";
    public static final String COL_NOTE_ID = "ID";
    public static final String COL_STUDENT_ID = "STUDENT_ID";
    public static final String COL_NOTE = "NOTE";
    public static final String COL_MATIERE = "MATIERE";
    public static final String COL_OBSERVATION = "OBSERVATION";
    public static final String COL_SEQUENCE = "SEQUENCE";

    DatabaseHelper databaseHelper;

    public NotesDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public List<Note> getAllNotes() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +
                TABLE_NOTE, null);

        List<Note> noteList = new ArrayList<>();

        while (res.moveToNext()) {
            Note n = new Note();
            n.setIdnote(res.getInt(res.getColumnIndex(COL_NOTE_ID)));
            n.setIdstudent(res.getInt(res.getColumnIndex(COL_STUDENT_ID)));
            n.setNote(res.getString(res.getColumnIndex(COL_NOTE)));
            n.setMatiere(res.getString(res.getColumnIndex(COL_MATIERE)));
            n.setObservation(res.getString(res.getColumnIndex(COL_OBSERVATION)));
            n.setSequence(res.getString(res.getColumnIndex(COL_SEQUENCE)));
            noteList.add(n);
        }
        return noteList;
    }


    public List<Note> getNotes(int idstudent) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NOTE + " where " + COL_STUDENT_ID + " = '" + idstudent + "'", null);

        List<Note> noteList = new ArrayList<>();

        while (res.moveToNext()) {
            Note n = new Note();
            n.setIdnote(res.getInt(res.getColumnIndex(COL_NOTE_ID)));
            n.setIdstudent(res.getInt(res.getColumnIndex(COL_STUDENT_ID)));
            n.setNote(res.getString(res.getColumnIndex(COL_NOTE)));
            n.setMatiere(res.getString(res.getColumnIndex(COL_MATIERE)));
            n.setObservation(res.getString(res.getColumnIndex(COL_OBSERVATION)));
            n.setSequence(res.getString(res.getColumnIndex(COL_SEQUENCE)));
            noteList.add(n);
        }
        return noteList;
    }

    public boolean insert(Note n) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NOTE_ID, n.getIdnote());
        contentValues.put(COL_STUDENT_ID, n.getIdstudent());
        contentValues.put(COL_NOTE, n.getNote());
        contentValues.put(COL_MATIERE, n.getMatiere());
        contentValues.put(COL_OBSERVATION, n.getObservation());
        contentValues.put(COL_SEQUENCE, n.getSequence());
        long result = db.insert(TABLE_NOTE, null, contentValues);
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
        db.delete(TABLE_NOTE, "1=1", null);
    }
}
