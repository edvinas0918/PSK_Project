package Interceptors;

import Entities.Clubmember;
import RestControllers.ClubmemberFacadeREST;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Dziugas on 5/4/2016.
 */

@Provider
@Priority(Priorities.AUTHENTICATION)
@Authentication(role = {"Candidate", "Member", "Admin"})
public class AuthenticationFilter implements ContainerRequestFilter {


    @Inject
    ClubmemberFacadeREST userServiceREST;

    @Context
    HttpServletRequest webRequest;

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        HttpSession session = webRequest.getSession();

        Clubmember user = (Clubmember) session.getAttribute("User");

        if (user == null) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        } else {
            String[] allowedRoles = resourceInfo.getResourceMethod().getAnnotation(Authentication.class).role();

            if (!Arrays.asList(allowedRoles).contains(user.getMemberStatus().getName())) {
                requestContext.abortWith(
                        Response.status(Response.Status.FORBIDDEN).build());
            }
        }
    }
}
