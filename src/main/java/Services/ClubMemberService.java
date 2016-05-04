package Services;

import Entities.Clubmember;
import Entities.Payment;
import Entities.Tax;
import Helpers.MembershipException;
import Interceptors.Audit;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Calendar;
import java.util.List;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Dziugas on 4/25/2016.
 */
@Stateless
public class ClubMemberService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU", type=TRANSACTION)
    private EntityManager em;

    public Clubmember getMember(Integer id){
        return em.find(Clubmember.class, id);
    }

    public List<Clubmember> getAllMembers() {
        return em.createNamedQuery("Clubmember.findAll").getResultList();
    }

    public void updateMember(Clubmember member) {
        em.merge(member);
    }

    @Audit
    public void grantPoints(Clubmember member, Integer points){
        if (points > 0){
            member.setPoints(member.getPoints() + points);
            em.persist(member);
        }
    }

    public void renewMembership(Clubmember member) throws MembershipException{
        //Adding year to membership year
        Calendar c = Calendar.getInstance();
        c.setTime(member.getMembershipExpirationDate());
        c.add(Calendar.YEAR, 1);
        member.setMembershipExpirationDate(c.getTime());

        //Updating member points
        Integer memberTax = ((Tax)em.createNamedQuery("Tax.findMemberTax").getSingleResult()).getPrice();
        Integer memberPoints = member.getPoints();

        if (memberPoints < memberTax)
            throw new MembershipException("Nepakankamas taškų skaičius.");

        member.setPoints(memberPoints - memberTax);
        em.merge(member);

        //Creating Payment
        Calendar cal = Calendar.getInstance();
        Payment payment = new Payment();
        payment.setMemberID(member);
        payment.setPaymentDate(cal.getTime());
        payment.setTaxID((Tax)em.createNamedQuery("Tax.findMemberTax").getSingleResult());
        payment.setConfirmed(false);
        em.persist(payment);
    }
}
