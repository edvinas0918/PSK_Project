/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.*;
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
    @NamedQuery(name = "HouseServicePrice.findByTax", query = "SELECT h FROM HouseServicePrice h WHERE h.tax.id = :taxID")})
public class HouseServicePrice implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected HouseServicePricePK houseServicePricePK;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private int price;
    @Basic(optional = false)
    @NotNull
    @Version
    @Column(name = "OPT_LOCK_VERSION")
    private int optLockVersion;

    public HouseServicePrice() {
    }

    public HouseServicePrice(HouseServicePricePK houseServicePricePK) {
        this.houseServicePricePK = houseServicePricePK;
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

    public int getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(int optLockVersion) {
        this.optLockVersion = optLockVersion;
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
