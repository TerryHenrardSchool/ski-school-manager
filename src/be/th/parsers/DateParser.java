package be.th.parsers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

import be.th.validators.DateValidator;
import be.th.validators.StringValidator;

public class DateParser {
    private static final String BELGIAN_DATE_PATTERN = "dd/MM/yyyy";
    private static final DateTimeFormatter BELGIAN_DATE_FORMATTER = DateTimeFormatter.ofPattern(BELGIAN_DATE_PATTERN);


	public static LocalDate toLocalDate(Date date) {
		return DateValidator.hasValue(date) 
			? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() 
			: null;
    }
	
    public static LocalDate toLocalDate(String date) throws Exception, IllegalArgumentException {  
    	if(!StringValidator.hasValue(date)) {
    		throw new IllegalArgumentException();
    	}
    	
        try {
            return LocalDate.parse(date, BELGIAN_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new Exception(e);
        }
    }
    
    public static Date toDate(LocalDate localDate) {
        if (!DateValidator.hasValue(localDate)) {
    		throw new IllegalArgumentException();
        }
        
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
