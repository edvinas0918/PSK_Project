package interceptors;

import org.json.JSONObject;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.OptimisticLockException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Dziugas on 5/28/2016.
 */
@Interceptor
@ExceptionHandler
public class ExceptionHandlerInterceptor {

    @AroundInvoke
    public Object handleOptimisticLockException(InvocationContext ctx) throws Exception {

        JSONObject responseBody = new JSONObject();
        try {
            return ctx.proceed();
        } catch(OptimisticLockException ex){
            responseBody.put("errorMessage", "Duomenys nebuvo išsaugoti serveryje. Prašome pabandyti dar kartą.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseBody.toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception ex){
            responseBody.put("errorMessage", ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseBody.toString())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

    }
}
