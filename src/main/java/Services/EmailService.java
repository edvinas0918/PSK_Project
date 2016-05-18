package Services;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Aurimas on 2016-04-19.
 */

@Stateful
public class EmailService {

    @Inject
    ClubMemberService clubMemberService;

    private String host;
    private String port;
    private String userName;
    private String password;
    private String reccomendationTemplate;

    public EmailService(){
        setEmailServerValues();
    }

    private void setEmailServerValues(){
         try{
            Context context = new InitialContext();
            host = (String) context.lookup("java:comp/env/emailHost");
            port = (String) context.lookup("java:comp/env/emailPort");
            userName = (String) context.lookup("java:comp/env/emailUsername");
            password = (String) context.lookup("java:comp/env/emailPassword");
            reccomendationTemplate = (String) context.lookup("java:comp/env/reccomendationTemplate");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

   private void sendHtmlEmail(String toAddresses[], String subject, String message)
           throws Exception {

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getDefaultInstance(properties, auth);

       for(String toAddress : toAddresses) {
           if(toAddress.isEmpty()){
               continue;
           }
           // creates a new e-mail message
           Message msg = new MimeMessage(session);

           msg.setFrom(new InternetAddress(userName));
           InternetAddress[] toAddressesSend = {new InternetAddress(toAddress)};
           msg.setRecipients(Message.RecipientType.TO, toAddressesSend);
           msg.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
           msg.setSentDate(new Date());


           // set plain text message
           msg.setContent(message, "text/html; charset=UTF-8");

           // sends the e-mail
           Transport.send(msg);
       }
    }

    private String getEmailTemplate() throws Exception {
        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();
        VelocityContext context = new VelocityContext();
        Template t = ve.getTemplate(reccomendationTemplate);

        StringWriter writer = new StringWriter();
        t.merge( context, writer );

        return writer.toString();
    }

    public void sendInvitationEmail(String [] mailTo) throws Exception {
        String subject = "Kvietimas prisijungti prie „Labanoro draugų“";

        // message contains HTML markups
        String message = "Sveiki,<br>";
        message += String.format("<i>%s</i> Jus kviečia prisijungti prie „Labanoro draugų“ klubo. Detalesnę informaciją ir kandidato anketą galite rasti <a href=\"http://localhost:8080\">mūsų puslapyje</a>.",
                clubMemberService.getCurrentUser().getFirstName() + " " + clubMemberService.getCurrentUser().getLastName());
        message += "<br><br>Pagarbiai,<br>„Labanoro draugų“ klubas";

        sendHtmlEmail(mailTo, subject, message);
    }

    public void sendPointsReceivedEmail(String [] mailTo, Integer pointsReceived, String reason) throws Exception {
        String subject = "Taškai už nuopelnus";

        if (reason == null || reason == "")
            reason = "Nenurodyta";

        // message contains HTML markups
        String message = "Sveiki,<br>";
        message += String.format(" Jums skirta <i>%d</i> taškų. <br>Priežastis: %s", pointsReceived, reason);
        message += "<br><br>Pagarbiai,<br>„Labanoro draugų“ klubas";

        sendHtmlEmail(mailTo, subject, message);
    }

    public void sendRecommendationEmail(String [] mailTo) throws Exception {
        String subject = "Naujo nario rekomendacija";

        // message contains HTML markups
        String message = "Sveiki,<br>";
        message += String.format("Naujas kandidatas <i> %s %s </i> laukia tavo patvirtinimo! Kandidato anketą galite peržiūrėti <a href=\"http://localhost:8080/#/members/%d\">mūsų puslapyje</a>.",
                clubMemberService.getCurrentUser().getFirstName(), clubMemberService.getCurrentUser().getLastName(), clubMemberService.getCurrentUser().getId());
        message += "<br><br>Pagarbiai,<br>„Labanoro draugų“ klubas";

        sendHtmlEmail(mailTo, subject, message);
    }

    public void sendCandidatePromotionEmail(String mailTo) throws Exception {
        String subject = "Anketa patvirtinta";

        String message = "Sveiki,<br>";
        message += String.format("Jūsų anketa patvirtinta! Nuo šiol prisijungę į puslapį matysite visas klubo nariams " +
                "suteikiamas paslaugas.");
        message += "<br><br>Pagarbiai,<br>„Labanoro draugų“ klubas";

        sendHtmlEmail(new String[] {mailTo}, subject, message);
    }
}
