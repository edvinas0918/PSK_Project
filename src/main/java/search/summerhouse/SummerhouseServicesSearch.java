package search.summerhouse;

import entities.Summerhouse;
import restControllers.HouseServicePriceFacadeREST;
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
public class SummerhouseServicesSearch implements SummerhouseSeach{
    @Inject
    @Delegate
    @Any
    SummerhouseSeach summerhouseSeach;

    @Inject
    HouseServicePriceFacadeREST houseServicePriceFacadeREST;

    @Override
    public List<Summerhouse> search(List<Summerhouse> summerhouses, SummerhouseSearchDto searchDto) {
        List<Summerhouse> result = summerhouseSeach.search(summerhouses, searchDto);

        if(searchDto.additionalServices != null){
            result.removeIf(p -> !houseServicePriceFacadeREST.getSummerhouseAdditionalServices(p.getId()).containsAll(searchDto.additionalServices));
        }
        return result;
    }
}
