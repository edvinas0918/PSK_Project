package models;

import java.io.Serializable;

/**
 * Created by Aurimas on 2016-05-01.
 */
public class Mailing  implements Serializable {

    private String [] emailAddresses;

    public Mailing() {}

    public Mailing(String[] emailAddresses) {
       this.emailAddresses = emailAddresses;
    }

    public String[] getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(String[] emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

}
