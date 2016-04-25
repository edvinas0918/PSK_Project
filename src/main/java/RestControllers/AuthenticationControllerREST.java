package RestControllers;

import models.AuthResponse;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Edvinas.Barickis on 4/20/2016.
 */

@Stateless
@Path("authentication")
public class AuthenticationControllerREST {

   /* @GET
    @Path("logout")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthResponse logout () {
        this.session.invalidate ();
    }*/


    @GET
    @Path("getUserInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthResponse getUserInfo(@QueryParam("access_token") String accessToken){

        FBGraph fbGraph = new FBGraph(accessToken);
        String userInfo;

        userInfo = fbGraph.getFBGraph();

        return new AuthResponse(200, userInfo);
    }


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

        return new AuthResponse(200, accessToken);
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
            return accessToken;
        }
    }

    public class FBGraph {
        private String accessToken;

        public FBGraph(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getFBGraph() {
            String graph = null;
            try {

                String g = "https://graph.facebook.com/me?access_token=" + accessToken;
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
            return graph;
        }

        public Map getGraphData(String fbGraph) {
            Map fbProfile = new HashMap();
            try {
                JSONObject json = new JSONObject(fbGraph);
                fbProfile.put("id", json.getString("id"));
                fbProfile.put("first_name", json.getString("first_name"));
                if (json.has("email"))
                    fbProfile.put("email", json.getString("email"));
                if (json.has("gender"))
                    fbProfile.put("gender", json.getString("gender"));
            } catch (JSONException e) {
                e.printStackTrace();
                throw new RuntimeException("ERROR in parsing FB graph data. " + e);
            }
            return fbProfile;
        }
    }
}
