package Services;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Edvinas.Barickis on 5/28/2016.
 */
public class AuthenticationService {

    @javax.ws.rs.core.Context
    HttpServletRequest webRequest;


    public String getOrigin () {
        String url = webRequest.getRequestURL().toString();
        String uri = webRequest.getRequestURI();

        return url.substring(0, url.indexOf(uri) + 1);
    }
}
