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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "additionalservice")
@NamedQueries({
    @NamedQuery(name = "Additionalservice.findAll", query = "SELECT a FROM AdditionalService a"),
    @NamedQuery(name = "Additionalservice.findById", query = "SELECT a FROM AdditionalService a WHERE a.id = :id"),
    @NamedQuery(name = "Additionalservice.findByName", query = "SELECT a FROM AdditionalService a WHERE a.name = :name"),
    @NamedQuery(name = "Additionalservice.findByPricePoints", query = "SELECT a FROM AdditionalService a WHERE a.pricePoints = :pricePoints")})
public class AdditionalService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "PricePoints")
    private int pricePoints;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "additionalServiceID", fetch = FetchType.EAGER)
    private List<Additionalservicereservation> additionalservicereservationList;

    public AdditionalService() {
    }

    public AdditionalService(Integer id) {
        this.id = id;
    }

    public AdditionalService(Integer id, String name, int pricePoints) {
        this.id = id;
        this.name = name;
        this.pricePoints = pricePoints;
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

    public int getPricePoints() {
        return pricePoints;
    }

    public void setPricePoints(int pricePoints) {
        this.pricePoints = pricePoints;
    }

    public List<Additionalservicereservation> getAdditionalservicereservationList() {
        return additionalservicereservationList;
    }

    public void setAdditionalservicereservationList(List<Additionalservicereservation> additionalservicereservationList) {
        this.additionalservicereservationList = additionalservicereservationList;
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
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Additionalservice[ id=" + id + " ]";
    }
    
}
