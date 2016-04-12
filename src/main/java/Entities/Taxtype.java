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
@Table(name = "taxtype")
@NamedQueries({
    @NamedQuery(name = "Taxtype.findAll", query = "SELECT t FROM Taxtype t"),
    @NamedQuery(name = "Taxtype.findByIDTaxType", query = "SELECT t FROM Taxtype t WHERE t.iDTaxType = :iDTaxType"),
    @NamedQuery(name = "Taxtype.findByName", query = "SELECT t FROM Taxtype t WHERE t.name = :name")})
public class Taxtype implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_TaxType")
    private Integer iDTaxType;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "Name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDTaxType", fetch=FetchType.EAGER)
    private List<Tax> taxList;

    public Taxtype() {
    }

    public Taxtype(Integer iDTaxType) {
        this.iDTaxType = iDTaxType;
    }

    public Taxtype(Integer iDTaxType, String name) {
        this.iDTaxType = iDTaxType;
        this.name = name;
    }

    public Integer getIDTaxType() {
        return iDTaxType;
    }

    public void setIDTaxType(Integer iDTaxType) {
        this.iDTaxType = iDTaxType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tax> getTaxList() {
        return taxList;
    }

    public void setTaxList(List<Tax> taxList) {
        this.taxList = taxList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDTaxType != null ? iDTaxType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Taxtype)) {
            return false;
        }
        Taxtype other = (Taxtype) object;
        if ((this.iDTaxType == null && other.iDTaxType != null) || (this.iDTaxType != null && !this.iDTaxType.equals(other.iDTaxType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Taxtype[ iDTaxType=" + iDTaxType + " ]";
    }
    
}
