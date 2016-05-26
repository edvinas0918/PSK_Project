/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestControllers;

import Entities.HouseServicePrice;
import Entities.HouseServicePricePK;
import models.HouseServicePriceDTO;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
public class HouseServicePriceFacadeREST extends AbstractFacade<HouseServicePrice> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    SummerhouseFacadeREST summerhouseFacadeREST;

    @Inject
    AdditionalServiceFacadeREST additionalServiceFacadeREST;

    private HouseServicePricePK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;houseID=houseIDValue;serviceID=serviceIDValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        Entities.HouseServicePricePK key = new Entities.HouseServicePricePK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> houseID = map.get("houseID");
        if (houseID != null && !houseID.isEmpty()) {
            key.setHouseID(new java.lang.Integer(houseID.get(0)));
        }
        java.util.List<String> serviceID = map.get("serviceID");
        if (serviceID != null && !serviceID.isEmpty()) {
            key.setServiceID(new java.lang.Integer(serviceID.get(0)));
        }
        return key;
    }

    public HouseServicePriceFacadeREST() {
        super(HouseServicePrice.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
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
    public void handleServicePrices(ArrayList<HouseServicePriceDTO> priceMap) throws Exception {
        for (HouseServicePriceDTO dto : priceMap) {
            if (dto.getId() > 0) {
                Query query = em.createNamedQuery("HouseServicePrice.findByIDs");
                query.setParameter("serviceID", dto.getServiceID());
                query.setParameter("houseID", dto.getHouseID());
                HouseServicePrice firstResult = (HouseServicePrice) query.getResultList().get(0);
                HouseServicePrice houseServicePrice = new HouseServicePrice(dto.getId(), firstResult.getOptLockVersion(), dto.getPrice(), additionalServiceFacadeREST.find(dto.getServiceID()), summerhouseFacadeREST.find(dto.getHouseID()));
                edit(houseServicePrice);
            } else {
                HouseServicePrice houseServicePrice = new HouseServicePrice(dto.getPrice(), additionalServiceFacadeREST.find(dto.getServiceID()), summerhouseFacadeREST.find(dto.getHouseID()));
                create(houseServicePrice);
            }
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(HouseServicePrice entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        Entities.HouseServicePricePK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public HouseServicePrice find(HouseServicePricePK key) {
        return super.find(key);
    }

    @GET
    @Path("findSummerhouseServicePrices/{houseID}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<HouseServicePrice> getSummerhouseServicesPrices(@PathParam("houseID") final Integer houseID)
    {
        List<HouseServicePrice> houseServicePrices = findAll();
        Stream<HouseServicePrice> houseServicePriceStream = findAll().stream().filter(x -> x.getSummerhouse().getId().equals(houseID));
        List<HouseServicePrice> list = houseServicePriceStream.collect(Collectors.toList());
        return list;
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<HouseServicePrice> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<HouseServicePrice> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
