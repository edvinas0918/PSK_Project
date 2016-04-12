/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "summerhousereservation")
@NamedQueries({
    @NamedQuery(name = "Summerhousereservation.findAll", query = "SELECT s FROM Summerhousereservation s"),
    @NamedQuery(name = "Summerhousereservation.findByIDSummerhouseReservation", query = "SELECT s FROM Summerhousereservation s WHERE s.iDSummerhouseReservation = :iDSummerhouseReservation"),
    @NamedQuery(name = "Summerhousereservation.findByFromDate", query = "SELECT s FROM Summerhousereservation s WHERE s.fromDate = :fromDate"),
    @NamedQuery(name = "Summerhousereservation.findByUntilDate", query = "SELECT s FROM Summerhousereservation s WHERE s.untilDate = :untilDate")})
public class Summerhousereservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_SummerhouseReservation")
    private Integer iDSummerhouseReservation;
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
    @JoinColumn(name = "ID_Member", referencedColumnName = "ID_Member")
    @ManyToOne(optional = false)
    private Member1 iDMember;
    @JoinColumn(name = "ID_Summerhouse", referencedColumnName = "ID_Summerhouse")
    @ManyToOne(optional = false)
    private Summerhouse iDSummerhouse;

    public Summerhousereservation() {
    }

    public Summerhousereservation(Integer iDSummerhouseReservation) {
        this.iDSummerhouseReservation = iDSummerhouseReservation;
    }

    public Summerhousereservation(Integer iDSummerhouseReservation, Date fromDate, Date untilDate) {
        this.iDSummerhouseReservation = iDSummerhouseReservation;
        this.fromDate = fromDate;
        this.untilDate = untilDate;
    }

    public Integer getIDSummerhouseReservation() {
        return iDSummerhouseReservation;
    }

    public void setIDSummerhouseReservation(Integer iDSummerhouseReservation) {
        this.iDSummerhouseReservation = iDSummerhouseReservation;
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

    public Member1 getIDMember() {
        return iDMember;
    }

    public void setIDMember(Member1 iDMember) {
        this.iDMember = iDMember;
    }

    public Summerhouse getIDSummerhouse() {
        return iDSummerhouse;
    }

    public void setIDSummerhouse(Summerhouse iDSummerhouse) {
        this.iDSummerhouse = iDSummerhouse;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDSummerhouseReservation != null ? iDSummerhouseReservation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Summerhousereservation)) {
            return false;
        }
        Summerhousereservation other = (Summerhousereservation) object;
        if ((this.iDSummerhouseReservation == null && other.iDSummerhouseReservation != null) || (this.iDSummerhouseReservation != null && !this.iDSummerhouseReservation.equals(other.iDSummerhouseReservation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Summerhousereservation[ iDSummerhouseReservation=" + iDSummerhouseReservation + " ]";
    }
    
}
