package be.th.validators;

import java.util.Optional;

public class IntegerValidator {
	public static boolean hasValue(Integer number) {
		return number != null;
	}
	
	public static boolean isPositiveOrEqualToZero(int number) {
    	return number >= 0;
    }
	
	public static boolean isSmallerOrEqual(int value, int max) {
		return value <= max;
	}
	
	public static boolean isGreaterOrEqual(int value, int min) {
		return value >= min;
	}
	
	public static boolean isGreaterOrEqual(Optional<Integer> value, int min) {
		return value.filter(_value ->_value >= min).isPresent();
	}
	
	public static boolean isGreaterOrEqual(double value, double min) {
		return value >= min;
	}
}
