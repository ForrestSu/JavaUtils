package com.sunquan.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    /**
     * @return Integer CurDate YYYYMMDD
     */
    public static int CurDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int output = year * 10000 + month * 100 + day;
        return output;
    }

    /**
     * @return String CurDate YYYYMMDD
     */
    public static String CurDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }

    /**
     * @return String CurDate YYYY-MM-DD
     */
    public static String CurDateHunman() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String CurTimeHuman() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String CurDateTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date());
    }

    public static int CurTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        return Integer.parseInt(sdf.format(new Date()));
    }

    /**
     * Calculate the millisecond value at the specified point in time.
     * @param hour
     * @param min
     * @param sec
     * @param bNextDay
     * @return
     */
    public static long TimeSpanMS(int hour, int min, int sec, boolean bNextDay) {
        final Calendar expect = Calendar.getInstance();
        final long nowms = expect.getTimeInMillis();
        expect.set(Calendar.HOUR_OF_DAY, hour);
        expect.set(Calendar.MINUTE, min);
        expect.set(Calendar.SECOND, sec);
        expect.set(Calendar.MILLISECOND, 0);
        final long expectms = expect.getTimeInMillis();
        long span = 0;
        if (expectms >= nowms) {
            span = (expectms - nowms) + (bNextDay ? 24 * 3600 * 1000 : 0); // 1day
        } else {
            span = 24 * 3600 * 1000 - (nowms - expectms);
        }
        return span;
    }

    public static long TimeSpanMS(String hhmmss, boolean bNextDay) {
        String[] arr = hhmmss.split(":");
        if (arr.length != 3) {
            System.err.println("invaild timepoint format! timepoint is " + hhmmss);
            return 5; // 5 seconds
        }
        int hour = Integer.valueOf(arr[0]);
        int minute = Integer.valueOf(arr[1]);
        int sec = Integer.valueOf(arr[2]);
        return TimeSpanMS(hour, minute, sec, bNextDay);
    }

    public static String MsToHuman(int tm) {
        tm /= 1000;
        int sec = tm % 60;
        tm /= 60;
        int min = tm % 60;
        tm /= 60;
        int hour = tm;
        return String.format("%d hour %d min %d seconds", hour, min, sec);
    }
}