package Helpers;

import com.owlike.genson.Genson;
import org.joda.time.MonthDay;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Mindaugas on 23/04/16.
 */
public class Helpers {

    private static Genson instance = null;
    private Helpers() {
        // Exists only to defeat instantiation.
    }
    public static Genson getGensonInstance() {
        if(instance == null) {
            instance = new Genson();
        }
        return instance;
    }

    public static MonthDay monthDayFromDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.get(Calendar.DAY_OF_MONTH);
        calendar.get(Calendar.MONTH);
        return new MonthDay(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static Date dateFromMonthDay(MonthDay monthDay) {
        Date date = new GregorianCalendar(0, monthDay.getMonthOfYear(), monthDay.getDayOfMonth()).getTime();
        return date;
    }

}
