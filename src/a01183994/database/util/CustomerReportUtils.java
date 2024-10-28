package a01183994.database.util;

public class CustomerReportUtils {
    private static final String EMPTY_STRING = "";
    private static final String SPACE = " ";
    private static final String ELLIPSIS = "...";

    public static class CustomerFormatter {
        public static String truncateAndReplace(String stringInput, int maxLength) {
            if (stringInput == null) return EMPTY_STRING;
            String processed = stringInput.replaceAll("\\s+", SPACE);
            return processed.length() <= maxLength ? processed : processed.substring(0, maxLength - 3) + ELLIPSIS;
        }

        public static String formatPhoneNumber(String phone) {
            if (phone == null || phone.length() != 10) {
                return phone;
            }
            return String.format("(%s) %s-%s", 
                phone.substring(0, 3),
                phone.substring(3, 6),
                phone.substring(6));
        }
    }
}