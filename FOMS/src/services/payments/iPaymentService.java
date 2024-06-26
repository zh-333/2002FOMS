package services.payments;

import java.util.ArrayList;

import utilities.exceptionHandlers.TransactionFailedException;

/**
 * This interface is implemented by every new type of payment service class. PaymentServices should be able to do the following functions.
 * Each type of payment service may have their own way of processing payment.
 * This interface exemplifies SRP and OCP -- the methods are focused, and extending of payment service types is fairly easy
 * @author Siah Yee Long
 */
public interface iPaymentService {
    /**
     * The name of the payment service
     * @return the name of the payment service
     */
    String getPaymentTypeName();
    /**
     * If the current payment service is enabled
     * @return true if enabled, false otherwise
     */
    boolean checkEnabled();
    /**
     * Method to enable / disable the payment service
     * @param enable to enable / disable the payment service 
     */
    void setEnabled(boolean enable);
    /**
     * The method used to make a transaction. Payment details stored in the transaction history
     * @param customerID the ID of the customer
     * @param amount the amount to pay
     * @throws TransactionFailedException if the transaction fails
     */
    void pay(int customerID, double amount) throws TransactionFailedException;
    /**
     * Method to get the transaction history of the payment service
     * @return the transaction history of the payment service
     */
    ArrayList<PaymentDetails> getTransactionHist();
}
