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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "member")
@NamedQueries({
    @NamedQuery(name = "Member1.findAll", query = "SELECT m FROM Member1 m"),
    @NamedQuery(name = "Member1.findByIDMember", query = "SELECT m FROM Member1 m WHERE m.iDMember = :iDMember"),
    @NamedQuery(name = "Member1.findByFirstName", query = "SELECT m FROM Member1 m WHERE m.firstName = :firstName"),
    @NamedQuery(name = "Member1.findByLastName", query = "SELECT m FROM Member1 m WHERE m.lastName = :lastName"),
    @NamedQuery(name = "Member1.findByEmail", query = "SELECT m FROM Member1 m WHERE m.email = :email"),
    @NamedQuery(name = "Member1.findByPoints", query = "SELECT m FROM Member1 m WHERE m.points = :points"),
    @NamedQuery(name = "Member1.findByReservationGroup", query = "SELECT m FROM Member1 m WHERE m.reservationGroup = :reservationGroup")})
public class Member1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID_Member")
    private Integer iDMember;
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "ReservationGroup")
    private int reservationGroup;
    @JoinTable(name = "recommendation", joinColumns = {
        @JoinColumn(name = "ID_Member", referencedColumnName = "ID_Member")}, inverseJoinColumns = {
        @JoinColumn(name = "Recommended", referencedColumnName = "ID_Member")})
    @ManyToMany
    private List<Member1> member1List;
    @ManyToMany(mappedBy = "member1List")
    private List<Member1> member1List1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDMember")
    private List<Summerhousereservation> summerhousereservationList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDMember")
    private List<Invitation> invitationList;
    @JoinColumn(name = "ID_MemberStatus", referencedColumnName = "ID_MemberStatus")
    @ManyToOne(optional = false)
    private Memberstatus iDMemberStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "iDMember")
    private List<Payment> paymentList;

    public Member1() {
    }

    public Member1(Integer iDMember) {
        this.iDMember = iDMember;
    }

    public Member1(Integer iDMember, String firstName, String lastName, String email, int points, int reservationGroup) {
        this.iDMember = iDMember;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.points = points;
        this.reservationGroup = reservationGroup;
    }

    public Integer getIDMember() {
        return iDMember;
    }

    public void setIDMember(Integer iDMember) {
        this.iDMember = iDMember;
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

    public List<Member1> getMember1List() {
        return member1List;
    }

    public void setMember1List(List<Member1> member1List) {
        this.member1List = member1List;
    }

    public List<Member1> getMember1List1() {
        return member1List1;
    }

    public void setMember1List1(List<Member1> member1List1) {
        this.member1List1 = member1List1;
    }

    public List<Summerhousereservation> getSummerhousereservationList() {
        return summerhousereservationList;
    }

    public void setSummerhousereservationList(List<Summerhousereservation> summerhousereservationList) {
        this.summerhousereservationList = summerhousereservationList;
    }

    public List<Invitation> getInvitationList() {
        return invitationList;
    }

    public void setInvitationList(List<Invitation> invitationList) {
        this.invitationList = invitationList;
    }

    public Memberstatus getIDMemberStatus() {
        return iDMemberStatus;
    }

    public void setIDMemberStatus(Memberstatus iDMemberStatus) {
        this.iDMemberStatus = iDMemberStatus;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDMember != null ? iDMember.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Member1)) {
            return false;
        }
        Member1 other = (Member1) object;
        if ((this.iDMember == null && other.iDMember != null) || (this.iDMember != null && !this.iDMember.equals(other.iDMember))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Member1[ iDMember=" + iDMember + " ]";
    }
    
}
