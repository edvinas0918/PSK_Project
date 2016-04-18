/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Dziugas
 */
@Embeddable
public class AdditionalservicereservationPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "SummerhouseReservationID")
    private int summerhouseReservationID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TaxID")
    private int taxID;

    public AdditionalservicereservationPK() {
    }

    public AdditionalservicereservationPK(int summerhouseReservationID, int taxID) {
        this.summerhouseReservationID = summerhouseReservationID;
        this.taxID = taxID;
    }

    public int getSummerhouseReservationID() {
        return summerhouseReservationID;
    }

    public void setSummerhouseReservationID(int summerhouseReservationID) {
        this.summerhouseReservationID = summerhouseReservationID;
    }

    public int getTaxID() {
        return taxID;
    }

    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) summerhouseReservationID;
        hash += (int) taxID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdditionalservicereservationPK)) {
            return false;
        }
        AdditionalservicereservationPK other = (AdditionalservicereservationPK) object;
        if (this.summerhouseReservationID != other.summerhouseReservationID) {
            return false;
        }
        if (this.taxID != other.taxID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.AdditionalservicereservationPK[ summerhouseReservationID=" + summerhouseReservationID + ", taxID=" + taxID + " ]";
    }
    
}
