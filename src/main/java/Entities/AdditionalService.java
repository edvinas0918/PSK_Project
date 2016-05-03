/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import com.owlike.genson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mindaugas
 */
@Entity
@Table(name = "AdditionalService")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AdditionalService.findAll", query = "SELECT a FROM AdditionalService a"),
    @NamedQuery(name = "AdditionalService.findById", query = "SELECT a FROM AdditionalService a WHERE a.id = :id"),
    @NamedQuery(name = "AdditionalService.findByName", query = "SELECT a FROM AdditionalService a WHERE a.name = :name")})
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
    private int priceInPoints;

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

    public int getPriceInPoints() {
        return priceInPoints;
    }

    public void setPriceInPoints(int priceInPoints) {
        this.priceInPoints = priceInPoints;
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
        return "Entities.AdditionalService[ id=" + id + " ]";
    }
    
}
