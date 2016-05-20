/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RestControllers;

import Entities.Clubmember;
import Entities.Settings;
import Helpers.InsufficientFundsException;
import Interceptors.Authentication;
import Services.ClubMemberService;
import Services.EmailService;
import Services.SettingsService;
import models.PointsGrant;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Dziugas
 */
@Stateless
@Path("clubmember")
public class ClubmemberFacadeREST extends AbstractFacade<Clubmember> {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    private ClubMemberService clubMemberService;

    @Inject
    private SettingsService settingsService;

    @Inject
    private EmailService emailService;

    public ClubmemberFacadeREST() {
        super(Clubmember.class);
    }

    @POST
    @Override
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
    @Consumes({MediaType.APPLICATION_JSON})
    public Response grantPoints(PointsGrant grant){
        Clubmember member = clubMemberService.getMember(grant.getMemberID());
        if (member == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Member not found").build();
        }
        clubMemberService.grantPoints(member, grant.getPoints());
        try{
            emailService.sendPointsReceivedEmail(new String[] {member.getEmail()}, grant.getPoints(), grant.getDescription());
        } catch (Exception e){
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Email sending has failed").build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    @Path("recommend/{candidateId}")
    @Consumes({MediaType.APPLICATION_JSON})
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
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Clubmember> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @PUT
    @Path("renewMembership")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response renewMembership(Clubmember member) {
        try{
            clubMemberService.renewMembership(member);
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
