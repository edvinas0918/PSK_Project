package models;

/**
 * Created by Mindaugas on 28/05/16.
 */
public class PayPalAuthorizationDTO {

    private Integer amount;
    private String returnURL;
    private String cancelURL;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getCancelURL() {
        return cancelURL;
    }

    public void setCancelURL(String cancelURL) {
        this.cancelURL = cancelURL;
    }
}
