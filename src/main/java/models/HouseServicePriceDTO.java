package models;

import Entities.Tax;

/**
 * Created by Mindaugas on 15/05/16.
 */
public class HouseServicePriceDTO {
    private int houseID;
    private int serviceID;
    private Tax tax;

    public HouseServicePriceDTO() {
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        this.tax = tax;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

}
