/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mindaugas
 */
@Entity
@Table(name = "HouseServicePrice")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "HouseServicePrice.findAll", query = "SELECT h FROM HouseServicePrice h"),
    @NamedQuery(name = "HouseServicePrice.findByHouseID", query = "SELECT h FROM HouseServicePrice h WHERE h.houseServicePricePK.houseID = :houseID"),
    @NamedQuery(name = "HouseServicePrice.findByServiceID", query = "SELECT h FROM HouseServicePrice h WHERE h.houseServicePricePK.serviceID = :serviceID"),
    @NamedQuery(name = "HouseServicePrice.findByPrice", query = "SELECT h FROM HouseServicePrice h WHERE h.price = :price")})
public class HouseServicePrice implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HouseServicePricePK houseServicePricePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private int price;

    public HouseServicePrice() {
    }

    public HouseServicePrice(HouseServicePricePK houseServicePricePK) {
        this.houseServicePricePK = houseServicePricePK;
    }

    public HouseServicePrice(HouseServicePricePK houseServicePricePK, int price) {
        this.houseServicePricePK = houseServicePricePK;
        this.price = price;
    }

    public HouseServicePrice(int houseID, int serviceID) {
        this.houseServicePricePK = new HouseServicePricePK(houseID, serviceID);
    }

    public HouseServicePricePK getHouseServicePricePK() {
        return houseServicePricePK;
    }

    public void setHouseServicePricePK(HouseServicePricePK houseServicePricePK) {
        this.houseServicePricePK = houseServicePricePK;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (houseServicePricePK != null ? houseServicePricePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HouseServicePrice)) {
            return false;
        }
        HouseServicePrice other = (HouseServicePrice) object;
        if ((this.houseServicePricePK == null && other.houseServicePricePK != null) || (this.houseServicePricePK != null && !this.houseServicePricePK.equals(other.houseServicePricePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.HouseServicePrice[ houseServicePricePK=" + houseServicePricePK + " ]";
    }
    
}
