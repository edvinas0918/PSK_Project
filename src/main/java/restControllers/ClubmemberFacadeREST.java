/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restControllers;

import entities.Clubmember;
import entities.Settings;
import entities.Summerhousereservation;
import helpers.InsufficientFundsException;
import interceptors.Authentication;
import interceptors.ExceptionHandler;
import services.ClubMemberService;
import services.EmailService;
import services.SettingsService;
import models.PointsGrant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Dziugas
 */
@Stateless
@Path("clubmember")
@ExceptionHandler
public class ClubmemberFacadeREST extends AbstractFacade<Clubmember> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private ClubMemberService clubMemberService;

    @Inject
    private SettingsService settingsService;

    @Inject
    private EmailService emailService;

    @Inject
    AuthenticationControllerREST authenticationControllerREST;

    public ClubmemberFacadeREST() {
        super(Clubmember.class);
    }

    @POST
    @Override
    @Authentication(role = {"Member", "Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Clubmember entity) throws Exception {
        Settings memberMax = settingsService.getSetting("membersMax");
        if(clubMemberService.countActiveMembers()
                >= Integer.parseInt(memberMax.getValue()))
            throw new Exception("Registracija nepavyko. Pasiektas maksimalus vartotojų skaičius. Pabandykite vėliau.");
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Clubmember entity) {
        super.edit(entity);
    }

    @PUT
    @Path("grantPoints")
    @Authentication(role = {"Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response grantPoints(PointsGrant grant) {
        Clubmember member = clubMemberService.getMember(grant.getMemberID());
        if (member == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Member not found").build();
        }

        clubMemberService.grantPoints(member, grant.getPoints());
        if (member.getId().equals(authenticationControllerREST.getSessionUser().getId())) {
            authenticationControllerREST.setSessionUser(member);
        }

        try{
            emailService.sendPointsReceivedEmail(new String[] {member.getEmail()}, grant.getPoints(), grant.getDescription());
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Email sending has failed").build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("reservation")
    @Produces({MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public List<Clubmember> getMembers(
            @QueryParam("from") String fromDateString,
            @QueryParam("until") String untilDateString) throws ParseException {
        List<Clubmember> members = super.findAll();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate;
        Date untilDate;
        try
        {
            fromDate = format.parse(fromDateString);
            untilDate = format.parse(untilDateString);
        }
        catch (Exception ex){
            return members;
        }
        List<Clubmember> membersWithReservation = new ArrayList<>();

        for (Clubmember member: members) {
            List<Summerhousereservation> reservations = member.getSummerhousereservationList();

            boolean result = false;
            for(Summerhousereservation reservation: reservations){
                if (!reservation.getFromDate().after(untilDate) && !reservation.getUntilDate().before(fromDate)) {
                    result = true;
                    break;
                }
            }
            if(result) membersWithReservation.add(member);
        }

        return membersWithReservation;
    }

    @PUT
    @Path("recommend/{candidateId}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public Response recommend(@PathParam("candidateId") Integer candidateId){
        clubMemberService.recommendCandidate(candidateId);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") int id) {
        clubMemberService.DeactivateMember(id);
    }

    @GET
    @Path("/fbUserId/{fbUserId}")
    @Produces({MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public Clubmember findByFbUserId(@PathParam("fbUserId") String fbUserId) {
        TypedQuery<Clubmember> query =
                em.createNamedQuery("Clubmember.findByfbUserId", Clubmember.class).setParameter("fbUserId", fbUserId);
        List<Clubmember> results = query.getResultList();

        if (results.isEmpty()) {
            return null;
        }

        return results.get(0);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public Clubmember find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Path("recommendations")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Clubmember> getRecommendations() {
        return em.find(Clubmember.class, clubMemberService.getCurrentUser().getId()).getRecommenders();
    }

    @GET
    @Override
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public List<Clubmember> findAll() {
        return em.createNamedQuery("Clubmember.findAll").getResultList();
    }

    @GET
    @Path("getPoints/{id}")
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public Integer getMemberPoints(@PathParam("id") Integer memberID) {
        Query query = em.createNamedQuery("Clubmember.getMemberPoints");
        query.setParameter("id", memberID);
        return (Integer)query.getResultList().get(0);
    }

    @PUT
    @Path("renewMembership")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @Authentication(role = {"Member", "Admin"})
    public Response renewMembership() {
        try{    
            clubMemberService.renewMembership(clubMemberService.getCurrentUser());
        } catch(InsufficientFundsException exc){
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        return Response.status(Response.Status.OK).build();

    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
