package services;

import entities.Clubmember;
import entities.Payment;
import helpers.InsufficientFundsException;
import interceptors.Audit;

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

    @Audit
    public Payment makePayment(Clubmember member, int price, String name) throws InsufficientFundsException{
        if (member.getPoints() < price){
            throw new InsufficientFundsException("Nepakankamas taškų skaičius.");
        }
        member.setPoints(member.getPoints() - price);

        Payment payment = null;
        try {
            em.merge(member);
            payment = savePayment(member, price, name, false);
        } catch (Exception ex) {
            member.setPoints(member.getPoints() + price);
        }
        return payment;
    }

    @Audit
    public void makeMinusPayment(Clubmember member, int price, String name){
        member.setPoints(member.getPoints() + price);
        em.merge(member);

        savePayment(member, price, name, true);
    }

    private Payment savePayment(Clubmember member, int price, String name, boolean isMinus){
        Calendar cal = Calendar.getInstance();
        Payment payment = new Payment();
        payment.setMemberID(member);
        payment.setPaymentDate(cal.getTime());
        payment.setName(name);
        payment.setConfirmed(false);
        payment.setPrice( isMinus ? -price : price);
        em.persist(payment);
        return payment;
    }
}
