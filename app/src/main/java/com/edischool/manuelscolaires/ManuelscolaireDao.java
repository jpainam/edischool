package com.edischool.manuelscolaires;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.edischool.pojo.Manuelscolaire;
import com.edischool.pojo.Note;
import com.edischool.sql.DatabaseHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManuelscolaireDao {
    public static final String TABLE_MANUEL_SCOLAIRE = "manuelscolaires";
    private static final String TAG = "ManuelscolaireDao";
    public static final String COL_MANUEL_ID = "ID";
    public static final String COL_STUDENT_ID = "STUDENT_ID";
    public static final String COL_TITRE = "TITRE";
    public static final String COL_MATIERE = "MATIERE";
    public static final String COL_EDITEURS = "EDITEURS";
    public static final String COL_AUTEURS = "AUTEURS";
    public static final String COL_PRIX = "PRIX";

    DatabaseHelper databaseHelper;

    public ManuelscolaireDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public List<Manuelscolaire> getAllManuels() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +
                TABLE_MANUEL_SCOLAIRE, null);

        List<Manuelscolaire> manuelscolaires = new ArrayList<>();

        while (res.moveToNext()) {
            Manuelscolaire m = new Manuelscolaire();
            m.setIdmanuel(res.getInt(res.getColumnIndex(COL_MANUEL_ID)));
            m.setIdstudent(res.getInt(res.getColumnIndex(COL_STUDENT_ID)));
            m.setTitre(res.getString(res.getColumnIndex(COL_TITRE)));
            m.setAuteurs(res.getString(res.getColumnIndex(COL_AUTEURS)));
            m.setEditeurs(res.getString(res.getColumnIndex(COL_EDITEURS)));
            m.setPrix(res.getString(res.getColumnIndex(COL_PRIX)));
            m.setMatiere(res.getString(res.getColumnIndex(COL_MATIERE)));
            manuelscolaires.add(m);
        }
        return manuelscolaires;
    }


    public List<Manuelscolaire> getManuels(int idstudent) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_MANUEL_SCOLAIRE + " where " + COL_STUDENT_ID + " = '" + idstudent + "'", null);

        List<Manuelscolaire> manuelscolaires = new ArrayList<>();

        while (res.moveToNext()) {
            Manuelscolaire m = new Manuelscolaire();
            m.setIdmanuel(res.getInt(res.getColumnIndex(COL_MANUEL_ID)));
            m.setIdstudent(res.getInt(res.getColumnIndex(COL_STUDENT_ID)));
            m.setTitre(res.getString(res.getColumnIndex(COL_TITRE)));
            m.setAuteurs(res.getString(res.getColumnIndex(COL_AUTEURS)));
            m.setEditeurs(res.getString(res.getColumnIndex(COL_EDITEURS)));
            m.setPrix(res.getString(res.getColumnIndex(COL_PRIX)));
            m.setMatiere(res.getString(res.getColumnIndex(COL_MATIERE)));
            manuelscolaires.add(m);
        }
        return manuelscolaires;
    }
    public List<String> getAllSubjects(int idstudent){
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select distinct(" + COL_MATIERE + ") " +
                "from " + TABLE_MANUEL_SCOLAIRE + " " +
                "where " + COL_STUDENT_ID + " = '" + idstudent + "'", null);
        List<String> subjects = new ArrayList<>();
        while (res.moveToNext()) {
            subjects.add(res.getString(res.getColumnIndex(COL_MATIERE)));
        }
        return subjects;
    }

    public HashMap<String, List<Manuelscolaire>> getManualsBySubject(int idstudent){
        HashMap<String, List<Manuelscolaire>> hashMap =  new HashMap<>();
        List<String> subjects = this.getAllSubjects(idstudent);
        List<Manuelscolaire> manuelscolaires = this.getManuels(idstudent);
        for (Manuelscolaire m: manuelscolaires) {
            if(hashMap.containsKey(m.getMatiere())){
                hashMap.get(m.getMatiere()).add(m);
            }else{
                List<Manuelscolaire> list = new ArrayList<>();
                list.add(m);
                hashMap.put(m.getMatiere(), list);
            }
        }
        return hashMap;
    }

    public boolean insert(Manuelscolaire m) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MANUEL_ID, m.getIdmanuel());
        contentValues.put(COL_STUDENT_ID, m.getIdstudent());
        contentValues.put(COL_TITRE, m.getTitre());
        contentValues.put(COL_AUTEURS, m.getAuteurs());
        contentValues.put(COL_EDITEURS, m.getEditeurs());
        contentValues.put(COL_PRIX, m.getPrix());
        contentValues.put(COL_MATIERE, m.getMatiere());
        long result = db.insert(TABLE_MANUEL_SCOLAIRE, null, contentValues);
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
        db.delete(TABLE_MANUEL_SCOLAIRE, "1=1", null);
    }
}
