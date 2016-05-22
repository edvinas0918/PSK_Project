package RestControllers;

import Entities.Clubmember;
import Entities.Memberstatus;
import Services.ClubMemberService;
import models.AuthResponse;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Edvinas.Barickis on 4/20/2016.
 */

@Stateless
@Path("authentication")
public class AuthenticationControllerREST {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject
    ClubmemberFacadeREST userServiceREST;

    @Context
    HttpServletRequest webRequest;

    @Inject
    ClubMemberService userService;

    @GET
    @Path("getUserAccessToken")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthResponse getUserAccessToken(@QueryParam("code") String code,
                                           @QueryParam("redirectUrl") String redirectUrl) {

        FBConnection fbConnection = new FBConnection();
        if (code == null || code.equals("")) {
            return new AuthResponse(401, fbConnection.getFBAuthUrl(redirectUrl));
        }

        String accessToken;

        try {
            accessToken = fbConnection.getAccessToken(code);
        } catch (RuntimeException ex) {
            return new AuthResponse(401, ex.getMessage());
        }

        JSONObject userInfo = new FBGraph(accessToken).getFBGraph();

        Clubmember user = userServiceREST.findByFbUserId(userInfo.getString("id"));

        if(user != null && user.getIsActive() == false){
            return new AuthResponse(400, "Vartotojas yra deaktyvuotas. Prisijungimas negalimas.");
        }

        HttpSession session = webRequest.getSession();

        if (user == null){
            user = new Clubmember();
            String[] name = userInfo.getString("name").split(" ");
            user.setFirstName(name[0]);
            user.setLastName(name[1]);
            user.setMemberStatus(userService.getMemberStatusByName("Candidate"));
            user.setFbUserId(userInfo.getString("id"));
            user.setEmail(userInfo.getString("email"));
            user.setReservationGroup(1);
            user.setPoints(0);
            user.setIsActive(true);
            try {
                userServiceREST.create(user);
            } catch (Exception e) {
                return new AuthResponse(400, e.getMessage());
            }
        }
        session.setAttribute("User", user);

        return new AuthResponse(200, accessToken);
    }

    @GET
    @Path("getUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Clubmember getSessionUser() {
        return (Clubmember)webRequest.getSession().getAttribute("User");
    }

    @GET
    @Path("logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response LogOut() {
        webRequest.getSession().removeAttribute("User");

        return Response.ok().build();
    }

    private class FBConnection {
        public static final String FB_APP_ID = "1597719040467242";
        public static final String FB_APP_SECRET = "57e5ede13f7ac8be87628d2c2d29d9aa";
        public static final String REDIRECT_URI = "http://localhost:8080/";

        String accessToken = "";

        public String getFBAuthUrl(String redirectUrl) {
            String fbLoginUrl = "";
            try {
                fbLoginUrl = "http://www.facebook.com/dialog/oauth?" + "client_id="
                        + FBConnection.FB_APP_ID + "&redirect_uri="
                        + URLEncoder.encode(redirectUrl, "UTF-8")
                        + "&scope=email";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return fbLoginUrl;
        }

        public String getFBGraphUrl(String code) {
            String fbGraphUrl = "";
            try {
                fbGraphUrl = "https://graph.facebook.com/oauth/access_token?"
                        + "client_id=" + FBConnection.FB_APP_ID + "&redirect_uri="
                        + URLEncoder.encode(FBConnection.REDIRECT_URI, "UTF-8")
                        + "&client_secret=" + FB_APP_SECRET + "&code=" + code;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return fbGraphUrl;
        }

        public String getAccessToken(String code) {
            if ("".equals(accessToken)) {
                URL fbGraphURL;
                try {
                    fbGraphURL = new URL(getFBGraphUrl(code));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Invalid code received " + e);
                }
                URLConnection fbConnection;
                StringBuffer b = null;
                try {
                    fbConnection = fbGraphURL.openConnection();
                    BufferedReader in;
                    in = new BufferedReader(new InputStreamReader(
                            fbConnection.getInputStream()));
                    String inputLine;
                    b = new StringBuffer();
                    while ((inputLine = in.readLine()) != null)
                        b.append(inputLine + "\n");
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Unable to connect with Facebook "
                            + e);
                }

                accessToken = b.toString();
                if (accessToken.startsWith("{")) {
                    throw new RuntimeException("ERROR: Access Token Invalid: "
                            + accessToken);
                }
            }

            String pattern = "^access_token=([^&]+).*";
            Pattern r = Pattern.compile(pattern);

            Matcher m = r.matcher(accessToken);

            if (m.find()) {
                return m.group(1);
            }

            return "";
        }
    }

    private class FBGraph {
        private String accessToken;

        public FBGraph(String accessToken) {
            this.accessToken = accessToken;
        }

        public JSONObject getFBGraph() {
            String graph = null;
            try {

                String g = "https://graph.facebook.com/me?access_token=" + accessToken + "&fields=name,email";
                URL u = new URL(g);
                URLConnection c = u.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        c.getInputStream()));
                String inputLine;
                StringBuffer b = new StringBuffer();
                while ((inputLine = in.readLine()) != null)
                    b.append(inputLine + "\n");
                in.close();
                graph = b.toString();
                System.out.println(graph);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("ERROR in getting FB graph data. " + e);
            }

            return new JSONObject(graph);
        }
    }
}
