/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "tax")
@NamedQueries({
    @NamedQuery(name = "Tax.findAll", query = "SELECT t FROM Tax t"),
    @NamedQuery(name = "Tax.findById", query = "SELECT t FROM Tax t WHERE t.id = :id"),
    @NamedQuery(name = "Tax.findByName", query = "SELECT t FROM Tax t WHERE t.name = :name"),
    @NamedQuery(name = "Tax.findByPrice", query = "SELECT t FROM Tax t WHERE t.price = :price")})
@XmlRootElement
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
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "Price")
    private BigDecimal price;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxID", fetch = FetchType.EAGER)
    private List<Payment> paymentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "taxID", fetch = FetchType.EAGER)
    private List<Summerhouse> summerhouseList;
    @JoinColumn(name = "TaxTypeID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Taxtype taxTypeID;

    public Tax() {
    }

    public Tax(Integer id) {
        this.id = id;
    }

    public Tax(Integer id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @XmlTransient
    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    @XmlTransient
    public List<Summerhouse> getSummerhouseList() {
        return summerhouseList;
    }

    public void setSummerhouseList(List<Summerhouse> summerhouseList) {
        this.summerhouseList = summerhouseList;
    }

    public Taxtype getTaxTypeID() {
        return taxTypeID;
    }

    public void setTaxTypeID(Taxtype taxTypeID) {
        this.taxTypeID = taxTypeID;
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
