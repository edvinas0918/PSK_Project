package scheduledTasks;

import services.SettingsService;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 * Created by Dziugas on 5/2/2016.
 */
@Singleton
public class ReservationPriorityManager {

    @Inject
    ReservationPriorityStrategy priorityStrategy;

    @Inject
    SettingsService settingsService;

    @Schedule(month="1")
    public void reassignReservationPriorities(){
        int numberOfGroups = Integer.parseInt(settingsService.getSetting("reservationGroupNumber").getValue());
        priorityStrategy.formPriorityGroups(numberOfGroups);
    }
}
