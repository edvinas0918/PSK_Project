package Services;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Aurimas on 2016-04-19.
 */

public class EmailService {

    private String host;
    private String port;
    private String userName;
    private String password;
    private String reccomendationTemplate;
    private String invitationTemplate;

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
            invitationTemplate = (String) context.lookup("java:comp/env/invitationTemplate");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

   public void sendHtmlEmail(String toAddresses[], String subject, String message)
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
           // creates a new e-mail message
           Message msg = new MimeMessage(session);

           msg.setFrom(new InternetAddress(userName));
           InternetAddress[] toAddressesSend = {new InternetAddress(toAddress)};
           msg.setRecipients(Message.RecipientType.TO, toAddressesSend);
           msg.setSubject(subject);
           msg.setSentDate(new Date());

           // set plain text message
           msg.setContent(message, "text/html");

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

    public void sendTest() {

        // outgoing message information
        String mailTo [] = {"tavo.pastas@gmail.com", "tavo.pastas@gmail.com"};
        String subject = "Naujo nario rekomendacija";
        String newMember = "Labanoras Draugas";

        // message contains HTML markups
        String message = "Sveikas,";
        message += "<br/><br/>";
        message += String.format("Naujas kandidatas <i> %s </i> laukia tavo patvirtinimo!", newMember);
        message += "<br/><br/>";
        message += "<a href = \"http://localhost:8080/#/friends\"><button>Patvirtinti</button></a>";;

        try {
            EmailService emailService = new EmailService();
            emailService.sendHtmlEmail(mailTo, subject, message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
