package models;

import Entities.Summerhousereservation;

import java.util.List;

/**
 * Created by Mindaugas on 27/05/16.
 */
public class SummerhouseReservationDTO {

    private Summerhousereservation reservation;
    private List<AdditionalServiceReservationDTO> additionalServiceReservationDTOs;

    public Summerhousereservation getReservation() {
        return reservation;
    }

    public void setReservation(Summerhousereservation reservation) {
        this.reservation = reservation;
    }

    public List<AdditionalServiceReservationDTO> getAdditionalServiceReservationDTOs() {
        return additionalServiceReservationDTOs;
    }

    public void setAdditionalServiceReservationDTOs(List<AdditionalServiceReservationDTO> additionalServiceReservationDTOs) {
        this.additionalServiceReservationDTOs = additionalServiceReservationDTOs;
    }

}
