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

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "tax")
@NamedQueries({
    @NamedQuery(name = "Tax.findAll", query = "SELECT t FROM Tax t"),
    @NamedQuery(name = "Tax.findByIDTax", query = "SELECT t FROM Tax t WHERE t.iDTax = :iDTax"),
    @NamedQuery(name = "Tax.findByName", query = "SELECT t FROM Tax t WHERE t.name = :name"),
    @NamedQuery(name = "Tax.findByPrice", query = "SELECT t FROM Tax t WHERE t.price = :price")})
public class Tax implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_Tax")
    private Integer iDTax;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDTax")
    private List<Payment> paymentList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDTax")
    private List<Summerhouse> summerhouseList;
    @JoinColumn(name = "ID_TaxType", referencedColumnName = "ID_TaxType")
    @ManyToOne(optional = false)
    private Taxtype iDTaxType;

    public Tax() {
    }

    public Tax(Integer iDTax) {
        this.iDTax = iDTax;
    }

    public Tax(Integer iDTax, String name, BigDecimal price) {
        this.iDTax = iDTax;
        this.name = name;
        this.price = price;
    }

    public Integer getIDTax() {
        return iDTax;
    }

    public void setIDTax(Integer iDTax) {
        this.iDTax = iDTax;
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

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public List<Summerhouse> getSummerhouseList() {
        return summerhouseList;
    }

    public void setSummerhouseList(List<Summerhouse> summerhouseList) {
        this.summerhouseList = summerhouseList;
    }

    public Taxtype getIDTaxType() {
        return iDTaxType;
    }

    public void setIDTaxType(Taxtype iDTaxType) {
        this.iDTaxType = iDTaxType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDTax != null ? iDTax.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tax)) {
            return false;
        }
        Tax other = (Tax) object;
        if ((this.iDTax == null && other.iDTax != null) || (this.iDTax != null && !this.iDTax.equals(other.iDTax))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Tax[ iDTax=" + iDTax + " ]";
    }
    
}
