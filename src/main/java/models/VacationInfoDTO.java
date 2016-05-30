package models;

import java.util.Date;

/**
 * Created by Mindaugas on 30/05/16.
 */
public class VacationInfoDTO {

    private int summerhouseNumber;
    private Date fromDate;
    private Date untilDate;

    public VacationInfoDTO(int summerhouseNumber, Date fromDate, Date untilDate) {
        this.summerhouseNumber = summerhouseNumber;
        this.fromDate = fromDate;
        this.untilDate = untilDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public int getSummerhouseNumber() {
        return summerhouseNumber;
    }

    public void setSummerhouseNumber(int summerhouseNumber) {
        this.summerhouseNumber = summerhouseNumber;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

}
