/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @Column(name = "memberStatus")
    private int memberStatus;

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

    public int getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(int memberStatus) {
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
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.Moneyoperationlogentry[ id=" + id + " ]";
    }

    public int getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(int optLockVersion) {
        this.optLockVersion = optLockVersion;
    }
    
}
