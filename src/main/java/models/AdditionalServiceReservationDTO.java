package models;

import java.util.Date;

/**
 * Created by Mindaugas on 27/05/16.
 */
public class AdditionalServiceReservationDTO {

    private int price;
    private int additionalServiceID;
    private Integer serviceReservationID;
    private Date date;

    public Integer getServiceReservationID() {
        return serviceReservationID;
    }

    public void setServiceReservationID(Integer serviceReservationID) {
        this.serviceReservationID = serviceReservationID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAdditionalServiceID() {
        return additionalServiceID;
    }

    public void setAdditionalServiceID(int additionalServiceID) {
        this.additionalServiceID = additionalServiceID;
    }

}
