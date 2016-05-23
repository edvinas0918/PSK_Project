package search.summerhouse;

import Entities.Summerhouse;
import Services.SummerhouseReservation;
import models.SummerhouseSearchDto;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Gintautas on 5/23/2016.
 */
@Decorator
public class SummerhouseAvailabilitySearch implements SummerhouseSeach {
    @Inject
    @Delegate
    @Any
    SummerhouseSeach summerhouseSeach;

    @Inject
    SummerhouseReservation summerhouseReservationService;

    @Override
    public List<Summerhouse> search(List<Summerhouse> summerhouses, SummerhouseSearchDto searchDto) {
        List<Summerhouse> result = summerhouseSeach.search(summerhouses, searchDto);
        if(searchDto.fromDate != null && searchDto.untilDate != null){
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = null;
            Date untilDate = null;
            try {
                fromDate = format.parse(searchDto.fromDate);
                untilDate = format.parse(searchDto.untilDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(fromDate != null && untilDate != null) {
                result = summerhouseReservationService.getAvailableSummerhousesInPeriod(summerhouses, fromDate, untilDate);
            }
        }
        return result;
    }
}
