package be.th.ski_school_manager;

import java.time.LocalDate;
import java.util.Objects;

public class Secretary extends Person {
	
	// Static attributes
	private static final long serialVersionUID = 3225181921018011916L;
	
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
        if (!Utils.isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }
        this.password = password;
    }
    
    // Method
    public boolean logIn(String email, String password) {
    	return this.getEmail().equals(email) && this.password.equals(password);
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
