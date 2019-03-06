package com.wuzp.commonlib.Utils;

import android.content.Context;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间相关的工具类
 *
 * @author wuzhenpeng03
 */
public class TimeUtils {

    public static final String HOUR = "HH:mm";
    public static final String YEAR_HOUR = "yyyy.MM.dd HH:mm";
    private static long lastTime;

    private TimeUtils() {
    }

    public static String[] convertDateTime(Context context, long milliSecond) {
        String dateString = null;
        String timeString = null;
        if (milliSecond <= 0L) {
            dateString = "";
            timeString = "";
            return new String[]{dateString, timeString};
        } else {
            Calendar destTime = Calendar.getInstance();
            destTime.setTimeInMillis(milliSecond);
            int month = destTime.get(2) + 1;
            int dayOfMonth = destTime.get(5);
            int minute = destTime.get(12);
            if (0 <= minute && minute < 10) {
                timeString = destTime.get(11) + ":0" + minute;
            } else {
                timeString = destTime.get(11) + ":" + minute;
            }

            Calendar nowTime = Calendar.getInstance();
            nowTime.setTimeInMillis(System.currentTimeMillis());
            dateString = month + "-" + dayOfMonth;
            return new String[]{dateString, timeString};
        }
    }

    public static String getFormatTime(String format, long time) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = new Date(time);
        return sdf.format(date);
    }

    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static String getYesterday() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date((new Date()).getTime() - 86400000L));
    }

    public static String getDateEN() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static String getDateMS() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        return format.format(new Date(System.currentTimeMillis()));
    }

    public static long currentTimeMillis() {
        long time = System.currentTimeMillis();
        if ((double) time < 1.0E11D) {
            time *= 1000L;
        }

        return time;
    }

    public static long currentTimeSeconds() {
        long time = System.currentTimeMillis();
        if ((double) time > 1.0E11D) {
            time /= 1000L;
        }

        return time;
    }

    public static boolean isToday(long time) {
        Calendar currentTime = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.set(1, currentTime.get(1));
        today.set(2, currentTime.get(2));
        today.set(5, currentTime.get(5));
        today.set(11, 0);
        today.set(12, 0);
        today.set(13, 0);
        currentTime.setTimeInMillis(time);
        return currentTime.after(today);
    }

    public static String format(long ms) {
        int ss = 1;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;
        long day = ms / (long) dd;
        long hour = (ms - day * (long) dd) / (long) hh;
        long minute = (ms - day * (long) dd - hour * (long) hh) / (long) mi;
        long second = (ms - day * (long) dd - hour * (long) hh - minute * (long) mi) / (long) ss;
        long milliSecond = ms - day * (long) dd - hour * (long) hh - minute * (long) mi - second * (long) ss;
        if (day < 10L) {
            (new StringBuilder()).append("0").append(day).toString();
        } else {
            (new StringBuilder()).append("").append(day).toString();
        }

        String strHour = hour < 10L ? "" + hour : "" + hour;
        String strMinute = minute < 10L ? "0" + minute : "" + minute;
        if (second < 10L) {
            (new StringBuilder()).append("0").append(second).toString();
        } else {
            (new StringBuilder()).append("").append(second).toString();
        }

        String strMilliSecond = milliSecond < 10L ? "0" + milliSecond : "" + milliSecond;
        if (milliSecond < 100L) {
            (new StringBuilder()).append("0").append(strMilliSecond).toString();
        } else {
            (new StringBuilder()).append("").append(strMilliSecond).toString();
        }

        return strHour + ":" + strMinute;
    }

    public static String formatHour(long ms) {
        double result = (double) ((float) ms * 1.0F);
        int ss = 1;
        int mi = 60 * ss;
        int hh = 60 * mi;
        BigDecimal bd = new BigDecimal(result / (double) hh);
        bd = bd.setScale(1, 5);
        return bd.toString();
    }

    public static boolean isNewDay(long lastTime) {
        long curTime = System.currentTimeMillis();
        boolean newDay = !getDate(lastTime).equals(getDate(curTime));
        return newDay;
    }

    private static String getDate(long timeStamp) {
        String strDate = "";

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(timeStamp);
            strDate = dateFormat.format(date);
        } catch (Exception var5) {
            ;
        }

        return strDate;
    }
}

