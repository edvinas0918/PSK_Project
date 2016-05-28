package search.summerhouse;

import entities.Summerhouse;
import models.SummerhouseSearchDto;

import java.util.List;

/**
 * Created by Gintautas on 5/23/2016.
 */
public interface SummerhouseSeach {
    List<Summerhouse> search(List<Summerhouse> summerhouses, SummerhouseSearchDto searchDto);
}
