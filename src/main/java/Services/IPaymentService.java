package services;

import entities.Clubmember;
import entities.Payment;
import helpers.InsufficientFundsException;

/**
 * Created by Dziugas on 5/17/2016.
 */
public interface IPaymentService {
    void confirmPayment(Payment[] payments);
    Payment makePayment(Clubmember member, int price, String name) throws InsufficientFundsException;
    void makeMinusPayment(Clubmember member, int price, String name);
}
