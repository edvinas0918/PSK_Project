package models;

import Entities.AdditionalService;

/**
 * Created by Gintautas on 5/23/2016.
 */
public class SummerhouseSearchDto {
    public String fromDate;
    public String untilDate;
    public Integer number;
    public AdditionalService[] additionalServices;
    public Integer capacity;
}
