/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Mindaugas
 */
@Embeddable
public class HouseServicePricePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "houseID")
    private int houseID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "serviceID")
    private int serviceID;

    public HouseServicePricePK() {
    }

    public HouseServicePricePK(int houseID, int serviceID) {
        this.houseID = houseID;
        this.serviceID = serviceID;
    }

    public int getHouseID() {
        return houseID;
    }

    public void setHouseID(int houseID) {
        this.houseID = houseID;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) houseID;
        hash += (int) serviceID;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HouseServicePricePK)) {
            return false;
        }
        HouseServicePricePK other = (HouseServicePricePK) object;
        if (this.houseID != other.houseID) {
            return false;
        }
        if (this.serviceID != other.serviceID) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.HouseServicePricePK[ houseID=" + houseID + ", serviceID=" + serviceID + " ]";
    }
    
}
