package helpers;

/**
 * Created by Aurimas on 2016-05-04.
 */

public class InsufficientFundsException extends Exception{

    public InsufficientFundsException(String message) {
        super(message);
    }
}
