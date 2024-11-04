package be.th.formatters;

public class StringFormatter {

	public static String firstToUpper(String string) {
    	return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
