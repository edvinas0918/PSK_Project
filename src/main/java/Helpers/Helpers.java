package Helpers;

import Entities.Summerhouse;
import com.owlike.genson.Genson;
import org.joda.time.DateTime;
import org.joda.time.MonthDay;
import org.joda.time.format.DateTimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date date = null;
            try {
                date = format.parse(beginPeriod1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            summerhouseMap.remove(key);
            return date;
        }
        return null;
    }

}
