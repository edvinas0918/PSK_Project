package models;

import java.io.Serializable;

/**
 * Created by Aurimas on 2016-05-01.
 */
public class Mailing  implements Serializable {

    private String currentUser;
    private String [] emailAddresses;

    public Mailing() {}

    public Mailing(String currentUser, String[] emailAddresses) {
        this.currentUser = currentUser;
        this.emailAddresses = emailAddresses;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String[] getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(String[] emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

}
