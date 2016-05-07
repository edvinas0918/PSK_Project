package Interceptors;

import Entities.Clubmember;
import RestControllers.ClubmemberFacadeREST;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
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
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader == null || !authorizationHeader.startsWith("Token ")) {
                throw new NotAuthorizedException("Authorization header must be provided");
            }
            String token = authorizationHeader.substring("Token".length()).trim();

        try {
            validateToken(token);
        } catch (AccessDeniedException ex) {
            requestContext.abortWith(
                    Response.status(Response.Status.FORBIDDEN).entity("FORBIDDEN").type(MediaType.APPLICATION_JSON_TYPE).build());

        } catch (Exception ex) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

    private void validateToken(String token) throws Exception {

        Clubmember user = userServiceREST.findByToken(token);

        if (user == null) {
            throw new NotAuthorizedException("Unauthorized");
        }

        String[] allowedRoles = resourceInfo.getResourceMethod().getAnnotation(Authentication.class).role();

        if (!Arrays.asList(allowedRoles).contains(user.getMemberStatus().getName())) {
            throw new AccessDeniedException("Access denied");
        }
    }
}
