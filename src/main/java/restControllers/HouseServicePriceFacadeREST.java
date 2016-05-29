/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restControllers;

import entities.AdditionalService;
import entities.Additionalservicereservation;
import entities.HouseServicePrice;
import entities.HouseServicePricePK;
import interceptors.Authentication;
import interceptors.ExceptionHandler;
import models.HouseServicePriceDTO;
import models.HouseServicePricesDTO;
import services.AdditionalServiceReservationService;
import services.SummerhouseReservation;

import java.util.*;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.PathSegment;

/**
 *
 * @author Mindaugas
 */
@Stateless
@Path("houseserviceprice")
@ExceptionHandler
public class HouseServicePriceFacadeREST extends AbstractFacade<HouseServicePrice> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    SummerhouseFacadeREST summerhouseFacadeREST;

    @Inject
    AdditionalServiceFacadeREST additionalServiceFacadeREST;

    @Inject
    AdditionalServiceReservationService additionalServiceReservationService;

    @Inject
    private SummerhouseReservation summerhouseReservationService;

    public HouseServicePriceFacadeREST() {
        super(HouseServicePrice.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public void create(HouseServicePrice entity) {
        try {
            super.create(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @POST
    @Path("handleServicePrices")
    @Consumes({MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public void handleServicePrices(HouseServicePricesDTO houseServicePricesDTO) throws Exception {
        List<HouseServicePrice> houseServicePrices = getSummerhouseServicesPrices(houseServicePricesDTO.getHouseID());
        List<HouseServicePrice> editablePrices = new ArrayList<>();
        editablePrices.addAll(houseServicePrices);
        for (HouseServicePriceDTO dto : houseServicePricesDTO.getHouseServicePriceDTOList()) {
            HouseServicePrice houseServicePrice;
            if (dto.getId() > 0) {
                Query query = em.createNamedQuery("HouseServicePrice.findByIDs");
                query.setParameter("serviceID", dto.getServiceID());
                query.setParameter("houseID", houseServicePricesDTO.getHouseID());
                houseServicePrice = (HouseServicePrice) query.getResultList().get(0);
                houseServicePrice.setPrice(dto.getPrice());
                edit(houseServicePrice);
            } else {
                houseServicePrice = new HouseServicePrice(dto.getPrice(), additionalServiceFacadeREST.find(dto.getServiceID()), summerhouseFacadeREST.find(houseServicePricesDTO.getHouseID()));
                create(houseServicePrice);
            }
            if (editablePrices.contains(houseServicePrice)) {
                editablePrices.remove(houseServicePrice);
            }
        }

        for (HouseServicePrice houseServicePriceToRemove : editablePrices) {
            List<Additionalservicereservation> additionalservicereservations = additionalServiceReservationService.getAdditionalServiceReservationsForService(houseServicePriceToRemove.getAdditionalService().getId());
            if (additionalservicereservations != null && !additionalservicereservations.isEmpty()) {
                for (Additionalservicereservation additionalservicereservation : additionalservicereservations) {
                    summerhouseReservationService.cancelAdditionalServiceReservation(additionalservicereservation, additionalservicereservation.getSummerhouseReservation().getMember());
                }
            }
            em.remove(houseServicePriceToRemove);
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public void edit(HouseServicePrice entity) {
        super.edit(entity);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public HouseServicePrice find(HouseServicePricePK key) {
        return super.find(key);
    }

    @GET
    @Path("findSummerhouseServicePrices/{houseID}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public List<HouseServicePrice> getSummerhouseServicesPrices(@PathParam("houseID") final Integer houseID)
    {
        Query query = em.createNamedQuery("HouseServicePrice.findBySummerhouse");
        query.setParameter("houseID", houseID);
        return query.getResultList();
    }

    public List<AdditionalService> getSummerhouseAdditionalServices(Integer houseID) {
        List<HouseServicePrice> houseServicePrices = getSummerhouseServicesPrices(houseID);
        return houseServicePrices.stream().map(n -> n.getAdditionalService()).collect(Collectors.toList());
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public List<HouseServicePrice> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public List<HouseServicePrice> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
