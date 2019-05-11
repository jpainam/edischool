package com.edischool.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.edischool.absences.AbsenceDao;
import com.edischool.emplois.EmploiDao;
import com.edischool.manuelscolaires.ManuelscolaireDao;
import com.edischool.notes.NotesDao;
import com.edischool.notification.NotificationDao;
import com.edischool.student.StudentDao;
import com.edischool.timetable.TimeTableDao;
import com.edischool.user.UserDao;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelper sInstance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public static final String DATABASE_NAME = "eschool.db";

    ///////////////////////////////////////////
    public static final String COL_A = "ID";
    public static final String COL_B = "CONNECT";

    ///////////////////////

    /////////////////////////////

    //  TABLE USER
    public static final String COL_USER_ID = "ID";
    public static final String COL_USER_VISITE = "VISITE";
    public static final String COL_USER_TOKEN = "TOKEN";

    //  colonne  Eleve
    public static final String COL_21 = "IDELEVE";
    public static final String COL_22 = "MATRICULE";
    public static final String COL_23 = "IDPARENT";
    public static final String COL_24 = "GENRE";
    public static final String COL_25 = "NOMELEVE";
    public static final String COL_26 = "PRENOMELEVE";



    Context context;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NotificationDao.TABLE_NOTIFICATION +
                " (" + NotificationDao.IDNOTIFICATION + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NotificationDao.TITRENOTIFICATION + " TEXT," +
                NotificationDao.MESSAGENOTIFICATION + " TEXT," +
                NotificationDao.TYPENOTIFICATION + " TEXT," +
                NotificationDao.DATENOTIFICATION + " TEXT," +
                NotificationDao.NOTIFICATIONLU + " INTEGER)");

        db.execSQL("create table " + StudentDao.TABLE_STUDENT +
                " (" + StudentDao.COL_STUDENT_ID + " INTEGER PRIMARY KEY," +
                StudentDao.COL_STUDENT_FIRSTNAME + " TEXT," +
                StudentDao.COL_STUDENT_LASTNAME + " TEXT," +
                StudentDao.COL_STUDENT_SEXE + " TEXT," +
                StudentDao.COL_STUDENT_CLASSE + " TEXT," +
                StudentDao.COL_STUDENT_PHOTO + " TEXT," +
                StudentDao.COL_STUDENT_ETABLISSEMENT + " TEXT)");

        db.execSQL("create table " + NotesDao.TABLE_NOTE +
                " (" + NotesDao.COL_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                NotesDao.COL_STUDENT_ID + " INTEGER," +
                NotesDao.COL_MATIERE + " TEXT," +
                NotesDao.COL_NOTE + " TEXT," +
                NotesDao.COL_OBSERVATION + " TEXT," +
                NotesDao.COL_SEQUENCE + " TEXT)");

        db.execSQL("create table " + ManuelscolaireDao.TABLE_MANUEL_SCOLAIRE +
                " (" + ManuelscolaireDao.COL_MANUEL_ID + " INTEGER, " +
                ManuelscolaireDao.COL_STUDENT_ID + " INTEGER, " +
                ManuelscolaireDao.COL_TITRE + " TEXT, " +
                ManuelscolaireDao.COL_MATIERE + " TEXT, " +
                ManuelscolaireDao.COL_AUTEURS + " TEXT, " +
                ManuelscolaireDao.COL_EDITEURS + " TEXT, " +
                ManuelscolaireDao.COL_PRIX + " TEXT)");

        db.execSQL("create table " + AbsenceDao.TABLE_ABSENCE +
                " (" + AbsenceDao.COL_ABSENCE_ID + " INTEGER, " +
                AbsenceDao.COL_STUDENT_ID + " INTEGER, " +
                AbsenceDao.COL_START_HOUR + " TEXT, " +
                AbsenceDao.COL_END_HOUR + " TEXT, " +
                AbsenceDao.COL_DAY + " TEXT)");

        db.execSQL("create table " + EmploiDao.TABLE_EMPLOI +
                " (" + EmploiDao.COL_EMPLOI_ID + " INTEGER, " +
                EmploiDao.COL_STUDENT_ID + " INTEGER, " +
                EmploiDao.COL_HOUR + " TEXT, " +
                EmploiDao.COL_MATIERE + " TEXT, " +
                EmploiDao.COL_DAY + " TEXT)");

        String CREATE_TIMETABLE = "CREATE TABLE " + TimeTableDao.TIMETABLE + "("
                + TimeTableDao.WEEK_ID + " INTEGER,"
                + TimeTableDao.WEEK_SUBJECT + " TEXT,"
                + TimeTableDao.WEEK_FRAGMENT + " TEXT,"
                + TimeTableDao.WEEK_TEACHER + " TEXT,"
                + TimeTableDao.WEEK_ROOM + " TEXT,"
                + TimeTableDao.WEEK_FROM_TIME + " TEXT,"
                + TimeTableDao.WEEK_TO_TIME + " TEXT,"
                + TimeTableDao.WEEK_COLOR + " INTEGER" + ")";

        db.execSQL(CREATE_TIMETABLE);

        db.execSQL("create table " + UserDao.TABLE_USER + " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_USER_VISITE + " INTEGER," + COL_USER_TOKEN + " TEXT)");
        db.execSQL("create table " + UserDao.TABLE_NAME1 + " (" + COL_21 + " INTEGER," + COL_22 + " TEXT," +
                COL_23 + " INTEGER," + COL_24 + " TEXT," + COL_25 + " TEXT," + COL_26 + " TEXT)");
        //    db.execSQL("create table " + TABLE_NAME2 + " (" + COL_31 + " INTEGER," + COL_32 + " TEXT," + COL_33 + " TEXT," + COL_34 + " TEXT," + COL_35 + " TEXT," + COL_36 + " TEXT," + COL_37 + " TEXT," + COL_38 + " TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NotificationDao.TABLE_NOTIFICATION);
        db.execSQL("DROP TABLE IF EXISTS " + UserDao.TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + StudentDao.TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + NotesDao.TABLE_NOTE);
        db.execSQL("DROP TABLE IF EXISTS " + ManuelscolaireDao.TABLE_MANUEL_SCOLAIRE);
        db.execSQL("DROP TABLE IF EXISTS " + AbsenceDao.TABLE_ABSENCE);
        db.execSQL("DROP TABLE IF EXISTS " + EmploiDao.TABLE_EMPLOI);
        db.execSQL("DROP TABLE IF EXISTS " + TimeTableDao.TIMETABLE);
        onCreate(db);
    }
}
