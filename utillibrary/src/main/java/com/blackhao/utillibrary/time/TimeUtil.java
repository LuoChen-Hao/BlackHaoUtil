package com.blackhao.utillibrary.time;

import android.text.format.Time;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Author ： BlackHao
 * Time : 2016/8/13 14:05
 * Description : 时间类型转换工具类
 */
public class TimeUtil {

    /**
     * 默认的时间 String 模式
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取当前时间，并返回 String类型的数据
     *
     * @return 当前时间
     */
    public static String getCurrentTime() {
        return getCurrentTime(DEFAULT_PATTERN);
    }

    /**
     * 获取当前时间，并返回 String类型的数据
     *
     * @return 当前时间
     */
    public static String getCurrentTime(String pattern) {
        String currentTime;
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(pattern, Locale.CHINA);
        currentTime = formatter.format(curDate);
        return currentTime;
    }

    /**
     * 通过传入的 Date类型时间，并返回String类型的数据
     */
    public static String formatDateToString(Date curDate, String pattern) {
        String time;
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat(pattern, Locale.CHINA);
        time = formatter.format(curDate);
        return time;
    }

    /**
     * 通过传入的 Date类型时间，并返回 String类型的数据
     */
    public static String formatDateToString(Date curDate) {
        return formatDateToString(curDate, DEFAULT_PATTERN);
    }

    /**
     * 根据毫秒数返回年月日时分秒
     */
    public static int[] formatMsecToTimeInfo(long msec) {
        int[] dates = new int[6];
        Time t = new Time();
        t.set(msec);
        dates[0] = t.year;
        dates[1] = t.month + 1;
        dates[2] = t.monthDay;
        dates[3] = t.hour;
        dates[4] = t.minute;
        dates[5] = t.second;
        return dates;
    }

    /**
     * 通过枚举来返回当前的 int类型的时间类型（年，月，日，时，分，秒，周几）
     */
    public static int getTimeType(long msec, TimeType type) {
        int time = 0;
        Time t = new Time();
        t.set(msec); // 取得当前系统时间
        switch (type) {
            case YEAR:
                time = t.year;
                break;
            case MONTH:
                time = t.month;
                break;
            case DAY:
                time = t.monthDay;
                break;
            case HOUR:
                time = t.hour;
                break;
            case MINUTE:
                time = t.minute;
                break;
            case SECOND:
                time = t.second;
                break;
            case WEEKDAY:
                time = t.weekDay;
                break;
        }
        return time;
    }

    public enum TimeType {
        YEAR, MONTH, DAY, HOUR, MINUTE, SECOND, WEEKDAY
    }

    /**
     * 将 String("yyyy-MM-dd HH:mm:ss")转换成 Date
     */
    public static Date formatStrToDate(String str) {
        return formatStrToDate(str, DEFAULT_PATTERN);
    }

    /**
     * 将 String转换成 Date
     */
    public static Date formatStrToDate(String str, String pattern) {
        Date date;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.CHINA);
            date = formatter.parse(str);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将毫秒转化成固定格式的时间
     */
    public static String formatMsecToString(long msec) {
        return formatMsecToString(msec, DEFAULT_PATTERN);

    }

    /**
     * 将毫秒转化成固定格式的时间
     */
    public static String formatMsecToString(long msec, String pattern) {
        Date date = new Date();
        try {
            date.setTime(msec);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * 将字符串转化成毫秒
     */
    public static long formatStringToMsec(String str, String pattern) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat(pattern, Locale.CHINA).parse(str));
            return c.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将字符串转化成毫秒
     */
    public static long formatStringToMsec(String str) {
        return formatStringToMsec(str, DEFAULT_PATTERN);
    }

    /**
     * 毫秒转成 时：分：秒
     */
    public static String formatMsec(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;

        long hour = ms / hh;
        long minute = (ms - hour * hh) / mi;
        long second = (ms - hour * hh - minute * mi) / ss;

        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒

        return strHour + ":" + strMinute + ":" + strSecond;
    }

    /**
     * 判断日期是否在两个指定日期之内
     *
     * @param targetDate 需要判断的日期
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @param pattern    时间模式
     */
    public static boolean isBetweenTwoDays(String targetDate, String startDate, String endDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.CHINA);
        try {
            Date dt1 = df.parse(startDate);
            Date dt2 = df.parse(endDate);
            Date target = df.parse(targetDate);
            return dt1.getTime() <= target.getTime() && dt2.getTime() >= target.getTime();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * 判断日期是否在两个指定日期之内
     *
     * @param targetDate 需要判断的日期
     * @param startDate  开始日期
     * @param endDate    结束日期
     */
    public static boolean isBetweenTwoDays(String targetDate, String startDate, String endDate) {
        return isBetweenTwoDays(targetDate, startDate, endDate, DEFAULT_PATTERN);
    }

    /**
     * 获取指定网站的日期时间(必须在子线程调用),用于获取网络时间
     */
    public static long getWebsiteTime() {
        try {
            String webUrl[] = {"http://www.bjtime.cn", //bjTime
                    "http://www.baidu.com", //百度
                    "http://www.taobao.com",//淘宝
                    "http://www.ntsc.ac.cn", //中国科学院国家授时中心
                    "http://www.time.ac.cn/"};
            for (String aWebUrl : webUrl) {
                URL url = new URL(aWebUrl);// 取得资源对象
                URLConnection uc = url.openConnection();// 生成连接对象
                uc.setConnectTimeout(5 * 1000);
                uc.connect();// 发出连接
                // 读取网站日期时间
                if (uc.getDate() > formatStringToMsec("2016-01-01 00:00:00")) {
                    //只有网络时间大于2016/1/1才表示时间正常
                    return uc.getDate();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
