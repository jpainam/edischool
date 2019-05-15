package com.edischool.notification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edischool.pojo.Notification;
import com.edischool.sql.DatabaseHelper;

public class NotificationDao {
    public static final String TABLE_NOTIFICATION = "notification";
    public static final String IDNOTIFICATION = "IDNOTIFICATION";
    public static final String TITRENOTIFICATION = "TITRENOTIFICATION";
    public static final String MESSAGENOTIFICATION = "MESSAGENOTIFICATION";
    public static final String TYPENOTIFICATION = "TYPENOTIFICATION";
    public static final String DATENOTIFICATION = "DATENOTIFICATION";
    public static final String NOTIFICATIONLU = "NOTIFICATIONLU";

    DatabaseHelper databaseHelper;
    public NotificationDao(Context context){
        databaseHelper = DatabaseHelper.getInstance(context);
    }

    public Notification insert(String  titre, String message, String typenotification, String datenotification, int notificationlue) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITRENOTIFICATION, titre);
        contentValues.put(MESSAGENOTIFICATION, message);
        contentValues.put(TYPENOTIFICATION, typenotification);
        contentValues.put(DATENOTIFICATION, datenotification);
        contentValues.put(NOTIFICATIONLU, notificationlue);
        long result = db.insert(TABLE_NOTIFICATION, null, contentValues);
        if (result == -1)
            return null;
        else
            return getNotification(result);
    }

    public Notification getNotification(long id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NOTIFICATION + " where " + IDNOTIFICATION + "='" + id + "'", null);
        Notification notifications=new Notification();
        while (res.moveToNext()) {
            notifications.setNotificationTitle(res.getString(1));
            notifications.setNotificationMessage(res.getString(2));
            notifications.setNotificationType(res.getString(3));
            notifications.setCreateAt(res.getString(4));
            notifications.setRead(false);
        }
        return notifications;
    }

    public Cursor getAllNewNotification(int lastId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NOTIFICATION + " where " + IDNOTIFICATION + ">'" + lastId + "'", null);
        return res;
    }

    public boolean updcheckNotification(int idnotification) {
        String  idnotif= String.valueOf(idnotification);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTIFICATIONLU, 1);
        int result = db.update(TABLE_NOTIFICATION, contentValues, "" + IDNOTIFICATION + " = ? ", new String[]{idnotif});
        return true;
    }


    public Cursor getAllNotification() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " +
                NotificationDao.TABLE_NOTIFICATION, null);
        return res;
    }

    ////  delete notification
    public void deleteNotifiction(int idnotification) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.execSQL("delete from " + NotificationDao.TABLE_NOTIFICATION + " where " +
                NotificationDao.IDNOTIFICATION + "='" + idnotification + "'");

    }
}
