package Helpers;

import javax.ejb.ApplicationException;

/**
 * Created by Aurimas on 2016-05-04.
 */

@ApplicationException
public class MembershipException extends RuntimeException  implements IApplicationException {

    public MembershipException(String message) {
        super(message);
    }
}
