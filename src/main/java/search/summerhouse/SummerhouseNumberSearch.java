package search.summerhouse;

import Entities.Summerhouse;
import models.SummerhouseSearchDto;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by Gintautas on 5/23/2016.
 */
@Decorator
public class SummerhouseNumberSearch implements SummerhouseSeach {
    @Inject
    @Delegate
    @Any
    SummerhouseSeach summerhouseSeach;

    @Override
    public List<Summerhouse> search(List<Summerhouse> summerhouses, SummerhouseSearchDto searchDto) {
        List<Summerhouse> result = summerhouseSeach.search(summerhouses, searchDto);
        if(searchDto.number != null){
            result.removeIf(p -> p.getNumber() != searchDto.number);
        }
        return result;
    }
}
