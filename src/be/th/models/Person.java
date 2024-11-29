package be.th.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

import be.th.dao.DatabaseConstant;
import be.th.formatters.DatabaseFormatter;
import be.th.formatters.StringFormatter;
import be.th.validators.DateValidator;
import be.th.validators.IntegerValidator;
import be.th.validators.StringValidator;

public abstract class Person {
	
	// Static constant attributes
    private final static String DATE_PATTERN = "YYYY-MM-DD";
    
    private final static LocalDate MIN_VALID_BIRTHDATE = LocalDate.of(1900, 1, 1);
    private final static LocalDate MAX_VALID_BIRTHDATE = LocalDate.now();
    
    private final static int MIN_AGE = 4;
    
	private final static String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
	private final static String NAMES_REGEX = "^[a-zA-ZÀ-ÖØ-öø-ÿ\\s'\\-,.]{2,}$";
	private final static String PHONE_NUMBER_REGEX = 
		"^(((\\+|00)32[ ]?(?:\\(0\\)[ ]?)?)|0){1}(4(60|[789]\\d)\\/?(\\s?\\d{2}\\.?){2}" +  
		"(\\s?\\d{2})|(\\d\\/?\\s?\\d{3}|\\d{2}\\/?\\s?\\d{2})(\\.?\\s?\\d{2}){2})$";
	
	// Attributes
    private int id;
    private String lastName;
    private String firstName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    
    // References 
    private final Address address; // Composition

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
	) 
		throws IllegalArgumentException 
    {
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
        
        if(!StringValidator.isLengthSmallerOrEqual(lastName, DatabaseConstant.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Last name's length must be smaller than " + DatabaseConstant.MAX_CHARACTERS);
        }
        
		if (!StringValidator.isValidUsingRegex(lastName, NAMES_REGEX)) {
			throw new IllegalArgumentException(""
				+ "The last name format is invalid. Please enter a valid last name. "
				+ "Special characters and numbers aren't allowed."
			);
		}
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        if (!StringValidator.hasValue(firstName)) {
            throw new IllegalArgumentException("First name must have a value.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(firstName, DatabaseConstant.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("First name's length must be smaller than " + DatabaseConstant.MAX_CHARACTERS);
        }
        
        if (!StringValidator.isValidUsingRegex(lastName, NAMES_REGEX)) {
			throw new IllegalArgumentException(""
				+ "The last name format is invalid. Please enter a valid last name. "
				+ "Special characters and numbers aren't allowed."
			);
		}
        this.firstName = firstName;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (!DateValidator.hasValue(dateOfBirth)) {
            throw new IllegalArgumentException("Date of birth must have a value.");
        }
        
        if (!DateValidator.isInRange(dateOfBirth, MIN_VALID_BIRTHDATE, MAX_VALID_BIRTHDATE)) {
        	throw new IllegalArgumentException(""
				+ "The birthdate must be between " + DatabaseFormatter.toBelgianFormat(MIN_VALID_BIRTHDATE) 
				+ " and " + DatabaseFormatter.toBelgianFormat(MAX_VALID_BIRTHDATE)
    		);        
        }
        
		if (!hasRequiredMinimumAge(dateOfBirth)) {
			throw new IllegalArgumentException("The person must be at least " + MIN_AGE + " years old.");
		}
        this.dateOfBirth = dateOfBirth;
    }
    
    public void setDateOfBirth(String dateOfBirth) {
    	if(!StringValidator.hasValue(dateOfBirth)) {
    		throw new IllegalArgumentException("The birthdate cannot be null or empty");
    	}
    	
    	if (!DateValidator.isInRange(dateOfBirth, DATE_PATTERN, MIN_VALID_BIRTHDATE, MAX_VALID_BIRTHDATE)) {
    		throw new IllegalArgumentException(""
				+ "The birthdate must be between " + DatabaseFormatter.toBelgianFormat(MIN_VALID_BIRTHDATE) 
				+ " and " + DatabaseFormatter.toBelgianFormat(MAX_VALID_BIRTHDATE)
    		);
    	}
    	
    	LocalDate dateOfBirthParsed = null;
    	try {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
    		dateOfBirthParsed = LocalDate.parse(dateOfBirth, formatter);    		
    	} catch (DateTimeParseException ex) {
    		throw new IllegalArgumentException("The birthdate format is invalid. Please enter a valid date in the format " + DATE_PATTERN);
    	}
    	
    	if (!hasRequiredMinimumAge(dateOfBirthParsed)) {
			throw new IllegalArgumentException("The person must be at least " + MIN_AGE + " years old.");
		}
    	
    	this.dateOfBirth = dateOfBirthParsed;
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
    
    //Private methods
    private String getFirstNameFormattedForDisplay() {
    	return StringFormatter.firstToUpper(firstName);	
    }
    
    private String getLastnameFormattedForDisplay() {
    	return lastName.toUpperCase();
    }
    
    private boolean hasRequiredMinimumAge(LocalDate birthDate) {
    	LocalDate today = LocalDate.now();
    	java.time.Period age = java.time.Period.between(birthDate, today);
    	
    	return age.getYears() >= MIN_AGE;
    }
    
    // Public methods
	public String getFullNameFormattedForDisplay() {
		return getLastnameFormattedForDisplay() + " " + getFirstNameFormattedForDisplay();
	}
	
	public int calculateAge() {
		return LocalDate.now().getYear() - getDateOfBirth().getYear();
	}
	
	public String getCalculatedAgeFormattedForDisplay() {
		int age = calculateAge();
		return age + (age > 1 ? " years" : " year");
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
	        ", address=" + address;
    }
}
