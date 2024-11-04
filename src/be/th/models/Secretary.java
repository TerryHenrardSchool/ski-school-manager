package be.th.models;

import java.time.LocalDate;
import java.util.Objects;

import be.th.validators.StringValidator;

public class Secretary extends Person {
	
	// Static attributes
	private static final long serialVersionUID = 3225181921018011916L;
	
	// Constant attributes
	private final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
	private final int MIN_PASSWORD_LENGTH = 8;

	// Attributes
    private String password;

    // Constructor
    public Secretary(
		int id, 
		String lastName, 
		String firstName, 
		LocalDate dateOfBirth,
		String city, 
		String postcode, 
		String streetName, 
		String streetNumber,
		String phoneNumber, 
		String email, 
		String password
	) {
        super(id, lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email);
        setPassword(password);
    }

    // Getter
    public String getPassword() {
        return password;
    }

    // Setter
    public void setPassword(String password) {
    	if (!StringValidator.isLengthSmallerOrEqual(password, MIN_PASSWORD_LENGTH)) {
    		throw new IllegalArgumentException("Password must be at least 8 characters long.");
    	}
    	
        if (!StringValidator.isValidUsingRegex(password, PASSWORD_REGEX)) {
            throw new IllegalArgumentException("Password must contains at least 1 letter and 1 number.");
        }
        this.password = password;
    }
    
    // Method
    public boolean logIn(String email, String password) {
    	return getEmail().equals(email) && getPassword().equals(password);
    }
    
  //Override methods
    @Override
    public boolean equals(Object object) {
    	if(!(object instanceof Person)) {
    		return false;
    	}
    	
        if (!super.equals((Person) object)) {
            return false;
        }
        
        Secretary secretary = (Secretary) object;
        return Objects.equals(password, secretary.password);
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(super.hashCode() + password);
    }
    
    @Override
    public String toString() {
    	return super.toString() + ", password='" + password + '\'';
    }
}
