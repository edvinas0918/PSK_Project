package RestControllers;

import Entities.AdditionalService;
import Entities.Additionalservicereservation;
import Entities.Payment;
import Entities.Summerhousereservation;
import Helpers.InsufficientFundsException;
import Services.AdditionalServiceReservation;
import models.AdditionalServiceReservationDTO;

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

/**
 *
 * @author Mindaugas
 */
@Stateless
@Path("entities.additionalservicereservation")
public class AdditionalservicereservationFacadeREST extends AbstractFacade<Additionalservicereservation> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private AdditionalServiceFacadeREST additionalServiceFacadeREST;

    @Inject
    private AdditionalServiceReservation additionalServiceReservation;

    public AdditionalservicereservationFacadeREST() {
        super(Additionalservicereservation.class);
    }

    public void createServiceReservationsForSummerhouse(Summerhousereservation summerhouseReservation, List<AdditionalServiceReservationDTO> additionalServiceReservationDTOs) throws InsufficientFundsException{
        if (!additionalServiceReservationDTOs.isEmpty()){
            for (AdditionalServiceReservationDTO serviceReservationDTO : additionalServiceReservationDTOs) {
                AdditionalService additionalService = additionalServiceFacadeREST.find(serviceReservationDTO.getAdditionalServiceID());
                Payment payment = additionalServiceReservation.getPayment(additionalService, serviceReservationDTO);
                Additionalservicereservation additionalServiceReservation = new Additionalservicereservation(serviceReservationDTO.getDate(), summerhouseReservation, additionalService, payment);
                create(additionalServiceReservation);
            }
        }
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