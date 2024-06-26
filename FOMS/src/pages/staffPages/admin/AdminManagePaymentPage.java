package pages.staffPages.admin;

import pages.iPage;
import pages.PageViewer;
import services.ManagePaymentsService;
import utilities.Session;

/**
 * This page allows the admin to manage payment methods
 * The admin can view all payment methods, enable a payment method, and disable a payment method
 * @author Alvin Aw Yong
 * @author Siah Yee Long
 */
public class AdminManagePaymentPage implements iPage {
    /**
     * The current active session
     */
    private Session session;
    /**
     * Constructor for AdminManagePaymentPage
     * @param session the current session
     */
    public AdminManagePaymentPage(Session session) {
        this.session = session;
    }
    /**
     * Method to view Admin Manage Payment Methods Page
     */
    public void viewOptions() {
        System.out.println("Current available payment methods:");
        ManagePaymentsService.viewAllPaymentMethods(this.session);
        System.out.println("== FOMS | Admin Manage Payment Methods ==");
        System.out.println("[1] Enable a payment method");
        System.out.println("[2] Disable a payment method");
        System.out.println("[B] Go back");
    }
    /**
     * Method to handle user input. Calls ManagePaymentsService to do the job instead
     * @param option: user input
     */
    public void handleInput(String option) {
        switch (option) {
            case "1":
                ManagePaymentsService.enablePaymentsService(this.session);
                PageViewer.changePage("current");
                break;
            case "2":
                ManagePaymentsService.disablePaymentsService(this.session);
                PageViewer.changePage("current");
                break;
            case "B":
            case "b":
                PageViewer.changePage("back");
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }
}