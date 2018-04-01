package id.eightstudio.www.tiketku.utils;

import java.util.Calendar;

public class CurrentDate {

    public static Calendar calendar   = Calendar.getInstance();
    public static int currentYear            = calendar.get(Calendar.YEAR);
    public static int currentMonth           = calendar.get(Calendar.MONTH);
    public static int currentDay             = calendar.get(Calendar.DAY_OF_MONTH);

}
