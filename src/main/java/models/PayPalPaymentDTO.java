package models;

/**
 * Created by Mindaugas on 28/05/16.
 */
public class PayPalPaymentDTO {

    private String paymentID;
    private String token;
    private String payerID;

    public String getPayerID() {
        return payerID;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
