package models;

import Entities.AdditionalService;

import java.util.Collection;

/**
 * Created by Gintautas on 5/23/2016.
 */
public class SummerhouseSearchDto {
    public String fromDate;
    public String untilDate;
    public Integer number;
    public Collection<AdditionalService> additionalServices;
    public Integer capacity;
}
