package be.th.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import be.th.dao.SkierDAO;
import be.th.validators.ObjectValidator;

public class Skier extends Person {

	// Static attributes
	private static final long serialVersionUID = -342070697036823773L;
	
	// References
	private Set<Booking> bookings;

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
        bookings = new HashSet<>();
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
    
    // Getters
    public Set<Booking> getBookings() {
    	return Collections.unmodifiableSet(bookings);
    }

    // Database methods
    public boolean insertIntoDatabase(SkierDAO skierDAO) {
    	return skierDAO.create(this);
    }
    
    public boolean updateInDatabase(SkierDAO skierDAO) {
    	return skierDAO.update(this);
    }
    
    public boolean deleteFromDatabase(SkierDAO skierDAO) {
    	return skierDAO.delete(getId());
    }
    
    public static List<Skier> findAllInDatabase(SkierDAO skierDAO){
    	return skierDAO.findAll();
    }

    // Methods
	public boolean addBooking(Booking booking) {
		if (!ObjectValidator.hasValue(booking)) {
			throw new IllegalArgumentException("Booking must have value.");
        }
		
		if (bookings.contains(booking)) {
			return false;
		}
		
		return bookings.add(booking);

	}
	
	public boolean removeBooking(Booking booking) {
		if (!ObjectValidator.hasValue(booking)) {
			throw new IllegalArgumentException("Booking must have value.");
		}
		
		if (!bookings.contains(booking)) {
			return false;
		}
		
		return bookings.remove(booking);
	}
	
    public boolean hasScheduledLesson() {
        return !bookings.isEmpty();
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
    	return Objects.hash(super.hashCode(), bookings);
    }
    
    @Override
    public String toString() {
    	return super.toString() + bookings;
    }
}
