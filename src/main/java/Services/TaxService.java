package Services;

import Entities.Settings;
import Entities.Tax;
import models.SettingsDto;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Gintautas on 4/24/2016.
 */
@Stateless
public class TaxService {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    private static int MemberTaxId = 1;

    public SettingsDto getSettingsDto(){
        Tax tax = em.find(Tax.class, MemberTaxId);
        return new SettingsDto(tax.getId(), tax.getName(), String.valueOf(tax.getPrice()), tax.getClass().getName());
    }

    public void editTax(SettingsDto settings){
        Tax tax = em.find(Tax.class, settings.getId());
        tax.setPrice(Integer.parseInt(settings.getValue()));
    }
}
