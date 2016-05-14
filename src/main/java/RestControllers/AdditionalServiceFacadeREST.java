/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestControllers;

import Entities.AdditionalService;
import Entities.Summerhouse;
import Helpers.Helpers;

import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
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

import static Helpers.Helpers.getGensonInstance;

/**
 *
 * @author Mindaugas
 */
@Stateless
@Path("additionalservice")
public class AdditionalServiceFacadeREST extends AbstractFacade<AdditionalService> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public AdditionalServiceFacadeREST() {
        super(AdditionalService.class);
    }

    @POST
    @Override
    @Path("createService")
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(AdditionalService entity) throws Exception {
        super.create(entity);
    }

    @POST
    @Path("postServiceMap")
    @Consumes({MediaType.APPLICATION_JSON})
    public void handleService(Map<Object, Object> serviceMap) throws Exception {
        String serialized = getGensonInstance().serialize(serviceMap);
        AdditionalService additionalService = getGensonInstance().deserialize(serialized, AdditionalService.class);
        create(additionalService);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, AdditionalService entity) {
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
    public AdditionalService find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<AdditionalService> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<AdditionalService> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
