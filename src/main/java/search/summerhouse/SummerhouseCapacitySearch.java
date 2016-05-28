package search.summerhouse;

import entities.Summerhouse;
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
public class SummerhouseCapacitySearch implements SummerhouseSeach{
    @Inject
    @Delegate
    @Any
    SummerhouseSeach summerhouseSeach;

    @Override
    public List<Summerhouse> search(List<Summerhouse> summerhouses, SummerhouseSearchDto searchDto) {
        List<Summerhouse> result = summerhouseSeach.search(summerhouses, searchDto);
        if(searchDto.capacity != null){
            result.removeIf(p -> p.getCapacity() != searchDto.capacity);
        }
        return result;
    }
}
