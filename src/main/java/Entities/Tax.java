/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Gintautas
 */
@Entity
@Table(name = "tax")
@NamedQueries({
    @NamedQuery(name = "Tax.findAll", query = "SELECT t FROM Tax t"),
    @NamedQuery(name = "Tax.findById", query = "SELECT t FROM Tax t WHERE t.id = :id"),
    @NamedQuery(name = "Tax.findByName", query = "SELECT t FROM Tax t WHERE t.name = :name"),
    @NamedQuery(name = "Tax.findMemberTax", query = "SELECT t FROM Tax t WHERE t.name = 'Member Tax'"),
    @NamedQuery(name = "Tax.findByPrice", query = "SELECT t FROM Tax t WHERE t.price = :price")})
public class Tax implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Name")
    private String name;
    @Column(name = "Price")
    private Integer price;
    @JoinColumn(name = "TaxTypeID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Taxtype taxTypeID;
    @Basic(optional = false)
    @NotNull
    @Version
    @Column(name = "OPT_LOCK_VERSION")
    private int optLockVersion;

    public Tax() {
    }

    public Tax(Integer id) {
        this.id = id;
    }

    public Tax(Integer id, String name) {
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Taxtype getTaxTypeID() {
        return taxTypeID;
    }

    public void setTaxTypeID(Taxtype taxTypeID) {
        this.taxTypeID = taxTypeID;
    }

    public int getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(int optLockVersion) {
        this.optLockVersion = optLockVersion;
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
        if (!(object instanceof Tax)) {
            return false;
        }
        Tax other = (Tax) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Tax[ id=" + id + " ]";
    }
    
}
