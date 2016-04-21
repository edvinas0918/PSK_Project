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
@Table(name = "summerhouse")
@NamedQueries({
    @NamedQuery(name = "Summerhouse.findAll", query = "SELECT s FROM Summerhouse s"),
    @NamedQuery(name = "Summerhouse.findById", query = "SELECT s FROM Summerhouse s WHERE s.id = :id"),
    @NamedQuery(name = "Summerhouse.findByNumber", query = "SELECT s FROM Summerhouse s WHERE s.number = :number"),
    @NamedQuery(name = "Summerhouse.findByCapacity", query = "SELECT s FROM Summerhouse s WHERE s.capacity = :capacity"),
    @NamedQuery(name = "Summerhouse.findByAvailabilityPeriod", query = "SELECT s FROM Summerhouse s WHERE s.availabilityPeriod = :availabilityPeriod"),
    @NamedQuery(name = "Summerhouse.findByDescription", query = "SELECT s FROM Summerhouse s WHERE s.description = :description")})
@XmlRootElement
public class Summerhouse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Number")
    private int number;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Capacity")
    private int capacity;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "AvailabilityPeriod")
    private String availabilityPeriod;
    @Size(max = 500)
    @Column(name = "Description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "summerhouseID", fetch = FetchType.EAGER)
    private List<Summerhousereservation> summerhousereservationList;
    @JoinColumn(name = "TaxID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Tax taxID;

    public Summerhouse() {
    }

    public Summerhouse(Integer id) {
        this.id = id;
    }

    public Summerhouse(Integer id, int number, int capacity, String availabilityPeriod) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.availabilityPeriod = availabilityPeriod;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAvailabilityPeriod() {
        return availabilityPeriod;
    }

    public void setAvailabilityPeriod(String availabilityPeriod) {
        this.availabilityPeriod = availabilityPeriod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public List<Summerhousereservation> getSummerhousereservationList() {
        return summerhousereservationList;
    }

    public void setSummerhousereservationList(List<Summerhousereservation> summerhousereservationList) {
        this.summerhousereservationList = summerhousereservationList;
    }

    public Tax getTaxID() {
        return taxID;
    }

    public void setTaxID(Tax taxID) {
        this.taxID = taxID;
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
        if (!(object instanceof Summerhouse)) {
            return false;
        }
        Summerhouse other = (Summerhouse) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Summerhouse[ id=" + id + " ]";
    }
    
}
