package com.edischool.student;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.edischool.pojo.Student;
import com.edischool.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    private static final String TAG = "StudentDao";
    public static final String COL_STUDENT_ID = "ID";
    public static final String COL_STUDENT_FIRSTNAME = "FIRSTNAME";
    public static final String COL_STUDENT_LASTNAME = "LASTNAME";
    public static final String COL_STUDENT_CLASSE = "CLASSE";
    public static final String COL_STUDENT_ETABLISSEMENT = "ETABLISSEMENT";
    public static final String COL_STUDENT_SEXE = "SEXE";
    public static final String COL_STUDENT_PHOTO = "PHOTO";
    public static final String TABLE_STUDENT = "students";

    public static final String DATENOTIFICATION = "DATENOTIFICATION";
    public static final String NOTIFICATIONLU = "NOTIFICATIONLU";


    DatabaseHelper databaseHelper;

    public StudentDao(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public List<Student> getStudentList() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +
                TABLE_STUDENT, null);

        List<Student> studentList = new ArrayList<>();

        while (res.moveToNext()) {
            Student st = new Student(res.getInt(res.getColumnIndex(COL_STUDENT_ID)),
                    res.getString(res.getColumnIndex(COL_STUDENT_FIRSTNAME)),
                    res.getString(res.getColumnIndex(COL_STUDENT_LASTNAME)),
                    res.getString(res.getColumnIndex(COL_STUDENT_SEXE)),
                    res.getString(res.getColumnIndex(COL_STUDENT_CLASSE)),
                    res.getString(res.getColumnIndex(COL_STUDENT_ETABLISSEMENT)));
            st.setPhoto(res.getString(res.getColumnIndex(COL_STUDENT_PHOTO)));
            studentList.add(st);
        }
        return studentList;
    }

    public boolean insert(Student st) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STUDENT_ID, st.getId());
        contentValues.put(COL_STUDENT_FIRSTNAME, st.getFirstName());
        contentValues.put(COL_STUDENT_LASTNAME, st.getLastName());
        contentValues.put(COL_STUDENT_SEXE, st.getSexe());
        contentValues.put(COL_STUDENT_CLASSE, st.getClasse());
        contentValues.put(COL_STUDENT_ETABLISSEMENT, st.getEtablissement());
        contentValues.put(COL_STUDENT_PHOTO, st.getPhoto());
        long result = db.insert(TABLE_STUDENT, null, contentValues);
        Log.i(TAG, result + "");
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
     * KEY_USER_ID, TABLE_USERS, KEY_USER_NAME);
     * Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(user.userName)});
     * @param students
     * @return
     */
    public boolean insert(List<Student> students) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        for (Student st : students) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_STUDENT_ID, st.getId());
            contentValues.put(COL_STUDENT_FIRSTNAME, st.getFirstName());
            contentValues.put(COL_STUDENT_LASTNAME, st.getLastName());
            contentValues.put(COL_STUDENT_SEXE, st.getSexe());
            contentValues.put(COL_STUDENT_CLASSE, st.getClasse());
            contentValues.put(COL_STUDENT_ETABLISSEMENT, st.getEtablissement());
            db.insertOrThrow(TABLE_STUDENT, null, contentValues);
            db.setTransactionSuccessful();
        }
        db.endTransaction();
        return false;
    }

    public void emptyTable() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(TABLE_STUDENT, "1=1", null);
    }
}
