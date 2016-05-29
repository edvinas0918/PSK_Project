/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restControllers;

import entities.Memberformfield;
import interceptors.Authentication;
import interceptors.ExceptionHandler;
import services.MemberFormService;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@Path("memberFormField")
@ExceptionHandler
public class MemberformfieldFacadeREST extends AbstractFacade<Memberformfield> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private MemberFormService memberFormService;

    public MemberformfieldFacadeREST() {
        super(Memberformfield.class);
    }

    @PUT
    @Path("{id}")
    @Authentication(role = {"Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Memberformfield entity) {
        super.edit(entity);
    }

    @PUT
    @Authentication(role = {"Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public void editAll(Memberformfield[] fields) {
        memberFormService.editFields(fields);
    }

    @GET
    @Path("{id}")
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public Memberformfield find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public List<Memberformfield> findAll() {
        return super.findAll();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
