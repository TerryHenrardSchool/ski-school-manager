package be.th.models;

import java.time.LocalDate;
import java.util.ArrayList;

import be.th.dao.InstructorDAO;

public class Instructor extends Person {
	
	// Static attributes
	private static final long serialVersionUID = -5724827101822519690L;

	// Constructors
    public Instructor(
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
        super(
    		id, 
    		lastName, 
    		firstName, 
    		dateOfBirth, 
    		city, 
    		postcode, 
    		streetName, 
    		streetNumber, 
    		phoneNumber, 
    		email
		);
    }
    
    public Instructor(
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
        this(
    		0, 
    		lastName, 
    		firstName,
    		dateOfBirth, 
    		city,
    		postcode,
    		streetName,
    		streetNumber,
    		phoneNumber, 
    		email
		);
    }

    // Methods
    public boolean isAccreditate() {
        return false;
    }

    public boolean hasScheduledLesson() {
        return false;
    }

    public ArrayList<Lesson> getSchedule(int instructorId) {
        return new ArrayList<>(); //TODO
    }
    
    // Database methods
    public boolean insertIntoDatabase(InstructorDAO instructorDAO) {
    	return instructorDAO.create(this);
    }
    
    //Override methods
    @Override
    public boolean equals(Object object) {
    	if(!(object instanceof Person)) {
    		return false;
    	}
    	
    	return super.equals((Person) object);
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
