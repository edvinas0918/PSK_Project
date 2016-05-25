/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Dziugas
 */
@Entity
@Table(name = "summerhouse")
@NamedQueries({
        @NamedQuery(name = "Summerhouse.findAll", query = "SELECT s FROM Summerhouse s"),
        @NamedQuery(name = "Summerhouse.findById", query = "SELECT s FROM Summerhouse s WHERE s.id = :id"),
        @NamedQuery(name = "Summerhouse.findByNumber", query = "SELECT s FROM Summerhouse s WHERE s.number = :number"),
        @NamedQuery(name = "Summerhouse.findByCapacity", query = "SELECT s FROM Summerhouse s WHERE s.capacity = :capacity"),
        @NamedQuery(name = "Summerhouse.findByDescription", query = "SELECT s FROM Summerhouse s WHERE s.description = :description")})
@XmlRootElement
public class Summerhouse implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Version
    @Column(name = "OPT_LOCK_VERSION")
    private int optLockVersion;
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
    @Column(name = "reservationPrice")
    private int reservationPrice;
    @Temporal(TemporalType.DATE)
    @Column(name = "BeginPeriod")
    private Date beginPeriod;
    @Temporal(TemporalType.DATE)
    @Column(name = "EndPeriod")
    private Date endPeriod;
    @Size(max = 500)
    @Column(name = "Description")
    private String description;
    @Column(columnDefinition = "LONGTEXT", name="image")
    private String image;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "summerhouse", fetch = FetchType.EAGER)
    private List<Summerhousereservation> summerhousereservationList;

    @JoinTable(name = "HouseServicePrice", joinColumns = {
            @JoinColumn(name = "houseID")}, inverseJoinColumns = {
            @JoinColumn(name = "serviceID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private List<AdditionalService> additionalServices;


    public Summerhouse() {
    }

    public Summerhouse(Integer id) {
        this.id = id;
    }

    public Summerhouse(Integer id, int number, int capacity) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<AdditionalService> getAdditionalServices() {
        return additionalServices;
    }

    public void setAdditionalServices(List<AdditionalService> additionalServices) {
        this.additionalServices = additionalServices;
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

    public void setBeginPeriod(Date beginPeriod) {
        this.beginPeriod = beginPeriod;
    }

    public void setEndPeriod(Date endPeriod) {
        this.endPeriod = endPeriod;
    }

   public Date getBeginPeriod() {
        DateFormat df = new SimpleDateFormat("yyyy MM dd");
        //return df.format(beginPeriod);
        return beginPeriod;
   }

    public Date getEndPeriod() {
        DateFormat df = new SimpleDateFormat("yyyy MM dd");
        //return df.format(endPeriod);
        return endPeriod;
    }

    public int getReservationPrice() {
        return reservationPrice;
    }

    public void setReservationPrice(int reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
