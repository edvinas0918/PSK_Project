/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "additionalservice")
@NamedQueries({
    @NamedQuery(name = "Additionalservice.findAll", query = "SELECT a FROM AdditionalService a"),
    @NamedQuery(name = "Additionalservice.findById", query = "SELECT a FROM AdditionalService a WHERE a.id = :id"),
    @NamedQuery(name = "Additionalservice.findByName", query = "SELECT a FROM AdditionalService a WHERE a.name = :name")})
@XmlRootElement
public class AdditionalService implements Serializable {

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
    @Size(min = 1, max = 500)
    @Column(name = "Name")
    private String name;
    @Lob
    @Size(max = 65535)
    @Column(name = "Description")
    private String description;
    @OneToMany(mappedBy = "additionalService")
    private List<HouseServicePrice> houseServicePrices = new ArrayList<>();
    @OneToMany(orphanRemoval = true, mappedBy = "additionalService", fetch = FetchType.EAGER)
    private List<Additionalservicereservation> additionalServiceReservationList;

    public AdditionalService() {
    }

    public AdditionalService(Integer id) {
        this.id = id;
    }

    public AdditionalService(Integer id, String name) {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof AdditionalService)) {
            return false;
        }
        AdditionalService other = (AdditionalService) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.Additionalservice[ id=" + id + " ]";
    }
    public int getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(int optLockVersion) {
        this.optLockVersion = optLockVersion;
    }
    
}
