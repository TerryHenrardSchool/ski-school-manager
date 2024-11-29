package be.th.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import be.th.dao.SkierDAO;
import be.th.validators.ObjectValidator;

public class Skier extends Person {
	
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

    // Public methods
    public double calculateTotalSpent() {
    	return bookings.stream().mapToDouble(Booking::calculatePrice).sum();
    }
    
    public boolean isBookingSlotFree(Booking newBooking) {
        LocalDateTime newBookingDate = newBooking.getLesson().getDate();

        return bookings
    		.stream()
    		.noneMatch(booking -> booking.getLesson().getDate().equals(newBookingDate));
    }

    
    public boolean isFullyBookedDay(LocalDateTime lessonDateTime) {
        LocalDate lessonDate = lessonDateTime.toLocalDate();
        int lessonHour = lessonDateTime.getHour();

        return bookings
    		.stream()
    		.anyMatch(booking -> {
				LocalDateTime bookingDateTime = booking.getLesson().getDate();
				LocalDate bookingDate = bookingDateTime.toLocalDate();
				int bookingHour = bookingDateTime.getHour();
							
				return bookingDate.equals(lessonDate) && bookingHour != lessonHour;
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
        return lessonType.isValidAge(age);
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

        return bookings.add(newBooking);
    }

	public boolean removeBooking(Booking booking) {
		if (!ObjectValidator.hasValue(booking)) {
			throw new IllegalArgumentException("Booking must have value.");
		}
		
		return bookings.remove(booking);
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
