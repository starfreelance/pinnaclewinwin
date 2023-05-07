package com.pinnacle.winwin.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    /*public static final String ADD_NEW_DATE_FORMAT = "yyyy-dd-MM";*/
    /*public static final String ADD_NEW_DATE_FORMAT = "YYYY-MM-dd";*/
    public static final String ADD_NEW_DATE_FORMAT = "yyyy-MM-dd";
    public static final String BAAZAAR_REMAINING_TIME_FORMAT = "HH:mm:ss";
    public static final String UNIX_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String TRANSACTION_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";
    public static final String DATE_FORMAT_24_hrs = "yyyy-MM-dd HH:mm:ss";
    public static final String DISPLAY_DOB_DATE_FORMAT = "dd MMM, yyyy";
    public static final String DOB_DATE_FORMAT = "dd-MM-yyyy";
    public static final String CHAT_DATE_FORMAT = "MMM dd, yyyy hh:mm a";

    public static String getStringFormattedDate(Date date, String dateFormat) {

        DateFormat mDateFormat = new SimpleDateFormat(dateFormat, Locale.getDefault());
        if (date != null) {
            return mDateFormat.format(date);
        } else {
            return "";
        }
    }

    public static Date getDateFromString(String dateString, SimpleDateFormat dateFormat) {
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;

    }
}
