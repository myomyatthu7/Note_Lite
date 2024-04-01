package com.stone.mmt.app.notelite.library;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class CurrentDateTime {
    public static String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();
        String formattedDate = dateFormat.format(currentDate);
        String formattedTime = timeFormat.format(currentDate);
        return formattedDate+"  "+formattedTime;
    }
}
