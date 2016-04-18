/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "additionalservicereservation")
@NamedQueries({
    @NamedQuery(name = "Additionalservicereservation.findAll", query = "SELECT a FROM Additionalservicereservation a"),
    @NamedQuery(name = "Additionalservicereservation.findBySummerhouseReservationID", query = "SELECT a FROM Additionalservicereservation a WHERE a.additionalservicereservationPK.summerhouseReservationID = :summerhouseReservationID"),
    @NamedQuery(name = "Additionalservicereservation.findByTaxID", query = "SELECT a FROM Additionalservicereservation a WHERE a.additionalservicereservationPK.taxID = :taxID")})
public class Additionalservicereservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AdditionalservicereservationPK additionalservicereservationPK;

    public Additionalservicereservation() {
    }

    public Additionalservicereservation(AdditionalservicereservationPK additionalservicereservationPK) {
        this.additionalservicereservationPK = additionalservicereservationPK;
    }

    public Additionalservicereservation(int summerhouseReservationID, int taxID) {
        this.additionalservicereservationPK = new AdditionalservicereservationPK(summerhouseReservationID, taxID);
    }

    public AdditionalservicereservationPK getAdditionalservicereservationPK() {
        return additionalservicereservationPK;
    }

    public void setAdditionalservicereservationPK(AdditionalservicereservationPK additionalservicereservationPK) {
        this.additionalservicereservationPK = additionalservicereservationPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (additionalservicereservationPK != null ? additionalservicereservationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Additionalservicereservation)) {
            return false;
        }
        Additionalservicereservation other = (Additionalservicereservation) object;
        if ((this.additionalservicereservationPK == null && other.additionalservicereservationPK != null) || (this.additionalservicereservationPK != null && !this.additionalservicereservationPK.equals(other.additionalservicereservationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Additionalservicereservation[ additionalservicereservationPK=" + additionalservicereservationPK + " ]";
    }
    
}
