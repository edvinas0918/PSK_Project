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
import javax.validation.constraints.Size;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "invitation")
@NamedQueries({
    @NamedQuery(name = "Invitation.findAll", query = "SELECT i FROM Invitation i"),
    @NamedQuery(name = "Invitation.findByIDInvitation", query = "SELECT i FROM Invitation i WHERE i.iDInvitation = :iDInvitation"),
    @NamedQuery(name = "Invitation.findByDate", query = "SELECT i FROM Invitation i WHERE i.date = :date"),
    @NamedQuery(name = "Invitation.findByEmail", query = "SELECT i FROM Invitation i WHERE i.email = :email")})
public class Invitation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_Invitation")
    private Integer iDInvitation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Email")
    private String email;
    @JoinColumn(name = "ID_Member", referencedColumnName = "ID_Member")
    @ManyToOne(optional = false)
    private Member1 iDMember;

    public Invitation() {
    }

    public Invitation(Integer iDInvitation) {
        this.iDInvitation = iDInvitation;
    }

    public Invitation(Integer iDInvitation, Date date, String email) {
        this.iDInvitation = iDInvitation;
        this.date = date;
        this.email = email;
    }

    public Integer getIDInvitation() {
        return iDInvitation;
    }

    public void setIDInvitation(Integer iDInvitation) {
        this.iDInvitation = iDInvitation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        hash += (iDInvitation != null ? iDInvitation.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Invitation)) {
            return false;
        }
        Invitation other = (Invitation) object;
        if ((this.iDInvitation == null && other.iDInvitation != null) || (this.iDInvitation != null && !this.iDInvitation.equals(other.iDInvitation))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Invitation[ iDInvitation=" + iDInvitation + " ]";
    }
    
}
