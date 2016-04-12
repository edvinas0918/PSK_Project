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
@Table(name = "summerhouse")
@NamedQueries({
    @NamedQuery(name = "Summerhouse.findAll", query = "SELECT s FROM Summerhouse s"),
    @NamedQuery(name = "Summerhouse.findByIDSummerhouse", query = "SELECT s FROM Summerhouse s WHERE s.iDSummerhouse = :iDSummerhouse"),
    @NamedQuery(name = "Summerhouse.findByNumber", query = "SELECT s FROM Summerhouse s WHERE s.number = :number"),
    @NamedQuery(name = "Summerhouse.findByNumberOfSeats", query = "SELECT s FROM Summerhouse s WHERE s.numberOfSeats = :numberOfSeats"),
    @NamedQuery(name = "Summerhouse.findByAvailabilityPeriod", query = "SELECT s FROM Summerhouse s WHERE s.availabilityPeriod = :availabilityPeriod"),
    @NamedQuery(name = "Summerhouse.findByDescription", query = "SELECT s FROM Summerhouse s WHERE s.description = :description")})
public class Summerhouse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_Summerhouse")
    private Integer iDSummerhouse;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Number")
    private int number;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NumberOfSeats")
    private int numberOfSeats;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 11)
    @Column(name = "AvailabilityPeriod")
    private String availabilityPeriod;
    @Size(max = 500)
    @Column(name = "Description")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDSummerhouse")
    private List<Summerhousereservation> summerhousereservationList;
    @JoinColumn(name = "ID_Tax", referencedColumnName = "ID_Tax")
    @ManyToOne(optional = false)
    private Tax iDTax;

    public Summerhouse() {
    }

    public Summerhouse(Integer iDSummerhouse) {
        this.iDSummerhouse = iDSummerhouse;
    }

    public Summerhouse(Integer iDSummerhouse, int number, int numberOfSeats, String availabilityPeriod) {
        this.iDSummerhouse = iDSummerhouse;
        this.number = number;
        this.numberOfSeats = numberOfSeats;
        this.availabilityPeriod = availabilityPeriod;
    }

    public Integer getIDSummerhouse() {
        return iDSummerhouse;
    }

    public void setIDSummerhouse(Integer iDSummerhouse) {
        this.iDSummerhouse = iDSummerhouse;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
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

    public List<Summerhousereservation> getSummerhousereservationList() {
        return summerhousereservationList;
    }

    public void setSummerhousereservationList(List<Summerhousereservation> summerhousereservationList) {
        this.summerhousereservationList = summerhousereservationList;
    }

    public Tax getIDTax() {
        return iDTax;
    }

    public void setIDTax(Tax iDTax) {
        this.iDTax = iDTax;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDSummerhouse != null ? iDSummerhouse.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Summerhouse)) {
            return false;
        }
        Summerhouse other = (Summerhouse) object;
        if ((this.iDSummerhouse == null && other.iDSummerhouse != null) || (this.iDSummerhouse != null && !this.iDSummerhouse.equals(other.iDSummerhouse))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Summerhouse[ iDSummerhouse=" + iDSummerhouse + " ]";
    }
    
}
