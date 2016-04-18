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
import javax.persistence.FetchType;
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
    @NamedQuery(name = "Summerhousereservation.findById", query = "SELECT s FROM Summerhousereservation s WHERE s.id = :id"),
    @NamedQuery(name = "Summerhousereservation.findByFromDate", query = "SELECT s FROM Summerhousereservation s WHERE s.fromDate = :fromDate"),
    @NamedQuery(name = "Summerhousereservation.findByUntilDate", query = "SELECT s FROM Summerhousereservation s WHERE s.untilDate = :untilDate")})
public class Summerhousereservation implements Serializable {

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
    @JoinColumn(name = "MemberID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Clubmember memberID;
    @JoinColumn(name = "SummerhouseID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Summerhouse summerhouseID;

    public Summerhousereservation() {
    }

    public Summerhousereservation(Integer id) {
        this.id = id;
    }

    public Summerhousereservation(Integer id, Date fromDate, Date untilDate) {
        this.id = id;
        this.fromDate = fromDate;
        this.untilDate = untilDate;
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

    public Clubmember getMemberID() {
        return memberID;
    }

    public void setMemberID(Clubmember memberID) {
        this.memberID = memberID;
    }

    public Summerhouse getSummerhouseID() {
        return summerhouseID;
    }

    public void setSummerhouseID(Summerhouse summerhouseID) {
        this.summerhouseID = summerhouseID;
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
