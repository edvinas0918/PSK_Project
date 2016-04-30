package RestControllers;

import Services.EmailService;

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
    public void sendInvitationMessage(String [] emails, String user) throws Exception {
        emailService.sendInvitationEmail(emails, user);
    }

    @POST
    @Path("reccomendation")
    @Consumes({MediaType.APPLICATION_JSON})
    public void sendReccomendationMessage(String [] emails, String user, String link) throws Exception {
        emailService.sendReccomendationEmail(emails, user, link);
    }

    @POST
    @Path("pointsReceived")
    @Consumes({MediaType.APPLICATION_JSON})
    public void sendPointsReceivedMessage(String [] emails, Integer points, String reason) throws Exception {
        emailService.sendPointsReceivedEmail(emails, points, reason);
    }

}
