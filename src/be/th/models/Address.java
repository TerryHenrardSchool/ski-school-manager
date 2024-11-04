package be.th.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.th.dao.DatabaseConstants;
import be.th.formatters.StringFormatter;
import be.th.validators.StringValidator;

public class Address {
	
	// Static constant attributes
	private final static String DESTRUCTUR_FORMATTED_ADDRESS_REGEX = "^(.+?)\\s+(\\d+),\\s+(.+)\\s+(\\d{4})$";
	
	// Constant attributes
	private final String STREET_NUMBER_REGEX = "^[0-9]{1,4}";
	private final String POSTCODE_REGEX = "^[0-9]{4}$";
	
	// Attributes
	private String city;
    private String postcode;
    private String streetName;
    private String streetNumber;
	
    // Constructors
    public Address(String city, String postcode, String streetName, String streetNumber) {
    	setCity(city);
    	setPostcode(postcode);
    	setStreetName(streetName);
    	setStreetNumber(streetNumber);
    }
    
    public Address(String address) {
        this(destructureFormattedAddress(address));
    }

    private Address(Map<String, String> addressMap) {
        this(
            addressMap.get("city"),
            addressMap.get("postcode"),
            addressMap.get("streetName"),
            addressMap.get("streetNumber")
        );
    }


    // Getters
	public String getCity() {
		return city;
	}

	public String getPostcode() {
		return postcode;
	}

	public String getStreetName() {
		return streetName;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	// Setters
	public void setCity(String city) {
        if (!StringValidator.hasValue(city)) {
            throw new IllegalArgumentException("City must have a value.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(city, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("City's length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
        }
        this.city = city;
    }

    public void setPostcode(String postcode) {
        if (!StringValidator.hasValue(postcode)) {
            throw new IllegalArgumentException("Postcode must have a value.");
        }
        
        if(!StringValidator.isValidUsingRegex(postcode, POSTCODE_REGEX)) {
        	throw new IllegalArgumentException("Postcode must be only 4 numbers.");
        }
        this.postcode = postcode;
    }

    public void setStreetName(String streetName) {
        if (!StringValidator.hasValue(streetName)) {
            throw new IllegalArgumentException("Street name must have value");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(streetName, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Street name's length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
        }
        this.streetName = streetName;
    }

    public void setStreetNumber(String streetNumber) {
        if (!StringValidator.hasValue(streetNumber)) {
            throw new IllegalArgumentException("Street number must have a value.");
        }
        
        if (!StringValidator.isValidUsingRegex(streetNumber, STREET_NUMBER_REGEX)) {
        	throw new IllegalArgumentException("The street number format is invalid. It must be at least 1 and maximum 4 numbers long.");
        }
        this.streetNumber = streetNumber;
    }
    
    // Static methods
    public static Map<String, String> destructureFormattedAddress(String address) {
        Pattern pattern = Pattern.compile(DESTRUCTUR_FORMATTED_ADDRESS_REGEX);
        Matcher matcher = pattern.matcher(address);
        
        Map<String, String> addressComponents = new HashMap<>();
        
        if (matcher.matches()) {
            addressComponents.put("streetName", matcher.group(1).trim());
            addressComponents.put("streetNumber", matcher.group(2).trim());
            addressComponents.put("city", matcher.group(3).trim());
            addressComponents.put("postcode", matcher.group(4).trim());
        } else {
            throw new IllegalArgumentException("Address does not match the expected format. E.g. 'Square Jules Hiérnaux 5, 6000 Charleroi'.");
        }
        
        return addressComponents;
    }

    
    // Methods
    public String getAddressFormattedForDisplay() {
    	return StringFormatter.firstToUpper(streetName) + " " + streetNumber + ", " + StringFormatter.firstToUpper(city) + " " + postcode;
    }
    
    // Override methods
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        
        Address address = (Address) object;
        return Objects.equals(city, address.city) &&
               Objects.equals(postcode, address.postcode) &&
               Objects.equals(streetName, address.streetName) &&
               Objects.equals(streetNumber, address.streetNumber);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(
    		city, 
    		postcode, 
    		streetName, 
    		streetNumber
		);
    }
    
    @Override
    public String toString() {
        return "Address: " +
           "city='" + city + '\'' +
           ", postcode='" + postcode + '\'' +
           ", streetName='" + streetName + '\'' +
           ", streetNumber='" + streetNumber + '\'';
    }
}
