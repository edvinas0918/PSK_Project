package models;

import java.util.Date;

/**
 * Created by Mindaugas on 30/05/16.
 */
public class ReservationInfoDTO {

    private String firstName;
    private String lastName;
    private Date fromDate;
    private Date untilDate;

    public ReservationInfoDTO(String firstName, String lastName, Date fromDate, Date untilDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fromDate = fromDate;
        this.untilDate = untilDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

}
