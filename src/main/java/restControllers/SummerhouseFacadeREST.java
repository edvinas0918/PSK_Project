/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restControllers;

import entities.Summerhouse;
import helpers.Helpers;
import interceptors.Authentication;
import interceptors.ExceptionHandler;
import models.SummerhouseSearchDto;
import org.json.JSONObject;
import search.summerhouse.SummerhouseSeach;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dziugas
 */
@Stateless
@Path("summerhouse")
@ExceptionHandler
public class SummerhouseFacadeREST extends AbstractFacade<Summerhouse> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    SummerhouseSeach summerhouseSeach;

    public SummerhouseFacadeREST() {
        super(Summerhouse.class);
    }

    @POST
    @Override
    @Authentication(role = {"Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Summerhouse entity) throws Exception {
        super.create(entity);
    }

    @POST
    @Path("postHashMap")
    @Authentication(role = {"Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response handleHouse(Map<Object, Object> summerhouseMap) throws Exception {
        JSONObject responseBody = new JSONObject();

        Summerhouse summerhouse = Helpers.getSummerhouseWithDates(summerhouseMap);
        if (summerhouse.getId() != null) {
            edit(summerhouse.getId(), summerhouse);
        } else {
            create(summerhouse);
        }
        responseBody.put("houseID", summerhouse.getId());
        return Response.status(Response.Status.OK)
                .entity(responseBody.toString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @POST
    @Path("search")
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public List<Summerhouse> searchSummerhouses(SummerhouseSearchDto searchDto) throws ParseException {
        List<Summerhouse> summerhouses = new ArrayList<>(em.createNamedQuery("Summerhouse.findAll").getResultList());
        return summerhouseSeach.search(summerhouses, searchDto);
    }

    @PUT
    @Path("{id}")
    @Authentication(role = {"Admin"})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Summerhouse entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    @Authentication(role = {"Admin"})
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Summerhouse find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public List<Summerhouse> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Summerhouse> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Authentication(role = {"Member", "Admin"})
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
