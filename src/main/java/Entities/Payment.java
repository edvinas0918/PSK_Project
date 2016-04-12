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
@Table(name = "payment")
@NamedQueries({
    @NamedQuery(name = "Payment.findAll", query = "SELECT p FROM Payment p"),
    @NamedQuery(name = "Payment.findByIDPayment", query = "SELECT p FROM Payment p WHERE p.iDPayment = :iDPayment"),
    @NamedQuery(name = "Payment.findByData", query = "SELECT p FROM Payment p WHERE p.data = :data"),
    @NamedQuery(name = "Payment.findByConfirmation", query = "SELECT p FROM Payment p WHERE p.confirmation = :confirmation")})
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_Payment")
    private Integer iDPayment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Confirmation")
    private boolean confirmation;
    @JoinColumn(name = "ID_Tax", referencedColumnName = "ID_Tax")
    @ManyToOne(optional = false)
    private Tax iDTax;
    @JoinColumn(name = "ID_Member", referencedColumnName = "ID_Member")
    @ManyToOne(optional = false)
    private Member1 iDMember;

    public Payment() {
    }

    public Payment(Integer iDPayment) {
        this.iDPayment = iDPayment;
    }

    public Payment(Integer iDPayment, Date data, boolean confirmation) {
        this.iDPayment = iDPayment;
        this.data = data;
        this.confirmation = confirmation;
    }

    public Integer getIDPayment() {
        return iDPayment;
    }

    public void setIDPayment(Integer iDPayment) {
        this.iDPayment = iDPayment;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public Tax getIDTax() {
        return iDTax;
    }

    public void setIDTax(Tax iDTax) {
        this.iDTax = iDTax;
    }

    public Member1 getIDMember() {
        return iDMember;
    }

    public void setIDMember(Member1 iDMember) {
        this.iDMember = iDMember;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDPayment != null ? iDPayment.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payment)) {
            return false;
        }
        Payment other = (Payment) object;
        if ((this.iDPayment == null && other.iDPayment != null) || (this.iDPayment != null && !this.iDPayment.equals(other.iDPayment))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Payment[ iDPayment=" + iDPayment + " ]";
    }
    
}
