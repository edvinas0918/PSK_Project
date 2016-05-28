package services;

import entities.Clubmember;
import entities.Invitation;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.Properties;

/**
 * Created by Aurimas on 2016-04-19.
 */

@Stateful
public class EmailService {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    ClubMemberService clubMemberService;

    @Inject
    AuthenticationService authService;

    private String host;
    private String port;
    private String userName;
    private String password;

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
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

   private void sendHtmlEmail(String toAddresses[], String subject, String message, boolean isLogged)
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

           if(isLogged){
               Invitation invitation = new Invitation();
               invitation.setMember(clubMemberService.getCurrentUser());
               invitation.setInvitationDate(new Date());
               invitation.setEmail(toAddress);
               em.persist(invitation);
               em.flush();
           }
       }
    }

    public void sendInvitationEmail(String [] mailTo) throws Exception {
        String subject = "Kvietimas prisijungti prie „Labanoro draugų“";

        // message contains HTML markups
        String message = "Sveiki,<br>";
        message += String.format("<i>%s</i> Jus kviečia prisijungti prie „Labanoro draugų“ klubo. Detalesnę informaciją ir kandidato anketą galite rasti <a href=\"" + authService.getOrigin() + "\">mūsų puslapyje</a>.",
                clubMemberService.getCurrentUser().getFirstName() + " " + clubMemberService.getCurrentUser().getLastName());
        message += "<br><br>Pagarbiai,<br>„Labanoro draugų“ klubas";

        sendHtmlEmail(mailTo, subject, message, false);
    }

    public void sendPointsReceivedEmail(String [] mailTo, Integer pointsReceived, String reason) throws Exception {
        String subject = "Taškai už nuopelnus";

        if (reason == null || reason == "")
            reason = "Nenurodyta";

        // message contains HTML markups
        String message = "Sveiki,<br>";
        message += String.format(" Jums skirta <i>%d</i> taškų. <br>Priežastis: %s", pointsReceived, reason);
        message += "<br><br>Pagarbiai,<br>„Labanoro draugų“ klubas";

        sendHtmlEmail(mailTo, subject, message, false);
    }

    public void sendRecommendationEmail(String [] mailTo) throws Exception {
        String subject = "Naujo nario rekomendacija";

        // message contains HTML markups
        String message = "Sveiki,<br>";
        message += String.format("Naujas kandidatas <i> %s %s </i> laukia tavo patvirtinimo! Kandidato anketą galite peržiūrėti <a href=\"" + authService.getOrigin() + "#/members/%d\">mūsų puslapyje</a>.",
                clubMemberService.getCurrentUser().getFirstName(), clubMemberService.getCurrentUser().getLastName(), clubMemberService.getCurrentUser().getId());
        message += "<br><br>Pagarbiai,<br>„Labanoro draugų“ klubas";

        sendHtmlEmail(mailTo, subject, message, true);
    }

    public void sendCandidatePromotionEmail(String mailTo) throws Exception {
        String subject = "Anketa patvirtinta";

        String message = "Sveiki,<br>";
        message += String.format("Jūsų anketa patvirtinta! Nuo šiol prisijungę į puslapį matysite visas klubo nariams " +
                "suteikiamas paslaugas.");
        message += "<br><br>Pagarbiai,<br>„Labanoro draugų“ klubas";

        sendHtmlEmail(new String[] {mailTo}, subject, message, false);
    }

    public boolean isMember(String emails []){
        for (String email: emails) {
            TypedQuery<Clubmember> query = em.createNamedQuery("Clubmember.findByEmail", Clubmember.class).setParameter("email", email);
            if (query.getResultList().isEmpty())
                return false;
            if(query.getSingleResult().getMemberStatus().getId() == 1)
                return false;
        }
        return true;
    }
}
