package discounts;

import entities.Clubmember;
import entities.Payment;
import helpers.InsufficientFundsException;
import services.ClubMemberService;
import services.IPaymentService;
import org.joda.time.LocalDateTime;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;

/**
 * Created by Dziugas on 5/17/2016.
 */
@Decorator
public abstract class ChristmasDiscount implements IPaymentService {
    @Inject @Delegate @Any
    IPaymentService paymentService;

    @Inject
    ClubMemberService memberService;

    private double discountPercentage = 0.3;
    LocalDateTime discountStartDate;
    LocalDateTime discountEndDate;

    public ChristmasDiscount(){
        discountPercentage = 0.3;
        discountStartDate = new LocalDateTime().withMonthOfYear(12).withDayOfMonth(1);
        discountEndDate = new LocalDateTime().withMonthOfYear(12).withDayOfMonth(31);
    }

    @Override
    public Payment makePayment(Clubmember member, int price, String name) throws InsufficientFundsException {
        Payment payment = paymentService.makePayment(member, price, name);

        LocalDateTime timeNow = new LocalDateTime();
        discountStartDate.withYear(timeNow.getYear());
        discountEndDate.withYear(timeNow.getYear());
        if (timeNow.isBefore(discountEndDate) && timeNow.isAfter(discountStartDate)){
            int discountAmount = (int) Math.round(price * discountPercentage);
            member.setPoints(member.getPoints() + discountAmount);
            memberService.updateMember(member);
        }
        return payment;
    }
}
