/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Dziugas
 */
@Entity
@Table(name = "summerhousereservation")
@NamedQueries({
    @NamedQuery(name = "Summerhousereservation.findAll", query = "SELECT s FROM Summerhousereservation s"),
    @NamedQuery(name = "Summerhousereservation.findById", query = "SELECT s FROM Summerhousereservation s WHERE s.id = :id"),
    @NamedQuery(name = "Summerhousereservation.findBySummerhouseId", query = "SELECT s FROM Summerhousereservation s WHERE s.summerhouse.id = :id"),
    @NamedQuery(name = "Summerhousereservation.findByClubmemberId", query = "SELECT s FROM Summerhousereservation s WHERE s.member.id = :id"),
    @NamedQuery(name = "Summerhousereservation.findMemberVacationInfo", query = "SELECT NEW models.VacationInfoDTO(s.summerhouse.number, s.summerhouse.id, s.fromDate, s.untilDate) FROM Summerhousereservation s WHERE s.member.id = :id"),
    @NamedQuery(name = "Summerhousereservation.findHouseReservationInfo", query = "SELECT NEW models.ReservationInfoDTO(s.member.firstName, s.member.lastName, s.fromDate, s.untilDate) FROM Summerhousereservation s WHERE s.summerhouse.id = :id"),
    @NamedQuery(name = "Summerhousereservation.findByFromDate", query = "SELECT s FROM Summerhousereservation s WHERE s.fromDate = :fromDate"),
    @NamedQuery(name = "Summerhousereservation.findByUntilDate", query = "SELECT s FROM Summerhousereservation s WHERE s.untilDate = :untilDate")})
public class Summerhousereservation implements Serializable {

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
    @Column(name = "FromDate")
    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UntilDate")
    @Temporal(TemporalType.DATE)
    private Date untilDate;

    @JoinColumn(name = "payment", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Payment payment;

    @JoinColumn(name = "MemberID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Clubmember member;

    @JoinColumn(name = "SummerhouseID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Summerhouse summerhouse;

    public Summerhousereservation() {
    }

    public Summerhousereservation(Date fromDate, Date untilDate, Clubmember member, Summerhouse summerhouse) {
        this.fromDate = fromDate;
        this.untilDate = untilDate;
        this.member = member;
        this.summerhouse = summerhouse;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getUntilDate() {
        return untilDate;
    }

    public void setUntilDate(Date untilDate) {
        this.untilDate = untilDate;
    }

    public Clubmember getMember() {
        return member;
    }

    public void setMember(Clubmember member) {
        this.member = member;
    }

    public Summerhouse getSummerhouse() {
        return summerhouse;
    }

    public void setSummerhouse(Summerhouse summerhouse) {
        this.summerhouse = summerhouse;
    }

    public int getOptLockVersion() {
        return optLockVersion;
    }

    public void setOptLockVersion(int optLockVersion) {
        this.optLockVersion = optLockVersion;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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
        if (!(object instanceof Summerhousereservation)) {
            return false;
        }
        Summerhousereservation other = (Summerhousereservation) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public String toString() {
        return "entities.Summerhousereservation[ id=" + id + " ]";
    }
    
}
