package restControllers;

import services.EmailService;
import models.Mailing;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Aurimas on 2016-04-23.
 */

@Stateless
@Path("mailing")
public class MailingInvitationFacadeREST {

    @Inject
    private EmailService emailService;

     public MailingInvitationFacadeREST() {}

    @POST
    @Path("invitation")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response sendInvitationMessage(Mailing mailing) {
        try {
            emailService.sendInvitationEmail(mailing.getEmailAddresses());
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("recommendation")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response sendRecommendationMessage(Mailing mailing){
        if(emailService.isMember(mailing.getEmailAddresses())){
            try {
                emailService.sendRecommendationEmail(mailing.getEmailAddresses());
            } catch (Exception e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            return Response.status(Response.Status.OK).build();
        }else{
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    @POST
    @Path("pointsReceived")
    @Consumes({MediaType.APPLICATION_JSON})
    public void sendPointsReceivedMessage(String [] emails, Integer points, String reason) throws Exception {
        emailService.sendPointsReceivedEmail(emails, points, reason);
    }

}
