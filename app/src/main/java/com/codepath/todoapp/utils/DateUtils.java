package com.codepath.todoapp.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static String getDateString(Date date){
        SimpleDateFormat MMMddyyyyFormat = new SimpleDateFormat("MMM dd, yyyy");
        String dateToString = MMMddyyyyFormat.format(date);
        return dateToString;
    }

    public static String getDayString(Date date){
        SimpleDateFormat ddFormat = new SimpleDateFormat("dd");
        String day= ddFormat.format(date);
        return day;
    }

    public static String getMonthString(Date date){
        SimpleDateFormat MMMMFormat = new SimpleDateFormat("MMMM");
        String month = MMMMFormat.format(date);
        return month;
    }

    public static String getYearString(Date date){
        SimpleDateFormat yyFormat = new SimpleDateFormat("yyyy");
        String dateToString = yyFormat.format(date);
        return dateToString;
    }

    public static String getTime12(String time){
        int newTime = 0;
        String exactTime;
        //time = time.substring(0,2);
        //System.out.println("#######" + time);
        time = time.substring(0, time.indexOf(':')!=-1?time.indexOf(':'):time.length());//IN windows Date comes as 10:00
        if(Integer.parseInt(time) > 12){
            newTime = Integer.parseInt(time.substring(0, 2)) -12;
            exactTime = Integer.toString(newTime) + ":00 PM";
        }else if(Integer.parseInt(time) == 12){
            exactTime = time + ":00 PM";
        }else{
            exactTime = time + ":00 AM";
        }
        return exactTime;
    }

    public static int getYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}
