package Services;

import Entities.Payment;
import Entities.Tax;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Gintautas on 4/28/2016.
 */

@Stateless
public class PaymentService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public void confirmPayment(Payment[] payments) {
        for (Payment item: payments) {
            Payment payment = em.find(Payment.class, item.getId());
            payment.setConfirmed(true);
        }
    }
}
