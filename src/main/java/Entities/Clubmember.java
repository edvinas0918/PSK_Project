/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Dziugas
 */
@Entity
@Table(name = "clubmember")
@NamedQueries({
        @NamedQuery(name = "Clubmember.findAll", query = "SELECT c FROM Clubmember c"),
        @NamedQuery(name = "Clubmember.findById", query = "SELECT c FROM Clubmember c WHERE c.id = :id"),
        @NamedQuery(name = "Clubmember.findByFirstName", query = "SELECT c FROM Clubmember c WHERE c.firstName = :firstName"),
        @NamedQuery(name = "Clubmember.findByLastName", query = "SELECT c FROM Clubmember c WHERE c.lastName = :lastName"),
        @NamedQuery(name = "Clubmember.findByEmail", query = "SELECT c FROM Clubmember c WHERE c.email = :email"),
        @NamedQuery(name = "Clubmember.findByPoints", query = "SELECT c FROM Clubmember c WHERE c.points = :points"),
        @NamedQuery(name = "Clubmember.findByReservationGroup", query = "SELECT c FROM Clubmember c WHERE c.reservationGroup = :reservationGroup"),
        @NamedQuery(name = "Clubmember.findByToken", query = "SELECT c FROM Clubmember c WHERE c.token = :token"),
        @NamedQuery(name = "Clubmember.findByfbUserId", query = "SELECT c FROM Clubmember c WHERE c.fbUserId = :fbUserId"),
        @NamedQuery(name = "Clubmember.findByMembershipExpirationDate", query = "SELECT c FROM Clubmember c WHERE c.membershipExpirationDate = :membershipExpirationDate")
})


@XmlRootElement
public class Clubmember implements Serializable {

    @Size(max = 200)
    @Column(name = "token")
    private String token;
    @Size(max = 20)
    @Column(name = "fbUserId")
    private String fbUserId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "FirstName")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "LastName")
    private String lastName;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "Email")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Points")
    private int points;

    @Temporal(TemporalType.DATE)
    @Column(name = "membershipExpirationDate")
    private Date membershipExpirationDate;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ReservationGroup")
    private int reservationGroup;
    @JoinTable(name = "recommendation", joinColumns = {
            @JoinColumn(name = "MemberID", referencedColumnName = "ID")}, inverseJoinColumns = {
            @JoinColumn(name = "RecommendedMemberID", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Clubmember> clubmemberList;
    @ManyToMany(mappedBy = "clubmemberList", fetch = FetchType.EAGER)
    private List<Clubmember> clubmemberList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberID", fetch = FetchType.EAGER)
    private List<Summerhousereservation> summerhousereservationList;
    @JoinColumn(name = "MemberStatusID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Memberstatus memberStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberID", fetch = FetchType.EAGER)
    private List<Invitation> invitationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "memberID", fetch = FetchType.EAGER)
    private List<Payment> paymentList;

    public Clubmember() {
    }

    public Clubmember(Integer id) {
        this.id = id;
    }

    public Clubmember(Integer id, String firstName, String lastName, String email, int points, int reservationGroup) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.points = points;
        this.reservationGroup = reservationGroup;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getReservationGroup() {
        return reservationGroup;
    }

    public void setReservationGroup(int reservationGroup) {
        this.reservationGroup = reservationGroup;
    }

    public Date getMembershipExpirationDate() {
        return membershipExpirationDate;
    }

    public void setMembershipExpirationDate(Date membershipExpirationDate) {
        this.membershipExpirationDate = membershipExpirationDate;
    }

    @XmlTransient
    public List<Clubmember> getClubmemberList() {
        return clubmemberList;
    }

    public void setClubmemberList(List<Clubmember> clubmemberList) {
        this.clubmemberList = clubmemberList;
    }

    @XmlTransient
    public List<Clubmember> getClubmemberList1() {
        return clubmemberList1;
    }

    public void setClubmemberList1(List<Clubmember> clubmemberList1) {
        this.clubmemberList1 = clubmemberList1;
    }

    @XmlTransient
    public List<Summerhousereservation> getSummerhousereservationList() {
        return summerhousereservationList;
    }

    public void setSummerhousereservationList(List<Summerhousereservation> summerhousereservationList) {
        this.summerhousereservationList = summerhousereservationList;
    }

    public Memberstatus getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Memberstatus memberStatus) {
        this.memberStatus = memberStatus;
    }

    @XmlTransient
    public List<Invitation> getInvitationList() {
        return invitationList;
    }

    public void setInvitationList(List<Invitation> invitationList) {
        this.invitationList = invitationList;
    }

    @XmlTransient
    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
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
        if (!(object instanceof Clubmember)) {
            return false;
        }
        Clubmember other = (Clubmember) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Clubmember[ id=" + id + " ]";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFbUserId() {
        return fbUserId;
    }

    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
    }

}
