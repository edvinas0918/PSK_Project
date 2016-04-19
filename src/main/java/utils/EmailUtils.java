package utils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Aurimas on 2016-04-19.
 */

public class EmailUtils {

    public static final String host = ""; //"smtp.gmail.com";
    public static final String port = ""; //"587";
    public static final String userName = "";
    public static final String password = "";

   public static void sendHtmlEmail(String toAddresses[], String subject, String message)
           throws AddressException, MessagingException {

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
            EmailUtils.sendHtmlEmail(mailTo, subject, message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
