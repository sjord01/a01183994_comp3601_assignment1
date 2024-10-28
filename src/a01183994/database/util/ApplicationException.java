package a01183994.database.util;

/**
 * ApplicationException is a custom exception class used to handle application-specific errors.
 * It extends the Exception class to allow checked exception handling.
 * 
 * @author Samson James Ordonez
 * @version 1.0
 */
public class ApplicationException extends Exception {
    
    private static final long serialVersionUID = 1L;

	/**
     * Constructs a new ApplicationException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public ApplicationException(String message) {
        super(message);
    }
}