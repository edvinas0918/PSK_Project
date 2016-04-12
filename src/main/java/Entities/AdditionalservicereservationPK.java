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
    @Column(name = "ID_SummerhouseReservation")
    private int iDSummerhouseReservation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_Tax")
    private int iDTax;

    public AdditionalservicereservationPK() {
    }

    public AdditionalservicereservationPK(int iDSummerhouseReservation, int iDTax) {
        this.iDSummerhouseReservation = iDSummerhouseReservation;
        this.iDTax = iDTax;
    }

    public int getIDSummerhouseReservation() {
        return iDSummerhouseReservation;
    }

    public void setIDSummerhouseReservation(int iDSummerhouseReservation) {
        this.iDSummerhouseReservation = iDSummerhouseReservation;
    }

    public int getIDTax() {
        return iDTax;
    }

    public void setIDTax(int iDTax) {
        this.iDTax = iDTax;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) iDSummerhouseReservation;
        hash += (int) iDTax;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AdditionalservicereservationPK)) {
            return false;
        }
        AdditionalservicereservationPK other = (AdditionalservicereservationPK) object;
        if (this.iDSummerhouseReservation != other.iDSummerhouseReservation) {
            return false;
        }
        if (this.iDTax != other.iDTax) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.AdditionalservicereservationPK[ iDSummerhouseReservation=" + iDSummerhouseReservation + ", iDTax=" + iDTax + " ]";
    }
    
}
