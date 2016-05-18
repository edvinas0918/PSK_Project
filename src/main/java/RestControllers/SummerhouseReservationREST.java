package RestControllers;

import Entities.Summerhousereservation;
import Services.SummerhouseReservation;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    SummerhouseFacadeREST summerhouseFacadeREST;

    @Inject
    ClubmemberFacadeREST clubmemberFacadeREST;

    @Inject
    AuthenticationControllerREST authenticationControllerREST;

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
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response reserveSummerhouse(Summerhousereservation reservation) {
        JSONObject responseBody = new JSONObject();

        try {
            summerhouseReservation.validatePeriod(reservation.getFromDate(), reservation.getUntilDate());
            summerhouseReservation.checkAvailabilityPeriod(
                    reservation.getSummerhouse(),
                    reservation.getFromDate(),
                    reservation.getUntilDate(),
                    this.findBySummerhouse(reservation.getSummerhouse().getId()));

            reservation.setMember(authenticationControllerREST.getSessionUser());
            summerhouseReservation.checkReservationGroup(reservation);
            super.create(reservation);
        } catch (Exception ex) {
            responseBody.put("errorMessage", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseBody.toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        return Response.ok().build();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
