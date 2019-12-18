package com.example.derich.bizwiz.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateAndTime {
    static SimpleDateFormat sdif = new SimpleDateFormat("yyyy.MM.dd  'at' HH:mm:ss z", Locale.US);
    public static String currentDateandTime = sdif.format(new Date());
    static SimpleDateFormat sdfAdd = new SimpleDateFormat("HH:mm:ss", Locale.US);
   public static String currentTimeOfAdd = sdfAdd.format(new Date());
    private static long timeMillis = System.currentTimeMillis();

    public static String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd",Locale.US);
        Calendar vCalendar = Calendar.getInstance();
        vCalendar.setTimeInMillis(timeMillis);
        return sdf.format(vCalendar.getTime());
    }
}

