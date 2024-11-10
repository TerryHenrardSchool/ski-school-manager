package be.th.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.th.dao.InstructorDAO;
import be.th.validators.IntegerValidator;
import be.th.validators.ObjectValidator;

public class Instructor extends Person {
	
	// Static attributes
	private static final long serialVersionUID = -5724827101822519690L;
	
	// References
	private Set<Accreditation> accreditations;

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
		String email,
		Set<Accreditation> accreditations
	) {
        super(id, lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email);
        
        this.accreditations = new HashSet<Accreditation>();
        setAccreditations(accreditations);
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
    		String email,
    		Set<Accreditation> accreditations
    		) {
    	this(0, lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email, accreditations);
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
		String email,
		Accreditation accreditation
	) {
        this(0, lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email, Set.of(accreditation));
    }
    
    // Getters
    public Set<Accreditation> getAccreditations(){
        return Collections.unmodifiableSet(accreditations);
    }
    
    // Setters
    public void setAccreditations(Set<Accreditation> accreditations) {
    	if(!ObjectValidator.hasValue(accreditations)) {
    		throw new IllegalArgumentException("Accreditations must be set.");
    	}
    	
    	if(!IntegerValidator.isGreaterOrEqual(accreditations.size(), 1)) {
        	throw new IllegalArgumentException("Accreditations must have at least one value.");
        }
    	
    	for(Accreditation accreditation: accreditations) {
        	addAccreditation(accreditation);
        }
    }

    // Methods
    public boolean removeAccreditation(Accreditation accreditation) {
        if (!ObjectValidator.hasValue(accreditation)) {
            throw new IllegalArgumentException("Accreditation must have value");
        }
        return accreditations.remove(accreditation);
    }

    public boolean addAccreditation(Accreditation accreditation) {
    	if (!ObjectValidator.hasValue(accreditation)) {
    		throw new IllegalArgumentException("Accreditation must have value");
    	}
    	
    	return accreditations.add(accreditation); 
    }
    
    public boolean isAccreditate(Accreditation accreditationToCheck) {
        return accreditations.stream().anyMatch(accreditation -> accreditation.equals(accreditationToCheck));
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
    
    public boolean updateInDatabase(InstructorDAO instructorDAO) {
    	return instructorDAO.update(this);
    }
    
    public boolean deleteFromDatabase(InstructorDAO instructorDAO) {
    	return instructorDAO.delete(getId());
    }
    
    public static List<Instructor> findAllInDatabase(InstructorDAO instructorDAO){
    	return instructorDAO.findAll();
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
    	String result = "";
    	
    	result += super.toString() + " ";
    	
    	result += "List of accreditations:";
    	for(Accreditation accreditation: accreditations) {
    		result += accreditation;
    	}
    	
    	return result;
    }
}
