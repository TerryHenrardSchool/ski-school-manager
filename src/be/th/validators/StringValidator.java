package be.th.validators;

public class StringValidator {
	public static boolean hasValue(String string) {
        return string != null && !string.trim().isEmpty();
    }
	
	public static boolean isValidUsingRegex(String string, String regex) {
    	return string.trim().matches(regex);
    }
	
	public static boolean isLengthSmallerOrEqual(String string, int max) {
		return string.length() <= max;
	}
}
