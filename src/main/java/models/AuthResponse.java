package models;

/**
 * Created by Edvinas.Barickis on 4/23/2016.
 */

public class AuthResponse {
    public int statusCode;
    public String message;
    //public Date ValidTo;

    public AuthResponse(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        // ValidTo = validTo;
    }
}
