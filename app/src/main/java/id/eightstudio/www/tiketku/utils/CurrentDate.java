package id.eightstudio.www.tiketku.utils;

import java.util.Calendar;

public class CurrentDate {

    public static Calendar calendar   = Calendar.getInstance();
    public static int currentYear            = calendar.get(Calendar.YEAR); // current year
    public static int currentMonth           = calendar.get(Calendar.MONTH); // current month
    public static int currentDay             = calendar.get(Calendar.DAY_OF_MONTH); // current day


}
