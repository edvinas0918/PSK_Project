package restControllers;

import entities.AdditionalService;
import entities.Additionalservicereservation;
import entities.Payment;
import entities.Summerhousereservation;
import helpers.InsufficientFundsException;
import interceptors.Authentication;
import interceptors.ExceptionHandler;
import services.AdditionalServiceReservationService;
import models.AdditionalServiceReservationDTO;
import models.HandlesServiceDTO;
import org.json.JSONObject;
import services.SummerhouseReservation;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
@ExceptionHandler
public class AdditionalservicereservationFacadeREST extends AbstractFacade<Additionalservicereservation> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private AdditionalServiceFacadeREST additionalServiceFacadeREST;

    @Inject
    private AdditionalServiceReservationService additionalServiceReservationService;

    @Inject
    private SummerhouseReservationREST summerhouseReservationREST;

    @Inject
    private SummerhouseReservation summerhouseReservationService;

    public AdditionalservicereservationFacadeREST() {
        super(Additionalservicereservation.class);
    }

    public void createServiceReservationsForSummerhouse(Summerhousereservation summerhouseReservation, List<AdditionalServiceReservationDTO> additionalServiceReservationDTOs)
                                                        throws InsufficientFundsException{
        if (!additionalServiceReservationDTOs.isEmpty()){
            for (AdditionalServiceReservationDTO serviceReservationDTO : additionalServiceReservationDTOs) {
                create(getServiceReservationForDTO(serviceReservationDTO, summerhouseReservation));
            }
        }
    }

    private Additionalservicereservation getServiceReservationForDTO(AdditionalServiceReservationDTO serviceReservationDTO, Summerhousereservation summerhouseReservation) throws InsufficientFundsException {
        AdditionalService additionalService = additionalServiceFacadeREST.find(serviceReservationDTO.getAdditionalServiceID());
        Payment payment = additionalServiceReservationService.getPayment(additionalService, serviceReservationDTO);
        return new Additionalservicereservation(serviceReservationDTO.getDate(), summerhouseReservation, additionalService, payment);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Authentication(role = {"Admin"})
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
    @Authentication(role = {"Member", "Admin"})
    public Response updateReservationServices(HandlesServiceDTO handlesServiceDTO) {
        JSONObject responseBody = new JSONObject();
        List<AdditionalServiceReservationDTO> additionalServiceReservationDTOs = handlesServiceDTO.getAdditionalServiceReservationDTOs();
        List<Additionalservicereservation> currentReservations = getAdditionalServiceReservations(handlesServiceDTO.getReservationID());
        List<Additionalservicereservation> editableReservations = new ArrayList<>();
        editableReservations.addAll(currentReservations);
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
                if (editableReservations.contains(additionalservicereservation)) {
                    editableReservations.remove(additionalservicereservation);
                }
            }

        } catch (Exception ex) {
            responseBody.put("errorMessage", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseBody.toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }


        for (Additionalservicereservation reservationToRemove : editableReservations) {
            summerhouseReservationService.cancelAdditionalServiceReservation(reservationToRemove, summerhouseReservation.getMember());
        }


        return Response.ok().build();
    }

    @GET
    @Path("reservedServicesFor/{summerhouseReservationID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authentication(role = {"Member", "Admin"})
    public List<Additionalservicereservation> getAdditionalServiceReservations(@PathParam("summerhouseReservationID") Integer id) {
        return additionalServiceReservationService.getAdditionalServiceReservations(id);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public void edit(@PathParam("id") Integer id, Additionalservicereservation entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    @Authentication(role = {"Member", "Admin"})
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public Additionalservicereservation find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public List<Additionalservicereservation> findAll() {
        return super.findAll();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}