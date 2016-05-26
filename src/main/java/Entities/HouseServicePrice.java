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
 * @author Mindaugas
 */
@Entity
@Table(name = "HouseServicePrice")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "HouseServicePrice.findAll", query = "SELECT h FROM HouseServicePrice h"),
        @NamedQuery(name = "HouseServicePrice.findByIDs", query = "SELECT h FROM HouseServicePrice h WHERE h.additionalService.id = :serviceID AND h.summerhouse.id = :houseID")})
public class HouseServicePrice implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private int price;
    @Basic(optional = false)
    @NotNull
    @Version
    @Column(name = "OPT_LOCK_VERSION")
    private int optLockVersion;
    @ManyToOne
    @JoinColumn(name = "serviceID")
    private AdditionalService additionalService;

    @ManyToOne
    @JoinColumn(name = "houseID")
    private Summerhouse summerhouse;

    public HouseServicePrice() {
    }

    public HouseServicePrice(int price, AdditionalService additionalService, Summerhouse summerhouse) {
        this.price = price;
        this.additionalService = additionalService;
        this.summerhouse = summerhouse;
    }

    public HouseServicePrice(int id, int price, int optLockVersion, AdditionalService additionalService, Summerhouse summerhouse) {
        this.price = price;
        this.optLockVersion = optLockVersion;
        this.id = id;
        this.additionalService = additionalService;
        this.summerhouse = summerhouse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(int optLockVersion) {
        this.optLockVersion = optLockVersion;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public AdditionalService getAdditionalService() {
        return additionalService;
    }

    public void setAdditionalService(AdditionalService additionalService) {
        this.additionalService = additionalService;
    }

    public Summerhouse getSummerhouse() {
        return summerhouse;
    }

    public void setSummerhouse(Summerhouse summerhouse) {
        this.summerhouse = summerhouse;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HouseServicePrice)) {
            return false;
        }
        HouseServicePrice other = (HouseServicePrice) object;
        if (!this.summerhouse.getId().equals(other.summerhouse.getId()) || !this.additionalService.getId().equals(other.additionalService.getId())) {
            return false;
        }
        return true;
    }

}
