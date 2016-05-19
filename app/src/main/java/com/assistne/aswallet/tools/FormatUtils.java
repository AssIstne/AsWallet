package com.assistne.aswallet.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by assistne on 16/1/5.
 */
public class FormatUtils {

    private static SimpleDateFormat HOUR_FORMAT;
    private static SimpleDateFormat DATE_FORMAT;


    static {
        HOUR_FORMAT = new SimpleDateFormat("h:mm a", Locale.US);
        DATE_FORMAT = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.US);
    }

    public static String dateToText(long date) {
        String res;
        Calendar nowCalendar = Calendar.getInstance();
        long delta = nowCalendar.getTimeInMillis() - date;
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTimeInMillis(date);
        Date targetDate = targetCalendar.getTime();
        if (delta < 3600000) {
            res = (delta/60000 + 1) + " minutes ago, " + HOUR_FORMAT.format(targetDate);
        } else if (delta < 10800000) {
            res = delta/3600000 + " hours ago, " + HOUR_FORMAT.format(targetDate);
        } else if (delta < 86400000 && targetCalendar.get(Calendar.DAY_OF_MONTH) == nowCalendar.get(Calendar.DAY_OF_MONTH)) {
            res = "Today, " + HOUR_FORMAT.format(targetDate);
        } else if (delta < 172800000 && (nowCalendar.get(Calendar.DAY_OF_MONTH) - targetCalendar.get(Calendar.DAY_OF_MONTH)) == 1) {
            res = "Yesterday, " + HOUR_FORMAT.format(targetDate);
        } else {
            res = DATE_FORMAT.format(targetDate);
        }

        return res;
    }
}
