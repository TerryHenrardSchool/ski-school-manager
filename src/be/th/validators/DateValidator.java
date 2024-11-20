package be.th.validators;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateValidator {
	public static boolean hasValue(Date date) {
		return date != null;
	}
	
	public static boolean hasValue(LocalDate date) {
        return date != null;
    }
	
	public static boolean hasValue(LocalDateTime date) {
		return date != null;
	}
	
	public static boolean isInRange(LocalDate date, LocalDate min, LocalDate max) {	    
	    return (date.isEqual(min) || date.isAfter(min)) && (date.isEqual(max) || date.isBefore(max));
	}
	
	public static boolean isInRange(String date, String pattern, LocalDate min, LocalDate max) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate localDate = LocalDate.parse(date, formatter);
        return isInRange(localDate, min, max);
    }
	
	public static boolean isInFuture(LocalDateTime date) {
        return date.isAfter(LocalDateTime.now());
    }
	
	public static boolean isInPast(LocalDateTime date) {
		return date.isBefore(LocalDateTime.now());
	}
}
