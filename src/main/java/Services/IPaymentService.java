package Services;

import Entities.Clubmember;
import Entities.Payment;
import Entities.Tax;
import Helpers.InsufficientFundsException;

/**
 * Created by Dziugas on 5/17/2016.
 */
public interface IPaymentService {
    void confirmPayment(Payment[] payments);
    void makePayment(Clubmember member, Tax tax) throws InsufficientFundsException;
    void makeMinusPayment(Clubmember member, Tax tax);
}
