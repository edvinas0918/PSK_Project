package Services;

import Entities.Settings;
import Entities.Tax;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Gintautas on 4/24/2016.
 */
@Stateless
public class TaxService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public Tax getTax(Object id){
        return em.find(Tax.class, id);
    }

    public void editTax(Tax tax) {
        em.merge(tax);
    }
}
