package models;

import java.util.Date;

/**
 * Created by Mindaugas on 27/05/16.
 */
public class AdditionalServiceReservationDTO {

    int price;
    int additionalServiceID;
    Date date;

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
