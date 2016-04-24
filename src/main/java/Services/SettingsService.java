package Services;

import Entities.Settings;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Gintautas on 4/24/2016.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SettingsService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU", type=TRANSACTION)
    private EntityManager em;

    public List<Settings> getSettings(){
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Settings.class));
        return em.createQuery(cq).getResultList();
    }
    public void editSettings(Settings settings) {
        em.merge(settings);
    }
}
