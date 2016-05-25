package Services;

import Entities.Clubmember;
import Entities.Payment;
import Helpers.InsufficientFundsException;

/**
 * Created by Dziugas on 5/17/2016.
 */
public interface IPaymentService {
    void confirmPayment(Payment[] payments);
    void makePayment(Clubmember member, int price, String name) throws InsufficientFundsException;
    void makeMinusPayment(Clubmember member, int price, String name);
}
