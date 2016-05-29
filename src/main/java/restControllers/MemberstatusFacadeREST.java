/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restControllers;

import entities.Memberstatus;
import interceptors.Authentication;
import interceptors.ExceptionHandler;

import java.util.List;
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

/**
 *
 * @author Dziugas
 */
@Stateless
@Path("memberstatus")
@ExceptionHandler
public class MemberstatusFacadeREST extends AbstractFacade<Memberstatus> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public MemberstatusFacadeREST() {
        super(Memberstatus.class);
    }

    @POST
    @Override
    @Authentication(role = {"Member", "Admin"})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Memberstatus entity) throws Exception {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Authentication(role = {"Member", "Admin"})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Memberstatus entity) {
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
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Memberstatus find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memberstatus> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Memberstatus> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
