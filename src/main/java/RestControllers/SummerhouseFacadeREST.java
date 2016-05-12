/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestControllers;

import Entities.Summerhouse;
import Helpers.Helpers;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dziugas
 */
@Stateless
@Path("summerhouse")
public class SummerhouseFacadeREST extends AbstractFacade<Summerhouse> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    TaxFacadeREST taxFacadeREST;

    public SummerhouseFacadeREST() {
        super(Summerhouse.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Summerhouse entity) throws Exception {
        super.create(entity);
    }

    @POST
    @Path("postHashMap")
    @Consumes({MediaType.APPLICATION_JSON})
    public void handleHouse(Map<Object, Object> summerhouseMap) throws Exception {
        Summerhouse summerhouse = Helpers.getSummerhouseWithDates(summerhouseMap);
        if (summerhouse.getId() != null) {
            edit(summerhouse.getId(), summerhouse);
        } else {
            create(summerhouse);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Summerhouse entity) {
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
    public Summerhouse find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Summerhouse> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Summerhouse> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
