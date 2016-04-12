/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestControllers;

import Entities.Summerhouse;
import Entities.Tax;
import Entities.Taxtype;
import java.math.BigDecimal;
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
@Path("summerhouse")
public class SummerhouseFacadeREST extends AbstractFacade<Summerhouse> {

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

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Summerhouse entity) {
        super.edit(entity);
    }
    
    @POST
    @Path("dummy")
    @Consumes({MediaType.APPLICATION_JSON})
    public void createDummy(){
        Summerhouse sh = new Summerhouse();
        sh.setNumber(1);
        sh.setDescription("much description");
        sh.setAvailabilityPeriod("2015-2016");
        sh.setNumberOfSeats(10);
                
        Taxtype taxType = new Taxtype();
        taxType.setName("rezervacijos mokestis");
        
        Tax tax = new Tax();
        tax.setIDTaxType(taxType);
        tax.setName("mokestis");
        tax.setPrice(BigDecimal.TEN);

        sh.setIDTax(tax);
        super.create(sh);
    }
    
    
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
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
    @Produces({MediaType.APPLICATION_JSON})
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
