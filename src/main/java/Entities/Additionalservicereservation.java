/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
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
        @NamedQuery(name = "Additionalservicereservation.findByServiceID", query = "SELECT a FROM Additionalservicereservation a WHERE a.additionalService.id = :serviceID"),
    @NamedQuery(name = "Additionalservicereservation.findByServiceStart", query = "SELECT a FROM Additionalservicereservation a WHERE a.serviceStart = :serviceStart"),
        @NamedQuery(name = "Additionalservicereservation.findBySummerhouseReservationID", query = "SELECT a FROM Additionalservicereservation a WHERE a.summerhouseReservation.id = :summerhouseReservationID")})
public class Additionalservicereservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ServiceStart")
    @Temporal(TemporalType.TIMESTAMP)
    private Date serviceStart;
    @JoinColumn(name = "SummerhouseReservationID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Summerhousereservation summerhouseReservation;

    @JoinColumn(name = "AdditionalServiceID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private AdditionalService additionalService;

    @JoinColumn(name = "payment", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Payment payment;

    public Additionalservicereservation() {
    }

    public Additionalservicereservation(Date serviceStart, Summerhousereservation summerhouseReservation, AdditionalService additionalService, Payment payment) {
        this.serviceStart = serviceStart;
        this.summerhouseReservation = summerhouseReservation;
        this.additionalService = additionalService;
        this.payment = payment;
    }

    public Summerhousereservation getSummerhouseReservation() {
        return summerhouseReservation;
    }

    public void setSummerhouseReservation(Summerhousereservation summerhouseReservationID) {
        this.summerhouseReservation = summerhouseReservationID;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public AdditionalService getAdditionalService() {
        return additionalService;
    }

    public void setAdditionalService(AdditionalService additionalService) {
        this.additionalService = additionalService;
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
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.Additionalservicereservation[ id=" + id + " ]";
    }
    
}
