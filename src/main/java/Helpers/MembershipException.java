package Helpers;

import javax.ejb.ApplicationException;

/**
 * Created by Aurimas on 2016-05-04.
 */

public class MembershipException extends Exception{

    public MembershipException(String message) {
        super(message);
    }
}
