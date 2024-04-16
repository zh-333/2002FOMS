package pages.staffPages;

import java.util.Random;
import java.util.Scanner;

import utilities.Logger;
import utilities.Session;
import constants.Settings;
import pages.iPage;
import pages.pageViewer;
import services.authenticator.iLoginService;
import services.authenticator.StaffLoginService;

/**
 * This is the page that the staff will first see
 * This page facilitates the logging in of staff. When the staff has successfully logged in, the session's currentActiveStaff will be updated to that guy
 * Which implies that any manager-only-accessible page or admin-only-accessible pages can simply check the session's active staff to determine privileges
 * @author Siah Yee Long
 */
public class StaffLoginPage implements iPage {
    /**
     * The current active session 
     */
    private Session session;
    /**
     * The LoginService used for this page
     */
    private StaffLoginService staffLoginService;
    /**
     * Initialising this page sets the session provided from pageViewer
     * @param s
     */
    public StaffLoginPage(Session s){
        this.session = s;
        this.staffLoginService = new StaffLoginService(this.session.getAllStaffs());
    }
    /**
     * Method to view menu options
     */
    public void viewOptions(){
        System.out.println( "                   _____ _         __  __   _             _                       \n" + //
                            "                  / ____| |       / _|/ _| | |           (_)                      \n" + //
                            "  ______ ______  | (___ | |_ __ _| |_| |_  | | ___   __ _ _ _ __    ______ ______ \n" + //
                            " |______|______|  \\___ \\| __/ _` |  _|  _| | |/ _ \\ / _` | | '_ \\  |______|______|\n" + //
                            "                  ____) | || (_| | | | |   | | (_) | (_| | | | | |                \n" + //
                            "                 |_____/ \\__\\__,_|_| |_|   |_|\\___/ \\__, |_|_| |_|                \n" + //
                            "                                                     __/ |                        \n" + //
                            "                                                    |___/                         ");
        System.out.println("[1] Log in");
        System.out.println("[2] Forgot password");
        System.out.println("[3] Change password");
        System.out.println("[B] Go back");
    }
    /**
     * Method to handle user input 
     * @param choice branches the pages
     */
    public void handleInput(String choice){
        switch (choice) {
            case "1":
                boolean loginSuccess = tryLogin(staffLoginService);
                if (loginSuccess) {
                    System.out.println("LOGIN SUCCESS");
                    System.out.println("Welcome, " + this.session.getCurrentActiveStaff().getFirstName());

                    // log in success. go to staff access page
                    pageViewer.changePage("StaffAccessPage");
                } else {
                    System.out.println("Login failed.");
                    pageViewer.changePage("MainPage");
                }
                break;
            case "2":
                if(tryForgotPassword(staffLoginService)) System.out.println("OI STOP FUCKING FORGETTING. RESET SUCCESS ANYWAY");
                else System.out.println("failed reset pw");
                pageViewer.changePage("StaffLoginPage");
                break;
            case "3": 
                if(tryChangePassword(staffLoginService)) System.out.println(("CHANGE PASSWORD SUCCESS YAYYY"));
                pageViewer.changePage("StaffLoginPage");
                break;
            case "b":
            case "B":
                // goes back to MainPage
                pageViewer.changePage("MainPage");
                break;
            default:
                System.out.println("Invalid choice!");
                break;
        }
    }

    /**
     * The {@link tryLogin} method will allow the user a maximum of {@link Settings.PW_MAX_TRIES} tries for logging in
     * @param loginService is the service used to do the login
     * @return true if successful
     */
    private boolean tryLogin(iLoginService loginService){
        Scanner sc = new Scanner(System.in);
        String userID, password;

        for(int i=0; i<Settings.PW_MAX_TRIES.getValue(); i++){
            System.out.println("Enter your username:");
            userID = sc.nextLine().trim();
            System.out.println("Enter your password:");
            password = sc.nextLine().trim();

            if (loginService.login(userID, password)) {
                this.session.setCurrentActiveStaff(staffLoginService.getStaffByID(userID));
                return true;
            } else {
                System.out.println("XXX WRONG. WHAT A FAILURE");
            }
        }
        System.out.println("FAILED TOO MANY TIMES. BOO.");
        return false;
    }
    /**
     * The {@link tryForgotPassword} method resets the user's password by verifying that they are 
     * a human (simulated only) a maximum of {@link Settings.FORGOTPW_MAX_TRIES} times, and resets 
     * the user's password accordingly
     * @param loginService 
     * @return true if successful
     */
    private boolean tryForgotPassword(iLoginService loginService){
        String generatedValue;
        Scanner sc = new Scanner(System.in);

        for(int i=0; i<Settings.FORGOTPW_MAX_TRIES.getValue(); i++){
            System.out.println("Enter your username:");
            String userID = sc.nextLine();
            generatedValue = generateRandomString();
            System.out.println("Verify you're human. Type what you see on the screen: "+generatedValue);
            if(generatedValue.equals(sc.nextLine())){
                return loginService.resetPassword(userID);
            }
            System.out.println("WRONG! TRY AGAIN");
        }
        sc.close();
        return false;
    }
    /**
     * The {@link tryChangePassword} allows the user to change their password with a maximum of {@link Settings.PW_MAX_TRIES} tries
     * @param loginService
     * @return true if successful
     */
    private boolean tryChangePassword(iLoginService loginService){
        Scanner sc = new Scanner(System.in);
        String userID = "", oldPassword = "";

        for(int i=0; i<Settings.PW_MAX_TRIES.getValue(); i++){
            System.out.println("Enter your user ID:");
            userID = sc.nextLine();
            System.out.println("Enter your old password:");
            oldPassword = sc.nextLine();

            if(loginService.login(userID, oldPassword)) break; // login success. break from for loop
            System.out.println("WRONG. THINK HARDER. THINKKKKK");
            if(i+1 == Settings.PW_MAX_TRIES.getValue()) return false; // tried max times and still fail login
        }

        System.out.println("Enter the new password:");
        String newPassword = sc.nextLine();
        // check if password meets minimum length criteria
        while (newPassword.length() < Settings.PW_MIN_CHARACTERS.getValue()){
            System.out.println("Password length too short! Passwords must be at least " + Settings.PW_MIN_CHARACTERS.getValue() + " long");
            System.out.println("Enter the new password:");
            newPassword = sc.nextLine();
        }

        return loginService.changePassword(userID, oldPassword, newPassword);
    }
    /**
     * Just a helper function to generate a random string for human verification
     * @return an alphanumeric combination string
     */
    public static String generateRandomString() {
        int length = 6; // Length of the alphanumeric string
        StringBuilder sb = new StringBuilder(length);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char c = characters.charAt(random.nextInt(characters.length()));
            sb.append(c);
        }
        return sb.toString();
    }
}
