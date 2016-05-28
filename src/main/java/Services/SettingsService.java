package services;

import entities.Settings;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import javax.ejb.Stateless;

/**
 * Created by Gintautas on 4/24/2016.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SettingsService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public List<Settings> getSettings(){
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Settings.class));
        return em.createQuery(cq).getResultList();
    }

    public void editSettings(Settings settings) {
        Settings s = em.find(Settings.class, settings.getId());
        s.setValue(settings.getValue());
    }

    public Settings getSetting(String referenceCode){
        return (Settings)em.createNamedQuery("Settings.findByReferenceCode")
                .setParameter("referenceCode", referenceCode).getSingleResult();
    }

}
