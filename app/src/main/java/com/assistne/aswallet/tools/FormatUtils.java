package com.assistne.aswallet.tools;

import com.assistne.aswallet.R;
import com.assistne.aswallet.component.MyApplication;

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
        HOUR_FORMAT = new SimpleDateFormat("kk:mm", Locale.CHINA);
        DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    }

    /** 把日期转换成显示在首页列表的时间文本 */
    public static String dateToText(long date) {
        String res;
        Calendar nowCalendar = Calendar.getInstance();
        long delta = nowCalendar.getTimeInMillis() - date;
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTimeInMillis(date);
        Date targetDate = targetCalendar.getTime();
        if (delta < 86400000 && targetCalendar.get(Calendar.DAY_OF_MONTH) == nowCalendar.get(Calendar.DAY_OF_MONTH)) {
            // 一天内
            res = String.format(MyApplication.getStaticContext().getString(R.string.today), HOUR_FORMAT.format(targetDate));
        } else if (delta < 172800000 && (nowCalendar.get(Calendar.DAY_OF_MONTH) - targetCalendar.get(Calendar.DAY_OF_MONTH)) == 1) {
            // 昨天
            res = String.format(MyApplication.getStaticContext().getString(R.string.yesterday), HOUR_FORMAT.format(targetDate));
        } else {
            // 前天之前
            res = DATE_FORMAT.format(targetDate);
        }

        return res;
    }
}
