package RestControllers;

import Services.EmailService;
import models.Mailing;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
    @Consumes({MediaType.APPLICATION_JSON})
    public void sendInvitationMessage(Mailing mailing) throws Exception {
        emailService.sendInvitationEmail(mailing.getEmailAddresses());
    }

    @POST
    @Path("recommendation")
    @Consumes({MediaType.APPLICATION_JSON})
    public void sendRecommendationMessage(Mailing mailing) throws Exception {
        emailService.sendRecommendationEmail(mailing.getEmailAddresses());
    }

    @POST
    @Path("pointsReceived")
    @Consumes({MediaType.APPLICATION_JSON})
    public void sendPointsReceivedMessage(String [] emails, Integer points, String reason) throws Exception {
        emailService.sendPointsReceivedEmail(emails, points, reason);
    }

}
