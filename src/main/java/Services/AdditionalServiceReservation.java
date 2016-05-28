package services;

import entities.AdditionalService;
import entities.Payment;
import helpers.InsufficientFundsException;
import restControllers.AuthenticationControllerREST;
import models.AdditionalServiceReservationDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created by Mindaugas on 27/05/16.
 */
@Stateless
public class AdditionalServiceReservation {

    @Inject
    private AuthenticationControllerREST authenticationControllerREST;

    @Inject
    private IPaymentService paymentService;

    public Payment getPayment (AdditionalService additionalService, AdditionalServiceReservationDTO serviceReservationDTO) throws InsufficientFundsException {
        return paymentService.makePayment(authenticationControllerREST.getSessionUser(), serviceReservationDTO.getPrice(), "Paslauga \"" + additionalService.getName() + "\" nuo "  + serviceReservationDTO.getDate());
    }

}
