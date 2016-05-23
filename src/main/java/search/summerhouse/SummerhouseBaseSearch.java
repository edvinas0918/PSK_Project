package search.summerhouse;

import Entities.Summerhouse;
import models.SummerhouseSearchDto;

import javax.ejb.Stateless;
import java.util.List;

/**
 * Created by Gintautas on 5/23/2016.
 */
@Stateless
public class SummerhouseBaseSearch implements SummerhouseSeach {
    @Override
    public List<Summerhouse> search(List<Summerhouse> summerhouses, SummerhouseSearchDto searchDto) {
        return summerhouses;
    }
}
