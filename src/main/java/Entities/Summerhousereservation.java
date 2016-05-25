/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "summerhousereservation")
@NamedQueries({
    @NamedQuery(name = "Summerhousereservation.findAll", query = "SELECT s FROM Summerhousereservation s"),
    @NamedQuery(name = "Summerhousereservation.findById", query = "SELECT s FROM Summerhousereservation s WHERE s.id = :id"),
    @NamedQuery(name = "Summerhousereservation.findBySummerhouseId", query = "SELECT s FROM Summerhousereservation s WHERE s.summerhouse.id = :id"),
    @NamedQuery(name = "Summerhousereservation.findByClubmemberId", query = "SELECT s FROM Summerhousereservation s WHERE s.member.id = :id"),
    @NamedQuery(name = "Summerhousereservation.findByFromDate", query = "SELECT s FROM Summerhousereservation s WHERE s.fromDate = :fromDate"),
    @NamedQuery(name = "Summerhousereservation.findByUntilDate", query = "SELECT s FROM Summerhousereservation s WHERE s.untilDate = :untilDate")})
public class Summerhousereservation implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Version
    @Column(name = "OPT_LOCK_VERSION")
    private int optLockVersion;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FromDate")
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UntilDate")
    @Temporal(TemporalType.DATE)
    private Date untilDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "payment")
    private int paymentID;
    @JoinColumn(name = "MemberID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Clubmember member;
    @JoinColumn(name = "SummerhouseID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Summerhouse summerhouse;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "summerhouseReservationID", fetch = FetchType.EAGER)
    private List<Additionalservicereservation> additionalServiceReservations;

    public Summerhousereservation() {
    }

    public Summerhousereservation(Date fromDate, Date untilDate, Clubmember member, Summerhouse summerhouse) {
        this.fromDate = fromDate;
        this.untilDate = untilDate;
        this.member = member;
        this.summerhouse = summerhouse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public Clubmember getMember() {
        return member;
    }

    public void setMember(Clubmember member) {
        this.member = member;
    }

    public Summerhouse getSummerhouse() {
        return summerhouse;
    }

    public void setSummerhouse(Summerhouse summerhouse) {
        this.summerhouse = summerhouse;
    }

    public int getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(int optLockVersion) {
        this.optLockVersion = optLockVersion;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
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
        if (!(object instanceof Summerhousereservation)) {
            return false;
        }
        Summerhousereservation other = (Summerhousereservation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Summerhousereservation[ id=" + id + " ]";
    }
    
}
