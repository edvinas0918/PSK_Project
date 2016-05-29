package restControllers;

import entities.Payment;
import interceptors.Authentication;
import interceptors.ExceptionHandler;
import models.PayPalAuthorizationDTO;
import models.PayPalPaymentDTO;
import services.IPaymentService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Gintautas on 4/28/2016.
 */

@Stateless
@Path("payments")
@ExceptionHandler
public class PaymentFacadeREST extends AbstractFacade<Payment> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private IPaymentService paymentService;

    public PaymentFacadeREST() {
        super(Payment.class);
    }

    @POST
    @Override
    @Authentication(role = {"Member", "Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Payment entity) throws Exception {
        super.create(entity);
    }

    @POST
    @Path("payPalAuthorizationForPayment")
    @Authentication(role = {"Member", "Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response getPaypalAuthorizationForPayment(PayPalAuthorizationDTO payPalAuthorizationDTO) {
        return paymentService.getPaypalAuthorizationForPayment(payPalAuthorizationDTO.getAmount(), payPalAuthorizationDTO.getReturnURL(), payPalAuthorizationDTO.getCancelURL());
    }

    @POST
    @Path("payPalMakePayment")
    @Authentication(role = {"Member", "Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response makePayPalPayment(PayPalPaymentDTO payPalPaymentDTO) {
        //return //paymentService.getPaypalAuthorizationForPayment(payPalAuthorizationDTO.getAmount(), payPalAuthorizationDTO.getReturnURL(), payPalAuthorizationDTO.getCancelURL());
        return paymentService.purchaseWithPayPal(payPalPaymentDTO);
    }

    @PUT
    @Path("{id}")
    @Authentication(role = {"Member", "Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Payment entity) {
        super.edit(entity);
    }

    @PUT
    @Path("confirm")
    @Authentication(role = {"Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public void confirmPayment(Payment[] payments){
        paymentService.confirmPayment(payments);
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
    @Produces({MediaType.APPLICATION_JSON})
    public Payment find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public List<Payment> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public List<Payment> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
