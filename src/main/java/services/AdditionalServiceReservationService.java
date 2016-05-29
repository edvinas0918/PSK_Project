package services;

import entities.AdditionalService;
import entities.Additionalservicereservation;
import entities.Payment;
import helpers.InsufficientFundsException;
import org.joda.time.DateTime;
import restControllers.AuthenticationControllerREST;
import models.AdditionalServiceReservationDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.util.List;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Mindaugas on 27/05/16.
 */
@Stateless
public class AdditionalServiceReservationService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU", type=TRANSACTION)
    private EntityManager em;

    @Inject
    private AuthenticationControllerREST authenticationControllerREST;

    @Inject
    private IPaymentService paymentService;

    public Payment getPayment (AdditionalService additionalService, AdditionalServiceReservationDTO serviceReservationDTO) throws InsufficientFundsException {
        String dateString = new DateTime(serviceReservationDTO.getDate()).toString("yyyy-MM-dd");
        return paymentService.makePayment(authenticationControllerREST.getSessionUser(), serviceReservationDTO.getPrice(),
                "Paslauga \"" + additionalService.getName() + "\" nuo "  + dateString);
    }

    public void delete(Additionalservicereservation reservation){
        reservation.setSummerhouseReservation(null);
        reservation.setAdditionalService(null);
        em.remove(reservation);
    }

    public List<Additionalservicereservation> getAdditionalServiceReservations(int reservationID){
        TypedQuery<Additionalservicereservation> query =
                em.createNamedQuery("Additionalservicereservation.findBySummerhouseReservationID",
                        Additionalservicereservation.class).setParameter("summerhouseReservationID", reservationID);
        List<Additionalservicereservation> additionalservicereservations = query.getResultList();
        return additionalservicereservations;
    }

}
