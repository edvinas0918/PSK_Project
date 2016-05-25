package Services;

import Entities.Settings;
import models.SettingsDto;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
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

    public List<SettingsDto> getSettingsDto(){
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Settings.class));
        List<Settings> settings = em.createQuery(cq).getResultList();
        List<SettingsDto> settingsDto = new ArrayList<>();
        for(Settings setting : settings){
            settingsDto.add(
                    new SettingsDto(
                            setting.getId(),
                            setting.getName(),
                            setting.getValue(),
                            setting.getClass().getName()));
        }
        return settingsDto;
    }

    public void editSettings(SettingsDto settingsDto) {
        Settings settings = em.find(Settings.class, settingsDto.getId());
        settings.setValue(settingsDto.getValue());
    }

    public Settings getSetting(String referenceCode){
        return (Settings)em.createNamedQuery("Settings.findByReferenceCode")
                .setParameter("referenceCode", referenceCode).getSingleResult();
    }

}
