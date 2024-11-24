package be.th.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    
	public static Skier findInDatabaseById(int id, SkierDAO skierDAO) {
		return skierDAO.find(id);
	}

    // Methods
    public double calculateTotalSpent() {
    	return bookings.stream().mapToDouble(Booking::calculatePrice).sum();
    }
    
    public boolean isBookingSlotFree(Booking newBooking) {
        LocalDateTime newBookingDate = newBooking.getLesson().getDate();
        
        return bookings.stream().noneMatch(booking -> {        	
        	return booking.getLesson().getDate().equals(newBookingDate);
        });
    }
    
    public boolean isFullyBookedDay(LocalDateTime lessonDateTime) {
    	System.out.println("booking size -> " + bookings.size());
    	System.out.println("booking -> " + bookings);
        return bookings.stream().anyMatch(skierBooking -> {
            LocalDateTime skierBookingDateTime = skierBooking.getLesson().getDate();
            LocalDate skierBookingDate = skierBookingDateTime.toLocalDate();
            LocalDate lessonDate = lessonDateTime.toLocalDate();
            
            System.out.println("----------------------------------------------");
            System.out.println("skier -> " + getFullNameFormattedForDisplay());
            System.out.println("skierBookingDateTime -> " + skierBookingDateTime);
            System.out.println("lessonDateTime -> " + lessonDateTime);
            System.out.println("----------------------------------------------");
            
            return 
            	skierBookingDate.equals(lessonDate) && 
        		skierBookingDateTime.getHour() != lessonDateTime.getHour();
        });
    }
	
	public Booking findBookingById(int bookingId) {
	    return bookings.stream()
		    .filter(booking -> booking.getId() == bookingId)
		    .findFirst()
		    .orElse(null); 
	}
    
    public boolean hasValidAgeForLessonType(LessonType lessonType) {
        int age = super.calculateAge();
        return lessonType.isAgeValid(age);
    }
    
    public boolean hasBookingForLesson(Lesson lesson) {
        return lesson.getBookings().stream().anyMatch(booking -> booking.getSkier().equals(this));
    }

    public boolean addBooking(Booking newBooking) {
        if (!ObjectValidator.hasValue(newBooking)) {
            throw new IllegalArgumentException("Booking must have value.");
        }

        if (!isBookingSlotFree(newBooking)) {
            throw new IllegalArgumentException("Skier already has a booking for this date and time.");
        }

        if (bookings.contains(newBooking)) {
            return false;
        }

        return bookings.add(newBooking);
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
    	return Objects.hash(super.hashCode());
    }
    
    @Override
    public String toString() {
    	return super.toString() + bookings;
    }
}
