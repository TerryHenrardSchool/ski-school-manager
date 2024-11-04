package be.th.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import be.th.dao.DatabaseConstants;
import be.th.formatters.StringFormatter;
import be.th.validators.DateValidator;
import be.th.validators.IntegerValidator;
import be.th.validators.StringValidator;

public abstract class Person implements Serializable {
	
	// Static attributes
	private static final long serialVersionUID = -6721217695491429434L;
	
	// Static Constant attributes
	private final static String SPLIT_LAST_NAME_AND_FIRST_NAME_REGEX = "^([A-Z\\s]+)\\s+([A-Z][a-zA-Z\\s]+)$";
	
	// Constant attributes
    private final String DATE_PATTERN = "YYYY-MM-DD";
    private final LocalDate MIN_VALID_BIRTHDATE = LocalDate.of(1900, 1, 1);
    private final LocalDate MAX_VALID_BIRTHDATE = LocalDate.now();
	private final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
	private final String PHONE_NUMBER_REGEX = 
		"^(((\\+|00)32[ ]?(?:\\(0\\)[ ]?)?)|0){1}(4(60|[789]\\d)\\/?(\\s?\\d{2}\\.?){2}(\\s?\\d{2})|(\\d\\/?\\s?\\d{3}|\\d{2}\\/?\\s?\\d{2})(\\.?\\s?\\d{2}){2})$";
	
	// Attributes
    private int id;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    String phoneNumber;
    private String email;
    
    // References
    private Address address;

    // Constructor
    public Person(
		int id, 
		String lastName, 
		String firstName, 
		LocalDate dateOfBirth, 
		String city, 
		String postcode, 
		String streetName, 
		String streetNumber,
		String phoneNumber, 
		String email
	) {
        setId(id);
        setLastName(lastName);
        setFirstName(firstName);
        setDateOfBirth(dateOfBirth);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        address = new Address(city, postcode, streetName, streetNumber);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
    
    public Address getAddress() {
    	return address;
    }

    // Setters
    public void setId(int id) {
        if (!IntegerValidator.isPositiveOrEqualToZero(id)) {
            throw new IllegalArgumentException("Id must be positive or equal to 0.");
        }
        this.id = id;
    }

    public void setLastName(String lastName) {
        if (!StringValidator.hasValue(lastName)) {
            throw new IllegalArgumentException("Last name must have a value.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(lastName, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Last name's length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
        }
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        if (!StringValidator.hasValue(firstName)) {
            throw new IllegalArgumentException("First name must have a value.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(firstName, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("First name's length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
        }
        this.firstName = firstName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (!DateValidator.hasValue(dateOfBirth)) {
            throw new IllegalArgumentException("Date of birth must have a value.");
        }
        
        if (!DateValidator.isInRange(dateOfBirth, MIN_VALID_BIRTHDATE, MAX_VALID_BIRTHDATE)) {
    		throw new IllegalArgumentException("The birtdate must be between " + MIN_VALID_BIRTHDATE.toString() + " and " + MAX_VALID_BIRTHDATE.toString());
        }
        this.dateOfBirth = dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
    	if(!StringValidator.hasValue(dateOfBirth)) {
    		throw new IllegalArgumentException("The birthdate cannot be null or empty");
    	}
    	
    	if (!DateValidator.isInRange(dateOfBirth, DATE_PATTERN, MIN_VALID_BIRTHDATE, MAX_VALID_BIRTHDATE)) {
    		throw new IllegalArgumentException("The birtdate must be between " + MIN_VALID_BIRTHDATE.toString() + " and " + MAX_VALID_BIRTHDATE.toString());
    	}
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
    	this.dateOfBirth = LocalDate.parse(dateOfBirth, formatter);
    }

    public void setPhoneNumber(String phoneNumber) {
        if (!StringValidator.hasValue(phoneNumber)) {
            throw new IllegalArgumentException("Phone number must have value.");
        }
        
        if (!StringValidator.isValidUsingRegex(phoneNumber, PHONE_NUMBER_REGEX)) {
        	throw new IllegalArgumentException("The phone number format is invalid. Please enter a phone number in Belgian format.");
        }
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        if (!StringValidator.hasValue(email)) {
            throw new IllegalArgumentException("Email must have value.");
        }
        
        if (!StringValidator.isValidUsingRegex(email, EMAIL_REGEX)) {
        	throw new IllegalArgumentException("Email format is invalid. Please enter a valid email address. E.g. john.doe@example.com.");
        }
        this.email = email;
    }
    
    // Static Methods
    public static Map<String, String> splitLastNameAndFirstName(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("The fullName must have value.");
        }

        Pattern pattern = Pattern.compile(SPLIT_LAST_NAME_AND_FIRST_NAME_REGEX);
        Matcher matcher = pattern.matcher(fullName);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("The format of the full name must be 'LASTNAME Firstname'");
        }

        String lastName = matcher.group(1).trim();
        String firstName = matcher.group(2).trim();

        Map<String, String> nameParts = new HashMap<>();
        nameParts.put("lastName", lastName);
        nameParts.put("firstName", firstName);

        return nameParts;
    }
    
    // Methods    
    public String getFirstNameFormattedForDisplay() {
		return StringFormatter.firstToUpper(firstName);	
    }
    
    public String getLastnameFormattedForDisplay() {
		return lastName.toUpperCase();
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

        Person person = (Person) object;
        return id == person.id &&
            Objects.equals(lastName, person.lastName) &&
            Objects.equals(firstName, person.firstName) &&
            Objects.equals(dateOfBirth, person.dateOfBirth) &&
            Objects.equals(phoneNumber, person.phoneNumber) &&
            Objects.equals(email, person.email) &&
            address.equals(person.address);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(
    		id, 
    		lastName, 
    		firstName, 
    		dateOfBirth, 
    		phoneNumber, 
    		email,
    		address.hashCode()
		);
    }
    
    @Override
    public String toString() {
        return "Person: " +
	        "id=" + id +
	        ", lastName='" + lastName + '\'' +
	        ", firstName='" + firstName + '\'' +
	        ", dateOfBirth=" + dateOfBirth +
	        ", phoneNumber='" + phoneNumber + '\'' +
	        ", email='" + email + '\'' + 
	        ", " + address.toString();
    }
}
