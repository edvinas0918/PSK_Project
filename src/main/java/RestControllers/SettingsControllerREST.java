package RestControllers;

import Entities.Settings;
import Services.SettingsService;
import models.SettingsDto;

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
@Stateless
@Path("settings")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SettingsControllerREST{
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Inject private SettingsService settingsService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<SettingsDto> getSettingsDto() {
        List<SettingsDto> settings = settingsService.getSettingsDto();
        return settings;
    }

    @GET
    @Path("{referenceCode}")
    @Produces({MediaType.APPLICATION_JSON})
    public Settings findbByRefernceCode(@PathParam("referenceCode") String referenceCode) {
        return settingsService.getSetting(referenceCode);
    }

    @PUT
    @Consumes({MediaType.APPLICATION_JSON})
    public void saveSettings(List<SettingsDto> settingsDto) {
        for(SettingsDto item : settingsDto){
            if(item.getType().endsWith("Settings")){
                settingsService.editSettings(item);
            }
        }
    }
}
