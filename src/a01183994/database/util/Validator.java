package a01183994.database.util;

import java.util.regex.Pattern;

/**
 * Currently, it includes functionality to validate email addresses.
 * 
 * This class is designed to be extended in the future for additional validation needs.
 * 
 * @author Samson James Ordonez
 * @version 1.1
 */
public class Validator {

    // Regular expression for a basic email format validation
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    
    // Precompiled Pattern for email validation using the above regular expression
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    /**
     * Validates the given email address against a basic regular expression pattern.
     * If the email address is valid, it returns the email. Otherwise, it throws an
     * ApplicationException with an appropriate error message.
     * 
     * @param email The email address to validate
     * @return The original email if valid
     * @throws ApplicationException If the email address is invalid
     */
    public static String validateEmail(String email) throws ApplicationException {
        // Check if the email is not null and matches the defined pattern
        if (email != null && EMAIL_PATTERN.matcher(email).matches()) {
            return email;
        } else {
            throw new ApplicationException("Invalid email address: " + email);
        }
    }
}