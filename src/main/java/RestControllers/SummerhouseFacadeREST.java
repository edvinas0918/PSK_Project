/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestControllers;

import Entities.Summerhouse;

import java.util.Date;
import java.util.List;
import java.util.Map;

import Helpers.Helpers;
import com.owlike.genson.*;
import org.joda.time.DateTime;

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
 * @author Dziugas
 */
@Stateless
@Path("summerhouse")
public class SummerhouseFacadeREST extends AbstractFacade<Summerhouse> {

    @Inject
    TaxFacadeREST taxFacadeREST;

    private Genson genson = Helpers.getGensonInstance();

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public SummerhouseFacadeREST() {
        super(Summerhouse.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Summerhouse entity) {
        super.create(entity);
    }

    @POST
    @Path("postHashMap")
    @Consumes({MediaType.APPLICATION_JSON})
    public void getHouse(Map<Object, Object> summerhouseMap) {
        Date endPeriod=null, beginPeriod=null;
        if(summerhouseMap.containsKey("endPeriod")) {
            summerhouseMap.get("endPeriod");
            DateTime dateTime = DateTime.parse((String) summerhouseMap.get("endPeriod"));
            endPeriod = dateTime.toDate();
            summerhouseMap.remove("endPeriod");
        }
        if(summerhouseMap.containsKey("beginPeriod")) {
            summerhouseMap.get("beginPeriod");
            DateTime dateTime = DateTime.parse((String) summerhouseMap.get("beginPeriod"));
            beginPeriod = dateTime.toDate();
            summerhouseMap.remove("beginPeriod");
        }
        String serialized = genson.serialize(summerhouseMap);
        Summerhouse summerhouse = genson.deserialize(serialized, Summerhouse.class);
        summerhouse.setEndPeriod(endPeriod);
        summerhouse.setBeginPeriod(beginPeriod);
        create(summerhouse);
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
        Summerhouse summerhouse = super.find(id);
        return summerhouse;
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
