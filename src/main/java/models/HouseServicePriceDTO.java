package models;

/**
 * Created by Mindaugas on 15/05/16.
 */
public class HouseServicePriceDTO {

    private int id;
    private int serviceID;
    private int price;

    public HouseServicePriceDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

}
