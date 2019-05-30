package com.edischool.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    private static SimpleDateFormat sdFormat = getDateFormat();
    private static SimpleDateFormat sdtFormat = getDateTimeFormat();

    private static SimpleDateFormat getDateTimeFormat() {
        if (sdtFormat == null) {
            sdtFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
            sdtFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return sdtFormat;
    }

    private static SimpleDateFormat getDateFormat() {
        if (sdFormat == null) {
            sdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
        return sdFormat;
    }

    public static int getCurrentYear() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(new Date());
        return instance.get(1);
    }

    public static String getCurrentDate() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy, HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        return date;
    }
}
