/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "additionalservicereservation")
@NamedQueries({
    @NamedQuery(name = "Additionalservicereservation.findAll", query = "SELECT a FROM Additionalservicereservation a"),
    @NamedQuery(name = "Additionalservicereservation.findBySummerhouseReservationID", query = "SELECT a FROM Additionalservicereservation a WHERE a.additionalservicereservationPK.summerhouseReservationID = :summerhouseReservationID"),
    @NamedQuery(name = "Additionalservicereservation.findByTaxID", query = "SELECT a FROM Additionalservicereservation a WHERE a.additionalservicereservationPK.taxID = :taxID"),
    @NamedQuery(name = "Additionalservicereservation.findByServiceStart", query = "SELECT a FROM Additionalservicereservation a WHERE a.serviceStart = :serviceStart"),
    @NamedQuery(name = "Additionalservicereservation.findByServiceEnd", query = "SELECT a FROM Additionalservicereservation a WHERE a.serviceEnd = :serviceEnd")})
public class Additionalservicereservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AdditionalservicereservationPK additionalservicereservationPK;
    @Column(name = "ServiceStart")
    @Temporal(TemporalType.DATE)
    private Date serviceStart;
    @Column(name = "ServiceEnd")
    @Temporal(TemporalType.DATE)
    private Date serviceEnd;
    @JoinColumn(name = "AdditionalServiceID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private AdditionalService additionalServiceID;

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

    public Date getServiceStart() {
        return serviceStart;
    }

    public void setServiceStart(Date serviceStart) {
        this.serviceStart = serviceStart;
    }

    public Date getServiceEnd() {
        return serviceEnd;
    }

    public void setServiceEnd(Date serviceEnd) {
        this.serviceEnd = serviceEnd;
    }

    public AdditionalService getAdditionalServiceID() {
        return additionalServiceID;
    }

    public void setAdditionalServiceID(AdditionalService additionalServiceID) {
        this.additionalServiceID = additionalServiceID;
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
