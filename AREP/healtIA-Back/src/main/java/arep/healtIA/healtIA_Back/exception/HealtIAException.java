package arep.healtIA.healtIA_Back.exception;

/**
 * Exception of the application.
 * @author Cristan Duarte.
 * @author Johann Amaya.
 * @author Sebastian Zamora.
 * @version 1.0
 */
public class HealtIAException extends Exception {
    public static final String USER_NOT_FOUND = "User not found, please insert a valid email";
    public static final String BAD_DATA = "Email or password are incorrect, please verify your data";
    public static final String USER_ALREADY_EXISTS = "User already exists, please insert an other email";

    /**
     * Constructor of the exception.
     * @param message: message of the exception.
     */
    public HealtIAException(String message) {
        super(message);
    }
}
