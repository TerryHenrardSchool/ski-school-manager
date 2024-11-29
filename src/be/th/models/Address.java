package be.th.models;

import java.util.Objects;

import be.th.dao.DatabaseConstant;
import be.th.formatters.StringFormatter;
import be.th.validators.StringValidator;

public class Address {
	
	// Static constant attributes
	private final static String STREET_NUMBER_REGEX = "^[0-9]{1,4}";
	private final static String POSTCODE_REGEX = "^[0-9]{4}$";
	
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
        
        if(!StringValidator.isLengthSmallerOrEqual(city, DatabaseConstant.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("City's length must be smaller than " + DatabaseConstant.MAX_CHARACTERS);
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
        
        if(!StringValidator.isLengthSmallerOrEqual(streetName, DatabaseConstant.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Street name's length must be smaller than " + DatabaseConstant.MAX_CHARACTERS);
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
