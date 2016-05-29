package services;

/**
 * Created by Mindaugas on 28/05/16.
 */

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import models.PayPalPaymentDTO;
import org.json.JSONObject;
import restControllers.AuthenticationControllerREST;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class PaypalService {

    @javax.ws.rs.core.Context
    HttpServletRequest webRequest;

    @Inject
    ClubMemberService clubMemberService;

    @Inject
    AuthenticationControllerREST authenticationControllerREST;

    private static final String APPROVAL_URL_REL = "approval_url";
    private static final String APPROVED_PAYMENT_STATE = "approved";
    private static final String ERROR_MESSAGE_KEY = "errorMessage";

    private static String accessToken = null;
    private static Payment pointsPayment = null;

    private APIContext loadPaypalConfig() {
        try{
            Context context = new InitialContext();
            String clientSecret = (String) context.lookup("java:comp/env/clientSecret");
            String clientID = (String) context.lookup("java:comp/env/clientID");
            Map<String, String> map = new HashMap<>();
            map.put("mode", "sandbox");
            accessToken = new OAuthTokenCredential(clientID, clientSecret, map).getAccessToken();
            APIContext apiContext = new APIContext(accessToken);
            apiContext.setConfigurationMap(map);
            return apiContext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public Response purchaseWithPayPal(PayPalPaymentDTO payPalPaymentDTO) {
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payPalPaymentDTO.getPayerID());
        JSONObject responseBody = new JSONObject();

        try {
            Payment payment = pointsPayment.execute(getApiContext(), paymentExecute);
            payment.setId(payPalPaymentDTO.getPaymentID());
            if(payment.getState().equals(APPROVED_PAYMENT_STATE) && !payment.getTransactions().isEmpty()) {
                Double total = Double.parseDouble(payment.getTransactions().get(0).getAmount().getTotal());
                clubMemberService.grantPoints(authenticationControllerREST.getSessionUser(), total.intValue());
                responseBody.put("total", total);
                return Response.ok()
                        .entity(responseBody.toString())
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            } else {
                responseBody.put(ERROR_MESSAGE_KEY, "Apmokėjimas nebuvo patvirtintas");
            }
        } catch (PayPalRESTException e) {
            Logger.getLogger(PaypalService.class.getName()).log(Level.SEVERE, null, e);
            responseBody.put(ERROR_MESSAGE_KEY, "Sistemos klaida");
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(responseBody.toString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    public Response getPaypalAuthorizationForPayment(Integer sum, String returnURL, String cancelURL) {

        JSONObject responseBody = new JSONObject();

        String sumString = String.valueOf(sum);

        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(sumString);

        Transaction transaction = new Transaction();
        transaction.setDescription("Labanoro Draugų klubo taškų įsigijimo mokestis - " + sumString + " EUR");
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        webRequest.getRequestURL();
        redirectUrls.setCancelUrl(cancelURL);
        redirectUrls.setReturnUrl(returnURL);
        payment.setRedirectUrls(redirectUrls);

        try {
            pointsPayment = payment.create(getApiContext());
            List<Links> links = pointsPayment.getLinks().stream().filter(link -> link.getRel().equals(APPROVAL_URL_REL)).collect(Collectors.toList());
            if (!links.isEmpty()) {
                responseBody.put(APPROVAL_URL_REL, links.get(0).getHref());
                return Response.ok()
                        .entity(responseBody.toString())
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            } else {
                responseBody.put(ERROR_MESSAGE_KEY, "Negauta nukreipimo nuoroda");
            }
        } catch (PayPalRESTException e) {
            Logger.getLogger(PaypalService.class.getName()).log(Level.SEVERE, null, e);
            responseBody.put(ERROR_MESSAGE_KEY, "Sistemos klaida");

        }
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(responseBody.toString())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private APIContext getApiContext() {
        return loadPaypalConfig();
    }

}
