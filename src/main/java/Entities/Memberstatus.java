/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "memberstatus")
@NamedQueries({
    @NamedQuery(name = "Memberstatus.findAll", query = "SELECT m FROM Memberstatus m"),
    @NamedQuery(name = "Memberstatus.findById", query = "SELECT m FROM Memberstatus m WHERE m.id = :id"),
    @NamedQuery(name = "Memberstatus.findByName", query = "SELECT m FROM Memberstatus m WHERE m.name = :name")})
@XmlRootElement
public class Memberstatus implements Serializable {

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
    @Size(min = 1, max = 40)
    @Column(name = "Name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberStatus", fetch = FetchType.EAGER)
    private List<Clubmember> clubmemberList;

    public Memberstatus() {

    }

    public Memberstatus(Integer id) {
        this.id = id;
    }

    public Memberstatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<Clubmember> getClubmemberList() {
        return clubmemberList;
    }

    public void setClubmemberList(List<Clubmember> clubmemberList) {
        this.clubmemberList = clubmemberList;
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
        if (!(object instanceof Memberstatus)) {
            return false;
        }
        Memberstatus other = (Memberstatus) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Memberstatus[ id=" + id + " ]";
    }

    public int getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(int optLockVersion) {
        this.optLockVersion = optLockVersion;
    }

}
