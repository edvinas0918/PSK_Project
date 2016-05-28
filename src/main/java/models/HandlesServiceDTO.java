package models;

import Entities.Summerhousereservation;

import java.util.List;

/**
 * Created by Mindaugas on 27/05/16.
 */
public class HandlesServiceDTO {

    private Integer reservationID;
    private List<AdditionalServiceReservationDTO> additionalServiceReservationDTOs;

    public Integer getReservationID() {
        return reservationID;
    }

    public void setReservationID(Integer reservationID) {
        this.reservationID = reservationID;
    }

    public List<AdditionalServiceReservationDTO> getAdditionalServiceReservationDTOs() {
        return additionalServiceReservationDTOs;
    }

    public void setAdditionalServiceReservationDTOs(List<AdditionalServiceReservationDTO> additionalServiceReservationDTOs) {
        this.additionalServiceReservationDTOs = additionalServiceReservationDTOs;
    }
}
