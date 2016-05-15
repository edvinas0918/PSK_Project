package models;

/**
 * Created by Mindaugas on 15/05/16.
 */
public class HouseServicePriceDTO {
    private int houseID;
    private int serviceID;
    private int price;

    public HouseServicePriceDTO() {
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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