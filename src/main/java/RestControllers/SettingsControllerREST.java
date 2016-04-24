package RestControllers;

import Entities.Clubmember;
import Entities.Settings;
import Entities.Tax;
import Services.SettingsService;
import Services.TaxService;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Gintautas on 4/24/2016.
 */
@Stateless
@Path("settings")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SettingsControllerREST{
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU", type=TRANSACTION)
    private EntityManager em;

    @Inject private SettingsService settingsService;
    @Inject private TaxService taxService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Settings> findAll() {
        List<Settings> settings = settingsService.getSettings();
        return settings;
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(List<Settings> settings) {
        for(Settings item : settings){
            settingsService.editSettings(item);
        }
    }
}
