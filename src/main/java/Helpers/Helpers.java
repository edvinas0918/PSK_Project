package Helpers;

import Entities.Summerhouse;
import com.owlike.genson.Genson;
import org.joda.time.DateTime;
import org.joda.time.MonthDay;
import org.joda.time.format.DateTimeFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

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
        return new MonthDay(calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }


    public static Summerhouse getSummerhouseWithDates(Map<Object, Object> summerhouseMap){
        Date endPeriod = parseDateFromKey(summerhouseMap, "endPeriod");
        Date beginPeriod = parseDateFromKey(summerhouseMap, "beginPeriod");
        String serialized = getGensonInstance().serialize(summerhouseMap);
        Summerhouse summerhouse = getGensonInstance().deserialize(serialized, Summerhouse.class);
        summerhouse.setEndPeriod(endPeriod);
        summerhouse.setBeginPeriod(beginPeriod);
        return summerhouse;
    }

    private static Date parseDateFromKey(Map<Object, Object> summerhouseMap, String key) {
        if(summerhouseMap.containsKey(key)) {
            String beginPeriod1 = (String)summerhouseMap.get(key);
            String[] parts = beginPeriod1.split("T");
            DateTime dateTime = DateTime.parse(parts[0],DateTimeFormat.forPattern("yyyy-MM-dd"));
            summerhouseMap.remove(key);
            return dateTime.toDate();
        }
        return null;
    }

    public static Date dateFromMonthDay(MonthDay monthDay) {
        Date date = new GregorianCalendar(0, monthDay.getMonthOfYear(), monthDay.getDayOfMonth()).getTime();
        return date;
    }

}
