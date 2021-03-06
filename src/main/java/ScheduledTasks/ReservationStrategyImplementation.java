package scheduledTasks;

import entities.Clubmember;
import entities.Summerhousereservation;
import restControllers.SummerhouseReservationREST;
import services.ClubMemberService;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Dziugas on 5/2/2016.
 */
@Alternative
public class ReservationStrategyImplementation implements ReservationPriorityStrategy {

    @Inject
    ClubMemberService memberService;

    @Override
    public void formPriorityGroups(int numberOfGroups) {
        List<Clubmember> members = new ArrayList<Clubmember>(memberService.getAllMembers());

        members.sort(new memberComparator());
        if (numberOfGroups == 0 ) {
            return;
        }

        double averageGroupSize = members.size() / numberOfGroups;
        long groupSize = averageGroupSize == 0 ? 1 : Math.round(averageGroupSize);


        int currentGroup = 0;
        int peopleInCurrentGroup = 0;
        for (Clubmember member : members){
            if (peopleInCurrentGroup >= groupSize && currentGroup != numberOfGroups){
                currentGroup ++;
                peopleInCurrentGroup = 0;
            }
            peopleInCurrentGroup ++;
            member.setReservationGroup(currentGroup);
            memberService.updateMember(member);
        }
    }
}

class memberComparator implements Comparator<Clubmember> {

    @Inject
    SummerhouseReservationREST summerhouseReservationREST;

    @Override
    public int compare(Clubmember member1, Clubmember member2) {
        // < 0   <=>   member1 < member2
        // = 0   <=>   member1 = member2
        // > 0   <=>   member1 > member2
        return countLastYearReservedNights(member1) < countLastYearReservedNights(member2)
                ? -1
                : 1;
    }

    private int countLastYearReservedNights(Clubmember member){
        int nights = 0;

        for (Summerhousereservation reservation : summerhouseReservationREST.findByClubmember(member.getId())){
            LocalDateTime date  = new LocalDateTime(reservation.getFromDate()).plusYears(1);
            if (LocalDateTime.now().isBefore(date)){
                LocalDateTime fromDate = new LocalDateTime(reservation.getFromDate());
                LocalDateTime toDate = new LocalDateTime(reservation.getUntilDate());
                nights += Period.fieldDifference(fromDate, toDate).getDays();
            }
        }
        return nights;
    }
}
