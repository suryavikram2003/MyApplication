package com.example.myapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeHelper {
    
    private static final String DATE_FORMAT = "MMM dd, yyyy";
    private static final String TIME_FORMAT = "HH:mm";
    private static final String DATE_TIME_FORMAT = "MMM dd, yyyy HH:mm";
    
    public static String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    public static String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    public static String formatDateTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
    
    public static String getCurrentDateTime() {
        return formatDateTime(System.currentTimeMillis());
    }
}