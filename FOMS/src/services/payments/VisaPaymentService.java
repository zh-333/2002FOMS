package services.payments;

import java.util.ArrayList;

import utilities.Logger;
import utilities.UserInputHelper;
import utilities.exceptionHandlers.PaymentServiceDisabledException;
import utilities.exceptionHandlers.TransactionFailedException;

/**
 * This class is used to handle Visa transactions. It implements iPaymentService
 * @author Siah Yee Long
 */
public class VisaPaymentService implements iPaymentService{
    /**
     * Determines if current payment service is enabled
     */
    private boolean isEnabled;
    /**
     * A list of PaymentDetails which used this service
     */
    private ArrayList<PaymentDetails> transactionHist;
    /**
     * Constructor called from Session
     */
    public VisaPaymentService(){
        this.isEnabled = true;
        transactionHist = new ArrayList<>();
    }
    /**
     * @return the name of the payment service
     */
    @Override
    public String getPaymentTypeName(){ return "Visa"; }
    /**
     * @return if the current payment service is enabled
     */
    @Override
    public boolean checkEnabled(){ return this.isEnabled; }
    /**
     * @param enable to enable / disable the payment service 
     */
    @Override
    public void setEnabled(boolean enable){
        this.isEnabled = enable;
    }
    /**
     * The method used to make a transaction. Payment details stored in the transaction history
     * @param customerID the ID of the customer
     * @param amount the amount to pay
     */
    @Override
    public void pay(int customerID, double amount) throws TransactionFailedException{
        if(this.isEnabled){
            try{
                simulateSwipe(amount);
                this.transactionHist.add(new PaymentDetails(customerID, amount, this));
                System.out.println(Logger.ANSI_GREEN+"Payment via " + this.getPaymentTypeName() + " successful."+Logger.ANSI_RESET);
                return;
            }
            catch (TransactionFailedException e){throw e;}
        }
        else
            throw new PaymentServiceDisabledException(Logger.ANSI_RED+this.getPaymentTypeName() + " is currently disabled!"+Logger.ANSI_RESET);
    }
    /**
     * @return an array list of payment details
     */
    @Override
    public ArrayList<PaymentDetails> getTransactionHist(){
        return this.transactionHist;
    }
    /**
     * Method to simulate a swipe of a Visa card
     * @param amount the amount to pay
     * @throws TransactionFailedException if the transaction fails
     */
    private void simulateSwipe(double amount) throws TransactionFailedException{
        // get user to type "tap" to simulate Apple pay payment
        String simulatedSwipe = UserInputHelper.getInput("Please swipe your card to transact $"+String.format("%.2f", amount)+". (type 'swipe'):");
        if(simulatedSwipe.equalsIgnoreCase("swipe")) return;
        else throw new TransactionFailedException(Logger.ANSI_RED+"Visa transaction failed"+Logger.ANSI_RESET);
    }
}
