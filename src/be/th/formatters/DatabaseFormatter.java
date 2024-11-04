package be.th.formatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import be.th.parsers.DateParser;

public class DatabaseFormatter {
    private static final String DATABASE_DATE_PATTERN = "yyyy-MM-dd";
    private static final String BELGIAN_DATE_PATTERN = "dd/MM/yyyy";
    private static final DateTimeFormatter DATABASE_DATE_FORMATTER = DateTimeFormatter.ofPattern(DATABASE_DATE_PATTERN);
    private static final DateTimeFormatter BELGIAN_DATE_FORMATTER = DateTimeFormatter.ofPattern(BELGIAN_DATE_PATTERN);

    public static String format(String string) {
        return string.trim().toLowerCase().replaceAll("\\s+", " ");
    }
    
    public static String format(LocalDate date) {
        return date.format(DATABASE_DATE_FORMATTER);
    }
    
    public static String format(Date date) {
        return DateParser.toLocalDate(date).format(DATABASE_DATE_FORMATTER);    
    }
    
    public static String toBelgianFormat(LocalDate date) {
        return date.format(BELGIAN_DATE_FORMATTER);
    }
    
    public static String toBelgianFormat(Date date) {
        return DateParser.toLocalDate(date).format(BELGIAN_DATE_FORMATTER);
    }
}
