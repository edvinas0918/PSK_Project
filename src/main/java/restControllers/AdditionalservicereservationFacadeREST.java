package restControllers;

import entities.AdditionalService;
import entities.Additionalservicereservation;
import entities.Payment;
import entities.Summerhousereservation;
import helpers.InsufficientFundsException;
import services.AdditionalServiceReservation;
import models.AdditionalServiceReservationDTO;
import models.HandlesServiceDTO;
import org.json.JSONObject;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Mindaugas
 */
@Stateless
@Path("servicesReservation")
public class AdditionalservicereservationFacadeREST extends AbstractFacade<Additionalservicereservation> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private AdditionalServiceFacadeREST additionalServiceFacadeREST;

    @Inject
    private AdditionalServiceReservation additionalServiceReservation;

    @Inject
    private SummerhouseReservationREST summerhouseReservationREST;

    public AdditionalservicereservationFacadeREST() {
        super(Additionalservicereservation.class);
    }

    public void createServiceReservationsForSummerhouse(Summerhousereservation summerhouseReservation, List<AdditionalServiceReservationDTO> additionalServiceReservationDTOs) throws InsufficientFundsException{
        if (!additionalServiceReservationDTOs.isEmpty()){
            for (AdditionalServiceReservationDTO serviceReservationDTO : additionalServiceReservationDTOs) {
                create(getServiceReservationForDTO(serviceReservationDTO, summerhouseReservation));
            }
        }
    }

    private Additionalservicereservation getServiceReservationForDTO(AdditionalServiceReservationDTO serviceReservationDTO, Summerhousereservation summerhouseReservation) throws InsufficientFundsException {
        AdditionalService additionalService = additionalServiceFacadeREST.find(serviceReservationDTO.getAdditionalServiceID());
        Payment payment = additionalServiceReservation.getPayment(additionalService, serviceReservationDTO);
        return new Additionalservicereservation(serviceReservationDTO.getDate(), summerhouseReservation, additionalService, payment);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Additionalservicereservation entity) {
        try {
            super.create(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("handleServices")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateReservationServices(HandlesServiceDTO handlesServiceDTO) {
        JSONObject responseBody = new JSONObject();
        List<AdditionalServiceReservationDTO> additionalServiceReservationDTOs = handlesServiceDTO.getAdditionalServiceReservationDTOs();
        List<Additionalservicereservation> currentReservations = getAdditionalServiceReservations(handlesServiceDTO.getReservationID());
        Summerhousereservation summerhouseReservation = summerhouseReservationREST.find(handlesServiceDTO.getReservationID());

        try {
            for (AdditionalServiceReservationDTO serviceReservationDTO : additionalServiceReservationDTOs) {
                Additionalservicereservation additionalservicereservation = null;
                if(serviceReservationDTO.getServiceReservationID() != null) {
                    additionalservicereservation = find(serviceReservationDTO.getServiceReservationID());
                    additionalservicereservation.setServiceStart(serviceReservationDTO.getDate());
                    edit(additionalservicereservation);
                } else {
                    additionalservicereservation = getServiceReservationForDTO(serviceReservationDTO, summerhouseReservation);
                    create(additionalservicereservation);
                }
                if (currentReservations.contains(additionalservicereservation)) {
                    currentReservations.remove(additionalservicereservation);
                }
            }

        } catch (Exception ex) {
            responseBody.put("errorMessage", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseBody.toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        for (Additionalservicereservation reservationToRemove : currentReservations) {
            Additionalservicereservation additionalServiceReservation = find(reservationToRemove.getId());
            additionalServiceReservation.setSummerhouseReservation(null);
            additionalServiceReservation.setAdditionalService(null);

            super.remove(additionalServiceReservation);
        }
        return Response.ok().build();
    }

    @GET
    @Path("reservedServicesFor/{summerhouseReservationID}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Additionalservicereservation> getAdditionalServiceReservations(@PathParam("summerhouseReservationID") Integer id) {
        TypedQuery<Additionalservicereservation> query =
                em.createNamedQuery("Additionalservicereservation.findBySummerhouseReservationID", Additionalservicereservation.class).setParameter("summerhouseReservationID", id);
        List<Additionalservicereservation> additionalservicereservations = query.getResultList();
        return additionalservicereservations;
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Additionalservicereservation entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Additionalservicereservation find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Additionalservicereservation> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Additionalservicereservation> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}