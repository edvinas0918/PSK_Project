package RestControllers;

import Entities.Additionalservicereservation;
import Entities.Payment;
import Entities.Summerhousereservation;
import Helpers.DateTermException;
import Services.ClubMemberService;
import Services.IPaymentService;
import Services.SummerhouseReservation;
import models.AdditionalServiceReservationDTO;
import models.SummerhouseReservationDTO;
import org.joda.time.DateTime;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Edvinas.Barickis on 5/15/2016.
 */
@Stateless
@Path("reservation")
public class SummerhouseReservationREST extends AbstractFacade<Summerhousereservation> {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    SummerhouseReservation summerhouseReservation;

    @Inject
    ClubMemberService clubMemberService;

    @Inject
    SummerhouseFacadeREST summerhouseFacadeREST;

    @Inject
    ClubmemberFacadeREST clubmemberFacadeREST;

    @Inject
    AdditionalServiceFacadeREST additionalServiceFacadeREST;

    @Inject
    AuthenticationControllerREST authenticationControllerREST;

    @Inject
    AdditionalservicereservationFacadeREST additionalServiceReservationFacadeREST;

    @Inject
    IPaymentService paymentService;

    public SummerhouseReservationREST() {
        super(Summerhousereservation.class);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Summerhousereservation find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public List<Summerhousereservation> findAll() {
        return super.findAll();
    }

    @GET
    @Path("summerhouse/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Summerhousereservation> findBySummerhouse(@PathParam("id") Integer id) {
        TypedQuery<Summerhousereservation> query =
                em.createNamedQuery("Summerhousereservation.findBySummerhouseId", Summerhousereservation.class).setParameter("id", id);
        return query.getResultList();
    }

    @GET
    @Path("clubmember/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Summerhousereservation> findByClubmember(@PathParam("id") Integer id) {
        TypedQuery<Summerhousereservation> query =
                em.createNamedQuery("Summerhousereservation.findByClubmemberId", Summerhousereservation.class).setParameter("id", id);
        return query.getResultList();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") Integer id) {
        try{
            Summerhousereservation reservation = super.find(id);
            summerhouseReservation.cancelReservation(reservation, clubMemberService.getCurrentUser());
            super.remove(reservation);
        } catch(DateTermException exc){
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return Response.status(Response.Status.OK).build();
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response reserveSummerhouse(SummerhouseReservationDTO summerhouseReservationDTO) {
        Summerhousereservation reservation = summerhouseReservationDTO.getReservation();
        List<AdditionalServiceReservationDTO> reservationDTOs = summerhouseReservationDTO.getAdditionalServiceReservationDTOs();

        JSONObject responseBody = new JSONObject();

        try {
            summerhouseReservation.checkMembership();
            summerhouseReservation.validatePeriod(reservation.getFromDate(), reservation.getUntilDate());
            summerhouseReservation.checkAvailabilityPeriod(
                    reservation,
                    this.findBySummerhouse(reservation.getSummerhouse().getId()));

            reservation.setMember(authenticationControllerREST.getSessionUser());
            summerhouseReservation.checkReservationGroup(reservation);

            Payment payment = summerhouseReservation.getPayment(reservation);
            reservation.setPayment(payment);

            super.create(reservation);
            additionalServiceReservationFacadeREST.createServiceReservationsForSummerhouse(reservation, reservationDTOs);
        } catch (Exception ex) {
            responseBody.put("errorMessage", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseBody.toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        return Response.ok().build();
    }

    @GET
    @Path("additionalServices/{summerhouseReservationID}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Additionalservicereservation> getAdditionalServiceReservations(@PathParam("summerhouseReservationID") Integer id) {
        TypedQuery<Additionalservicereservation> query =
                em.createNamedQuery("Additionalservicereservation.findBySummerhouseReservationID", Additionalservicereservation.class).setParameter("summerhouseReservationID", id);
        return query.getResultList();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
