package a01183994.database.util;

public class Formatter {
	private static final int PHONE_NUMBER_LENGTH = 10;
	private static final int AREA_CODE_LENGTH = 3;
	private static final int EXCHANGE_CODE_LENGTH = 3;
	private static final int SUBSCRIBER_NUMBER_LENGTH = 4;
	private static final String PHONE_FORMAT = "%s-%s-%s";
	private static final String DIGIT_REGEX = "\\D";
	private static final String DIGIT_REPLACEMENT = "";

	public static String formatPhone(String phoneNumber) {
		// Remove all non-digit characters
		String digitsOnly = phoneNumber.replaceAll(DIGIT_REGEX, DIGIT_REPLACEMENT);

		// Ensure we have exactly 10 digits
		if (digitsOnly.length() != PHONE_NUMBER_LENGTH) {
			return phoneNumber; // Return original if not 10 digits
		}

		// Format as xxx-xxx-xxxx
		return String.format(PHONE_FORMAT, digitsOnly.substring(0, AREA_CODE_LENGTH),
				digitsOnly.substring(AREA_CODE_LENGTH, AREA_CODE_LENGTH + EXCHANGE_CODE_LENGTH),
				digitsOnly.substring(AREA_CODE_LENGTH + EXCHANGE_CODE_LENGTH, PHONE_NUMBER_LENGTH));
	}

}
