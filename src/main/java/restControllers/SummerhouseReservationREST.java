package restControllers;

import entities.Payment;
import entities.Summerhousereservation;
import helpers.DateTermException;
import interceptors.Authentication;
import interceptors.ExceptionHandler;
import models.ReservationInfoDTO;
import models.VacationInfoDTO;
import services.ClubMemberService;
import services.DateService;
import services.IPaymentService;
import services.SummerhouseReservation;
import models.AdditionalServiceReservationDTO;
import models.SummerhouseReservationDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Edvinas.Barickis on 5/15/2016.
 */
@ExceptionHandler
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

    @Inject
    DateService dateService;

    public SummerhouseReservationREST() {
        super(Summerhousereservation.class);
    }

    @GET
    @Path("{id}")
    @Authentication(role = {"Member", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Summerhousereservation find(@PathParam("id") Integer id) {
        Summerhousereservation summerhousereservation = super.find(id);
        return summerhousereservation;
    }

    @GET
    @Override
    @Authentication(role = {"Member", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Summerhousereservation> findAll() {
        return super.findAll();
    }

    @GET
    @Path("summerhouse/{id}")
    @Authentication(role = {"Member", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Summerhousereservation> findBySummerhouse(@PathParam("id") Integer id) {
        TypedQuery<Summerhousereservation> query =
                em.createNamedQuery("Summerhousereservation.findBySummerhouseId", Summerhousereservation.class).setParameter("id", id);
        return query.getResultList();
    }

    @GET
    @Path("reservationInfoForSummerhouse/{id}")
    @Authentication(role = {"Member", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReservationInfoDTO> findReservationInfoForSummerhouse(@PathParam("id") Integer id) {
        TypedQuery<ReservationInfoDTO> query =
                em.createNamedQuery("Summerhousereservation.findHouseReservationInfo", ReservationInfoDTO.class).setParameter("id", id);
        List<ReservationInfoDTO> objects = query.getResultList();
        return objects;
    }

    @GET
    @Path("vacationInfoForMember/{id}")
    @Authentication(role = {"Member", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<VacationInfoDTO> findVacationInfoForMember(@PathParam("id") Integer id) {
        TypedQuery<VacationInfoDTO> query =
                em.createNamedQuery("Summerhousereservation.findMemberVacationInfo", VacationInfoDTO.class).setParameter("id", id);
        List<VacationInfoDTO> objects = query.getResultList();
        return objects;
    }

    @GET
    @Path("clubmember/{id}")
    @Authentication(role = {"Member", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Summerhousereservation> findByClubmember(@PathParam("id") Integer id) {
        TypedQuery<Summerhousereservation> query =
                em.createNamedQuery("Summerhousereservation.findByClubmemberId", Summerhousereservation.class).setParameter("id", id);
        List<Summerhousereservation> summerhousereservations = query.getResultList();
        return summerhousereservations;
    }

    @DELETE
    @Path("{id}")
    @Authentication(role = {"Member", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") Integer id) {
        try{
            Summerhousereservation reservation = super.find(id);
            summerhouseReservation.cancelReservation(reservation, clubMemberService.getCurrentUser());
            em.remove(reservation);
        } catch(DateTermException exc){
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return Response.status(Response.Status.OK).build();
    }


    @POST
    @Authentication(role = {"Member", "Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response reserveSummerhouse(SummerhouseReservationDTO summerhouseReservationDTO) throws Exception{
        summerhouseReservationDTO = summerhouseReservation.setCorrectDates(summerhouseReservationDTO);
        Summerhousereservation reservation = summerhouseReservationDTO.getReservation();
        List<AdditionalServiceReservationDTO> reservationDTOs = summerhouseReservationDTO.getAdditionalServiceReservationDTOs();

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

        return Response.ok().build();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
