package Services;

import Entities.*;
import Helpers.DateTermException;
import RestControllers.AuthenticationControllerREST;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static javax.persistence.PersistenceContextType.TRANSACTION;

/**
 * Created by Edvinas.Barickis on 5/15/2016.
 */

@Stateless
public class SummerhouseReservation {
    @PersistenceContext(unitName = "com.psk_LabanorasFriends_war_1.0-SNAPSHOTPU", type=TRANSACTION)
    private EntityManager em;

    @Inject
    private AuthenticationControllerREST authenticationControllerREST;

    @Inject
    private IPaymentService paymentService;

    public void validatePeriod(Date beginPeriod, Date endPeriod) throws Exception {
        if (endPeriod.compareTo(beginPeriod) != 1) {
            throw new Exception("Neteisingai įvestas laikotarpis");
        }

        int intervalInDays = (int) SummerhouseReservation.getDateDiff(beginPeriod, endPeriod, TimeUnit.DAYS);

        if (intervalInDays == 0 || (intervalInDays + 1) % 7 != 0) {
            throw new Exception("Rezervuoti galima tik savaitei, dviem ir t.t.");
        }

        Calendar cal = Calendar.getInstance();

        cal.setTime(beginPeriod);
        int beginWeekDay = cal.get(Calendar.DAY_OF_WEEK);
        cal.setTime(endPeriod);
        int endWeekDay = cal.get(Calendar.DAY_OF_WEEK);

        if (beginWeekDay != Calendar.MONDAY || endWeekDay != Calendar.SUNDAY) {
            throw new Exception("Rezervuoti galima laikotarpiui, kuris prasideda pirmadienį ir baigiasi sekmadienį.");
        }
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public void checkAvailabilityPeriod(Summerhousereservation reservation, List<Summerhousereservation> otherReservations) throws Exception {

        Date beginPeriod = reservation.getFromDate();
        Date endPeriod = reservation.getUntilDate();
        Summerhouse summerhouse = reservation.getSummerhouse();

        //check availability period
        if (endPeriod.compareTo(beginPeriod) != 1) {
            throw new Exception("Neteisingai įvestas laikotarpis");
        }

        if (beginPeriod.compareTo(summerhouse.getBeginPeriod()) == -1 || endPeriod.compareTo(summerhouse.getEndPeriod()) == 1) {
            throw new Exception("Šiuo laikotarpiu rezervacija nėra galima.");
        }

        //check if not reserved already
        for (Summerhousereservation otherReservation :
                otherReservations) {
            if (dateIsInPeriod(beginPeriod, otherReservation.getFromDate(), otherReservation.getUntilDate()) ||
                    dateIsInPeriod(endPeriod, otherReservation.getFromDate(), otherReservation.getUntilDate())) {
                throw new Exception("Šiuo laikotarpiu vasarnamis yra užimtas.");
            }
        }
    }

    private static boolean dateIsInPeriod(Date date, Date beginPeriod, Date endPeriod) {
        return !(date.compareTo(beginPeriod) == -1) &&
                !(date.compareTo(endPeriod) == 1);
    }

    public void checkReservationGroup(Summerhousereservation reservation) throws Exception {
        int reservationGroup = reservation.getMember().getReservationGroup();
        int weekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        int difference = reservationGroup - weekOfYear;

        if (difference > 0) {
            throw new Exception("Jūs galėsite atlikti rezervaciją tik po " + difference + "sav.");
        }
    }

    public List<Summerhouse> getAvailableSummerhousesInPeriod(List<Summerhouse> summerhouses, Date fromDate, Date untilDate) {
        List<Summerhouse> availableHouses = new ArrayList<>();
        for (Summerhouse house : summerhouses) {
            int year = new DateTime(fromDate).getYear();
            Date reservationBeginPeriod = new DateTime(house.getBeginPeriod()).withYear(year).toDate();
            Date reservationEndPeriod = new DateTime(house.getEndPeriod()).withYear(year).toDate();

            if (dateIsInPeriod(fromDate, reservationBeginPeriod, reservationEndPeriod)
                    && dateIsInPeriod(untilDate, reservationBeginPeriod, reservationEndPeriod)
                    && !summerHouseHasReservationInPeriod(house, fromDate, untilDate)) {
                availableHouses.add(house);
            }
        }
        return availableHouses;
    }

    private boolean summerHouseHasReservationInPeriod(Summerhouse summerHouse, Date fromDate, Date untilDate) {
        for (Summerhousereservation reservation : summerHouse.getSummerhousereservationList()) {
            if (dateIsInPeriod(reservation.getFromDate(), fromDate, untilDate)) {
                return true;
            }
        }
        return false;
    }

    public void cancelReservation(Summerhousereservation reservation, Clubmember clubmember) throws DateTermException {
        //1. Check if available for cancelation
        Integer daysBeforeCancellation = Integer.parseInt(em.createNamedQuery("Settings.findByReferenceCode", Settings.class).
                setParameter("referenceCode", "reservationCancellationDeadline").getSingleResult().getValue());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysBeforeCancellation);
        Calendar reservationCalendar = Calendar.getInstance();
        reservationCalendar.setTime(reservation.getFromDate());
        if(calendar.after(reservationCalendar)){
            String errorMessage = String.format("Atšaukimas negalimas. Rezervaciją galima atšaukti tik likus ne mažiau nei %d dienai/dienoms.",
                    daysBeforeCancellation);
            throw new DateTermException(errorMessage);
        }

        //2. Cancel related payment
        Payment payment = em.find(Payment.class, reservation.getPayment().getId());
        payment.setCanceled(true);

        //3. Give points back to user
        paymentService.makeMinusPayment(clubmember, payment.getPrice(), payment.getName());
    }

    public void checkMoney(Summerhousereservation reservation) throws Exception {
        int requiredAmount = reservation.getSummerhouse().getReservationPrice();
        //TODO: uncomment when additional services are implemented.
       /* for (Additionalservicereservation additionalService:
             reservation.getAdditionalServiceReservations()) {
            requiredAmount += additionalService.getTaxID().getPrice();
        }*/

        if (authenticationControllerREST.getSessionUser().getPoints() < requiredAmount) {
            throw new Exception("Nepakankamas taškų kiekis");
        }
    }
}
