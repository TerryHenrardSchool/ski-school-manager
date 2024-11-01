package be.th.classes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import javax.swing.filechooser.FileNameExtensionFilter;

public class Utils {
	
    private static final int MIN_VALID_YEAR = 1900;
    private static final String DATE_PATTERN_STRING = "YYYY-MM-DD";
	
    public static boolean isPositiveInteger(int number) {
        return number > 0;
    }
    
    public static boolean isNonNegativeInteger(int number) {
    	return number >= 0;
    }

    public static boolean isNonEmptyString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidDate(LocalDate dateObj) {
        if (dateObj == null || dateObj.isAfter(LocalDate.now())) {
            return false;
        }
        
        return dateObj.getYear() >= MIN_VALID_YEAR;
    }

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_STRING);
            LocalDate dateOfBirth = LocalDate.parse(dateStr, formatter);
            return isValidDate(dateOfBirth);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email != null && email.matches(emailRegex);
    }
    
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8;
    }
    
    public static boolean isValidBookingRange(int minBookings, int maxBookings) {
        return minBookings >= 0 && maxBookings >= minBookings;
    }
    
    public static boolean isFutureDate(LocalDateTime date) {
        return date != null && date.isAfter(LocalDateTime.now());
    }
    
    public static boolean isBooleanValid(Boolean value) {
        return value != null;
    }
    
    public static String formatStringForDataBaseStorage(String string) {
    	return string.trim().toLowerCase();
    }
    
    public static LocalDate formatDateFromJDateChooserToLocalDate(Date date) {
    	return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
