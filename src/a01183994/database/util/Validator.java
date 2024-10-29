package a01183994.database.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Currently, it includes functionality to validate email addresses.
 * 
 * This class is designed to be extended in the future for additional validation needs.
 * 
 * @author Samson James Ordonez
 * @version 1.0
 */
public class Validator {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
//    private static final String PHONE_REGEX = "^\\d{3}-\\d{3}-\\d{4}$";
//    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    
    private static final String PHONE_REGEX = "^\\s*(?:\\d{1,3}[-\\s]*){3,4}\\d{1,4}\\s*$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    
    private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final Pattern DATE_PATTERN = Pattern.compile(DATE_REGEX);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Validates that the given string is not null, empty, or only whitespace.
     * If the string is valid, it returns the trimmed string. Otherwise, it throws an
     * ApplicationException with an appropriate error message.
     *
     * @param input The string to validate
     * @return The trimmed string if valid
     * @throws ApplicationException If the string is null, empty, or only whitespace
     */
    public static String validateString(String input) throws ApplicationException {
        if (input == null || input.trim().isEmpty()) {
            throw new ApplicationException("Input cannot be empty or only whitespace");
        }
        return input.trim();
    }
    
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
    
    /**
     * Validates the given phone number against the format xxx-xxx-xxxx.
     * If the phone number is valid, it returns the phone number. Otherwise, it throws an
     * ApplicationException with an appropriate error message.
     *
     * @param phoneNumber The phone number to validate
     * @return The original phone number if valid
     * @throws ApplicationException If the phone number is invalid
     */
    public static String validatePhoneNumber(String phoneNumber) throws ApplicationException {
        if (phoneNumber != null && PHONE_PATTERN.matcher(phoneNumber).matches()) {
            return phoneNumber;
        } else {
            throw new ApplicationException("Invalid phone number: " + phoneNumber + ". Format should be 10 digits with optional spaces or hyphens.");
        }
    }
    
    /**
     * Validates the given joined date against the format YYYY-MM-DD.
     * If the date is valid, it returns the date. Otherwise, it throws an
     * ApplicationException with an appropriate error message.
     *
     * @param joinedDate The joined date to validate
     * @return The original joined date if valid
     * @throws ApplicationException If the joined date is invalid
     */
    public static String validateJoinedDate(String joinedDate) throws ApplicationException {
        if (joinedDate != null && DATE_PATTERN.matcher(joinedDate).matches()) {
            try {
                // Parse the date to ensure it's a valid date
                LocalDate.parse(joinedDate, DATE_FORMATTER);
                return joinedDate;
            } catch (DateTimeParseException e) {
                throw new ApplicationException("Invalid date: " + joinedDate + ". Date must be a valid date.");
            }
        } else {
            throw new ApplicationException("Invalid date format: " + joinedDate + ". Format should be YYYY-MM-DD");
        }
    }
}