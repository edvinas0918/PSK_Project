package services;

import entities.*;
import helpers.BadRequestException;
import helpers.DateTermException;
import helpers.InsufficientFundsException;
import restControllers.AuthenticationControllerREST;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Weeks;

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

    @Inject
    private SettingsService settingsService;

    public void validatePeriod(Date beginPeriod, Date endPeriod) throws BadRequestException {
        if (endPeriod.compareTo(beginPeriod) != 1) {
            throw new BadRequestException("Neteisingai įvestas laikotarpis");
        }

        int intervalInDays = (int) SummerhouseReservation.getDateDiff(beginPeriod, endPeriod, TimeUnit.DAYS);

        if (intervalInDays == 0 || (intervalInDays + 1) % 7 != 0) {
            throw new BadRequestException("Rezervuoti galima tik savaitei, dviem ir t.t.");
        }

        Calendar cal = Calendar.getInstance();

        cal.setTime(beginPeriod);
        int beginWeekDay = cal.get(Calendar.DAY_OF_WEEK);
        cal.setTime(endPeriod);
        int endWeekDay = cal.get(Calendar.DAY_OF_WEEK);

        if (beginWeekDay != Calendar.MONDAY || endWeekDay != Calendar.SUNDAY) {
            throw new BadRequestException("Rezervuoti galima laikotarpiui, kuris prasideda pirmadienį ir baigiasi sekmadienį.");
        }
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public void checkAvailabilityPeriod(Summerhousereservation reservation, List<Summerhousereservation> otherReservations)
            throws BadRequestException {

        Date beginPeriod = reservation.getFromDate();
        Date endPeriod = reservation.getUntilDate();
        Summerhouse summerhouse = reservation.getSummerhouse();

        //check availability period
        if (endPeriod.compareTo(beginPeriod) != 1) {
            throw new BadRequestException("Neteisingai įvestas laikotarpis");
        }

        if (beginPeriod.compareTo(summerhouse.getBeginPeriod()) == -1 || endPeriod.compareTo(summerhouse.getEndPeriod()) == 1) {
            throw new BadRequestException("Šiuo laikotarpiu rezervacija nėra galima.");
        }

        //check if not reserved already
        for (Summerhousereservation otherReservation :
                otherReservations) {
            if (dateIsInPeriod(beginPeriod, otherReservation.getFromDate(), otherReservation.getUntilDate()) ||
                    dateIsInPeriod(endPeriod, otherReservation.getFromDate(), otherReservation.getUntilDate())) {
                throw new BadRequestException("Šiuo laikotarpiu vasarnamis yra užimtas.");
            }
        }
    }

    private static boolean dateIsInPeriod(Date date, Date beginPeriod, Date endPeriod) {
        return !(date.compareTo(beginPeriod) == -1) &&
                !(date.compareTo(endPeriod) == 1);
    }

    public void checkReservationGroup(Summerhousereservation reservation) throws BadRequestException {

        Settings setting = settingsService.getSetting("reservationStartDate");
        DateTime reservationStartDate = DateTime.parse(setting.getValue());
        if (DateTime.now().isBefore(reservationStartDate)){
            throw new BadRequestException("Registracija dar neprasidėjo.");
        }
        int difference = Days.daysBetween(reservationStartDate.toLocalDate(), DateTime.now().toLocalDate()).getDays();

        int reservationGroup = reservation.getMember().getReservationGroup();

        int maxReservationGroupAllowed = difference / 7;
        if (maxReservationGroupAllowed  < reservationGroup){
            throw new BadRequestException("Jūs galėsite atlikti rezervaciją tik po " +
                    (reservationGroup - maxReservationGroupAllowed) + "sav.");
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

    public Payment getPayment (Summerhousereservation reservation) throws InsufficientFundsException {
        int price = reservation.getSummerhouse().getReservationPrice() * getWeekDiff(reservation.getFromDate(), reservation.getUntilDate());
        return paymentService.makePayment(authenticationControllerREST.getSessionUser(), price, "Vasarnamis " + reservation.getSummerhouse().getNumber()
                + " " + reservation.getFromDate() + " " + reservation.getUntilDate());
    }

    public int getWeekDiff (Date fromDate, Date untilDate) {
        DateTime from = new DateTime(fromDate);
        DateTime until = new DateTime(untilDate).plusDays(1);

        return Weeks.weeksBetween(from, until).getWeeks();
    }

    public void checkMembership() throws BadRequestException{
        Clubmember user = authenticationControllerREST.getSessionUser();
        Date membershipDate = user.getMembershipExpirationDate();

        if (membershipDate == null || user.getMembershipExpirationDate().compareTo(new Date()) == -1) {
            throw new BadRequestException ("Rezervuoti gali tik tie nariai, kurie yra sumokėję metinį klubo nario mokestį");
        }
    }
}
