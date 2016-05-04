package Interceptors;

import Entities.Clubmember;
import RestControllers.ClubmemberFacadeREST;
import Services.ClubMemberService;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Edvinas.Barickis on 5/2/2016.
 */

@Interceptor
@Authentication
public class AuthenticationInterceptor {

    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU", type=TRANSACTION)
    private EntityManager em;

    @Inject
    ClubmemberFacadeREST userServiceREST;

    @AroundInvoke
    public Object checkAuthentication(InvocationContext ctx) throws Exception {
       /*String token = headers.getRequestHeader("Authorization").toString();
        Clubmember user = userServiceREST.findByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Token is invalid").build();
        }*/


       // HttpHeaders headers = ctx.getParameters()[0]["headers"];//[0].headers;
       // String headerString = headers.getRequestHeader("Access").toString();
       // System.out.println(headerString);

        //TODO:Paklausk destytojo, kaip pasiekt headers
        //ctx.getParameters()[0].headers;

        return ctx.proceed();
    }
}