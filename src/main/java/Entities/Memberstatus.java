/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "memberstatus")
@NamedQueries({
    @NamedQuery(name = "Memberstatus.findAll", query = "SELECT m FROM Memberstatus m"),
    @NamedQuery(name = "Memberstatus.findByIDMemberStatus", query = "SELECT m FROM Memberstatus m WHERE m.iDMemberStatus = :iDMemberStatus"),
    @NamedQuery(name = "Memberstatus.findByName", query = "SELECT m FROM Memberstatus m WHERE m.name = :name")})
public class Memberstatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_MemberStatus")
    private Integer iDMemberStatus;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "Name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDMemberStatus")
    private List<Member1> member1List;

    public Memberstatus() {
    }

    public Memberstatus(Integer iDMemberStatus) {
        this.iDMemberStatus = iDMemberStatus;
    }

    public Memberstatus(Integer iDMemberStatus, String name) {
        this.iDMemberStatus = iDMemberStatus;
        this.name = name;
    }

    public Integer getIDMemberStatus() {
        return iDMemberStatus;
    }

    public void setIDMemberStatus(Integer iDMemberStatus) {
        this.iDMemberStatus = iDMemberStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member1> getMember1List() {
        return member1List;
    }

    public void setMember1List(List<Member1> member1List) {
        this.member1List = member1List;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDMemberStatus != null ? iDMemberStatus.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Memberstatus)) {
            return false;
        }
        Memberstatus other = (Memberstatus) object;
        if ((this.iDMemberStatus == null && other.iDMemberStatus != null) || (this.iDMemberStatus != null && !this.iDMemberStatus.equals(other.iDMemberStatus))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Memberstatus[ iDMemberStatus=" + iDMemberStatus + " ]";
    }
    
}
