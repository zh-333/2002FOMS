package services.authenticator;
/**
 * This interface is used to define the methods that are required for the login service
 * For different login services (in the future), they must implement this interface (OCP)
 * @author Siah Yee Long
 */
public interface iLoginService {
    /**
     * Method to verify login
     * @param userID
     * @param password
     * @return true if login is successful, false otherwise
     */
    boolean login(String userID, String password);
    /**
     * Method to reset password
     * @param userID
     * @return true if password is reset successfully, false otherwise
     */
    boolean resetPassword(String userID);
    /**
     * Method to change password
     * @param userID
     * @param oldPassword
     * @param newPassword
     * @return true if password is changed successfully, false otherwise
     */
    boolean changePassword(String userID, String oldPassword, String newPassword);
}
