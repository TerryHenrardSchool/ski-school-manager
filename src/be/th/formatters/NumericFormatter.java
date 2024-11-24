package be.th.formatters;

public class NumericFormatter {
	public static String toCurrency(double number, char currency) {
        return String.format("%.2f" + currency, number);
    }
}
