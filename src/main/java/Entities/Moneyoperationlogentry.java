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
import javax.validation.constraints.Size;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "moneyoperationlogentry")
@NamedQueries({
    @NamedQuery(name = "Moneyoperationlogentry.findAll", query = "SELECT m FROM Moneyoperationlogentry m"),
    @NamedQuery(name = "Moneyoperationlogentry.findById", query = "SELECT m FROM Moneyoperationlogentry m WHERE m.id = :id"),
    @NamedQuery(name = "Moneyoperationlogentry.findByMemberFirstName", query = "SELECT m FROM Moneyoperationlogentry m WHERE m.memberFirstName = :memberFirstName"),
    @NamedQuery(name = "Moneyoperationlogentry.findByMemberLastName", query = "SELECT m FROM Moneyoperationlogentry m WHERE m.memberLastName = :memberLastName"),
    @NamedQuery(name = "Moneyoperationlogentry.findByOperationTime", query = "SELECT m FROM Moneyoperationlogentry m WHERE m.operationTime = :operationTime"),
    @NamedQuery(name = "Moneyoperationlogentry.findByInvokedMethod", query = "SELECT m FROM Moneyoperationlogentry m WHERE m.invokedMethod = :invokedMethod")})
public class Moneyoperationlogentry implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 30)
    @Column(name = "memberFirstName")
    private String memberFirstName;
    @Size(max = 30)
    @Column(name = "memberLastName")
    private String memberLastName;
    @Column(name = "operationTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date operationTime;
    @Size(max = 60)
    @Column(name = "invokedMethod")
    private String invokedMethod;
    @JoinColumn(name = "member", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Clubmember member;
    @JoinColumn(name = "memberStatus", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Memberstatus memberStatus;

    public Moneyoperationlogentry() {
    }

    public Moneyoperationlogentry(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMemberFirstName() {
        return memberFirstName;
    }

    public void setMemberFirstName(String memberFirstName) {
        this.memberFirstName = memberFirstName;
    }

    public String getMemberLastName() {
        return memberLastName;
    }

    public void setMemberLastName(String memberLastName) {
        this.memberLastName = memberLastName;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getInvokedMethod() {
        return invokedMethod;
    }

    public void setInvokedMethod(String invokedMethod) {
        this.invokedMethod = invokedMethod;
    }

    public Clubmember getMember() {
        return member;
    }

    public void setMember(Clubmember member) {
        this.member = member;
    }

    public Memberstatus getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Memberstatus memberStatus) {
        this.memberStatus = memberStatus;
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
        if (!(object instanceof Moneyoperationlogentry)) {
            return false;
        }
        Moneyoperationlogentry other = (Moneyoperationlogentry) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Moneyoperationlogentry[ id=" + id + " ]";
    }
    
}
