package RestControllers;

import Services.EmailService;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by Aurimas on 2016-04-23.
 */

@Stateless
@Path("invitation")
public class MailingInvitationFacadeREST {

     public MailingInvitationFacadeREST() {}

    @POST
    @Path("emails")
    @Consumes({MediaType.APPLICATION_JSON})
    public void sendMessage(String[] recipients) {
        EmailService emailService = new EmailService();
        try {
            emailService.sendHtmlEmail(recipients, "Test", "Test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
