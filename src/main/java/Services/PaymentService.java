package Services;

import Entities.Clubmember;
import Entities.Payment;
import Entities.Tax;
import Helpers.InsufficientFundsException;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Calendar;

/**
 * Created by Gintautas on 4/28/2016.
 */

@Stateless
public class PaymentService implements IPaymentService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public void confirmPayment(Payment[] payments) {
        for (Payment item: payments) {
            Payment payment = em.find(Payment.class, item.getId());
            payment.setConfirmed(true);
        }
    }

    public void makePayment(Clubmember member, Tax tax) throws InsufficientFundsException{
        if (member.getPoints() < tax.getPrice()){
            throw new InsufficientFundsException("Nepakankamas taškų skaičius.");
        }
        member.setPoints(member.getPoints() - tax.getPrice());
        em.merge(member);

        savePayment(member, tax);
    }

    private void savePayment(Clubmember member, Tax tax){
        Calendar cal = Calendar.getInstance();
        Payment payment = new Payment();
        payment.setMemberID(member);
        payment.setPaymentDate(cal.getTime());
        payment.setTaxID(tax);
        payment.setConfirmed(false);
        em.persist(payment);
    }
}
