/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mindaugas
 */
@Entity
@Table(name = "additionalservicereservation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Additionalservicereservation.findAll", query = "SELECT a FROM Additionalservicereservation a"),
    @NamedQuery(name = "Additionalservicereservation.findById", query = "SELECT a FROM Additionalservicereservation a WHERE a.id = :id"),
    @NamedQuery(name = "Additionalservicereservation.findByServiceStart", query = "SELECT a FROM Additionalservicereservation a WHERE a.serviceStart = :serviceStart"),
    @NamedQuery(name = "Additionalservicereservation.findByServiceEnd", query = "SELECT a FROM Additionalservicereservation a WHERE a.serviceEnd = :serviceEnd"),
        @NamedQuery(name = "Additionalservicereservation.findBySummerhouseReservationID", query = "SELECT a FROM Additionalservicereservation a WHERE a.summerhouseReservationID = :summerhouseReservationID")})
public class Additionalservicereservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "ServiceStart")
    @Temporal(TemporalType.DATE)
    private Date serviceStart;
    @Column(name = "ServiceEnd")
    @Temporal(TemporalType.DATE)
    private Date serviceEnd;
    @JoinColumn(name = "SummerhouseReservationID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Summerhousereservation summerhouseReservationID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "AdditionalServiceID")
    private int additionalServiceID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TaxID")
    private int taxID;

    public Additionalservicereservation() {
    }

    public Summerhousereservation getSummerhouseReservationID() {
        return summerhouseReservationID;
    }

    public void setSummerhouseReservationID(Summerhousereservation summerhouseReservationID) {
        this.summerhouseReservationID = summerhouseReservationID;
    }

    public int getAdditionalServiceID() {
        return additionalServiceID;
    }

    public void setAdditionalServiceID(int additionalServiceID) {
        this.additionalServiceID = additionalServiceID;
    }

    public int getTaxID() {
        return taxID;
    }

    public void setTaxID(int taxID) {
        this.taxID = taxID;
    }

    public Additionalservicereservation(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Additionalservicereservation)) {
            return false;
        }
        Additionalservicereservation other = (Additionalservicereservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Additionalservicereservation[ id=" + id + " ]";
    }
    
}
