package services;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import java.util.Date;

/**
 * Created by Edvinas.Barickis on 5/29/2016.
 */
public class DateService {

    public Date ConvertToLTTimezone(Date date) throws Exception {
        DateTime dt = new DateTime(date);
        DateTimeZone dtZone = DateTimeZone.forID("Europe/Helsinki");
        return dt.withZone(dtZone).toLocalDateTime().toDate();
    }
}
