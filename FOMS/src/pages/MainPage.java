package pages;

import services.ProcessOrderService;
import utilities.Session;

/**
 * This is the Main Page that the user will see. While it is called the Main Page, the user actually sees the SelectBranchPage before seeing this page.
 * This page facilitates the segregation between a customer and a staff.
 * @author Siah Yee Long
 */
public class MainPage implements iPage{
    /**
     * The session object 
     */
    private Session session;
    /**
     * Constructor for the MainPage class
     * @param s the current session
     */
    public MainPage(Session s){
        this.session = s;
    }
    /**
     * Method to view menu options
     */
    public void viewOptions(){
        System.out.println(" __          __  _                          \n" +
                           " \\ \\        / / | |                         \n" +
                           "  \\ \\  /\\  / /__| | ___ ___  _ __ ___   ___ \n" +
                           "   \\ \\/  \\/ / _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\\n" +
                           "    \\  /\\  /  __/ | (_| (_) | | | | | |  __/\n" +
                           "     \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|\n" +
                           "                                            \n" +
                           "                                            ");
        System.out.println("[1] Start Ordering");
        System.out.println("[2] Collect order");
        System.out.println("[3] Log in as staff");
        System.out.println("[B] Back to select branch");
    }
    /**
     * Method to handle user input 
     * @param choice branches the pages
     */
    public void handleInput(String choice){
        try{
            switch (choice) {
                case "1":
                    PageViewer.changePage("CustomerPage");
                    break;
                case "2":
                    this.session.updateSession();
                    ProcessOrderService.custViewOrderStatus(this.session.getAllOrders(), this.session.getCurrentActiveBranch());
                    PageViewer.changePage("current");
                    break;
                case "3":
                    PageViewer.changePage("StaffLoginPage");
                    break;
                case "b":
                case "B":
                    this.session.setCurrentActiveBranch(null);
                    PageViewer.changePage("back");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
                    viewOptions();
                    break;
            }
        }catch(Exception e){
            System.err.println("An unexpected error occured. Please try again!");
            viewOptions();
        }
    }
}
