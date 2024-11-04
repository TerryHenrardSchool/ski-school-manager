package be.th.models;

import java.time.LocalDate;
import java.util.ArrayList;

public class Skier extends Person {

	// Static attributes
	private static final long serialVersionUID = -342070697036823773L;

	// Constructor
    public Skier(
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
        super(id, lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email);
    }
    
    public Skier(
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
    	this(0, lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email);
    }

    // Methods
    public boolean hasScheduledLesson() {
        return false; //TODO
    }
    
    //Override methods
    @Override
    public boolean equals(Object object) {
    	if(!(object instanceof Person)) {
    		return false;
    	}
    	
    	return super.equals(((Person) object));
    }
    
    @Override
    public int hashCode() {
    	return super.hashCode();
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}