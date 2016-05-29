package restControllers;

import entities.Settings;
import interceptors.Authentication;
import interceptors.ExceptionHandler;
import services.SettingsService;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Gintautas on 4/24/2016.
 */
@ExceptionHandler
@Stateless
@Path("settings")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SettingsControllerREST{
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject private SettingsService settingsService;

    @GET
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public List<Settings> getSettings() {
        return settingsService.getSettings();
    }

    @GET
    @Path("{referenceCode}")
    @Authentication(role = {"Member", "Admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public Settings findbByRefernceCode(@PathParam("referenceCode") String referenceCode) {
        return settingsService.getSetting(referenceCode);
    }

    @PUT
    @Authentication(role = {"Admin"})
    @Consumes({MediaType.APPLICATION_JSON})
    public void saveSettings(List<Settings> settings) {
        for(Settings setting : settings){
            settingsService.editSettings(setting);
        }
    }
}
