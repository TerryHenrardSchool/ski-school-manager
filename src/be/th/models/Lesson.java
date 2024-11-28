package be.th.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntToDoubleFunction;

import be.th.validators.IntegerValidator;
import be.th.validators.ObjectValidator;
import be.th.dao.DatabaseConstant;
import be.th.dao.LessonDAO;
import be.th.validators.DateValidator;
import be.th.validators.StringValidator;

public class Lesson implements Serializable {

    // Static attributes
    private static final long serialVersionUID = 5815631340419350701L;

    // Attributes
    private int id;
    private LocalDateTime startDate;
    
    // References
    Location location;
    LessonType lessonType;
    Instructor instructor;
    // Secretary secretary;
    Set<Booking> bookings;
    
    // Constructor
    public Lesson(
		int id, 
		LocalDateTime startDate, 
		LessonType lessonType, 
		Instructor instructor, 
		int locationId, 
		String LocationName
	) {
        setId(id);
        setStartDate(startDate);
        setLessonType(lessonType);
        setInstructor(instructor);
        location = new Location(locationId, LocationName);
        bookings = new LinkedHashSet<>();
    }
    
    public Lesson(
		LocalDateTime startDate, 
		LessonType lessonType, 
		Instructor instructor, 
		int locationId, 
		String LocationName
	) {
    	this(0, startDate, lessonType, instructor, locationId, LocationName);
    }

    // Getters
    public int getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return startDate;
    }
    
    public Location getLocation() {
    	return location;
    }
    
    public LessonType getLessonType() {
		return lessonType;
	}

	public Instructor getInstructor() {
		return instructor;
	}
	
	public Set<Booking> getBookings() {
        return Collections.unmodifiableSet(bookings);
    }

	// Setters
    public void setId(int id) {
        if (!IntegerValidator.isPositiveOrEqualToZero(id)) {
            throw new IllegalArgumentException("ID must be a positive or equal to 0. Negative values are not allowed.");
        }
        this.id = id;
    }

    public void setStartDate(LocalDateTime date) {
    	if (!DateValidator.hasValue(date)) {
            throw new IllegalArgumentException("Date must have a value.");
        }
    	
		if (!DateValidator.isInRange(date.toLocalDate(), Period.SKI_SCHOOL_OPENING_DATE, Period.SKI_SCHOOL_CLOSING_DATE)) {
			throw new IllegalArgumentException("Date must be within the ski school's opening dates.");
    		
    	}
        this.startDate = date;
    }

	public void setLessonType(LessonType lessonType) {
		if (!ObjectValidator.hasValue(lessonType)) {
    		throw new IllegalArgumentException("Lesson type must have value.");    		
    	}
		this.lessonType = lessonType;
	}

	public void setInstructor(Instructor instructor) {
		if (!ObjectValidator.hasValue(instructor)) {
    		throw new IllegalArgumentException("Instructor must have value.");    		
    	}
		this.instructor = instructor;
	}
	
	// Database methods
	public boolean insertIntoDatabase(LessonDAO lessonDAO) {
		return lessonDAO.create(this);
	}
	
	public boolean deleteFromDatabase(LessonDAO lessonDAO) {
		return lessonDAO.delete(id);
	}
	
	public static List<Lesson> findAllInDatabase(LessonDAO lessonDAO) {
		return lessonDAO.findAll();
	}
	
	public static List<Lesson> findAllAfterDateInDatabase(LocalDate date, LessonDAO lessonDAO) {
		return lessonDAO.findAll(date);
	}

	public boolean isBookingWithinAllowedTimeframe(Booking booking) {
	    if (!getLessonType().getIsPrivate()) {
	        return true;
	    }

	    if (booking.getPeriod().getIsVacation()) {
	        return booking.getBookingDate().isAfter(booking.getLesson().getDate().minusWeeks(1));
	    } else {
	        return booking.getBookingDate().isAfter(booking.getLesson().getDate().minusMonths(1));
	    }
	}
	
	public boolean addBooking(Booking booking) {
		if (!ObjectValidator.hasValue(booking)) {
			throw new IllegalArgumentException("Booking must have value.");
		}
		
		if (!isBookingWithinAllowedTimeframe(booking)) { 
		    throw new IllegalArgumentException(
		        "It's too early to book for this private lesson. "
		        + "Private lessons during school vacation must be booked at least 1 week in advance and "
		        + "private lessons during non-school vacation must be booked at least 1 month in advance."
		    );
		}
		
		if (bookings.contains(booking)) {
			return false;
		}
		
		if (!isAvailable()) {
			throw new IllegalArgumentException("Lesson is fully booked.");
		}
		
		if (!booking.getSkier().hasValidAgeForLessonType(lessonType)) {
			throw new IllegalArgumentException("Skier does not meet the age requirements for the lesson type.");
		}
		
		return bookings.add(booking);
	}
	
	public boolean removeBooking(Booking booking) {
		if (!ObjectValidator.hasValue(booking)) {
			throw new IllegalArgumentException("Booking must have value.");
		}

		if (!bookings.contains(booking)) {
			throw new IllegalArgumentException("Booking does not exist.");
		}
		return bookings.remove(booking);
	}
	
    public double calculatePrice() {
        return bookings.stream().mapToDouble(Booking::calculatePrice).sum();
    }

    public boolean hasBooking() {
        return !bookings.isEmpty();
    }
    
	public int getBookingCount() {
		return bookings.size();
	}
	
	public int getRemainingBookingsCount() {
		return lessonType.getMaxBookings() - bookings.size();
	}

	public boolean isFullyBooked() {
		return bookings.size() >= lessonType.getMaxBookings();
	}

	public boolean isAvailable() {
		return bookings.size() < lessonType.getMaxBookings();
	}
	
	public long calculateDaysUntilStartDate() {
        return Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(), startDate));
    }

	public String getCalculatedDaysUntilStartDateFormattedForDisplay() {
	    long days = calculateDaysUntilStartDate();

	    long years = days / 365; 
	    long remainingDays = days % 365;

	    long months = remainingDays / 30;
	    remainingDays %= 30;

	    String result = "";
	    if (years > 0) {
	        result += years + " years ";
	    }
	    if (months > 0) {
	        result += months + " months ";
	    }
	    if (remainingDays > 0 || result.length() == 0) {
	        result += remainingDays + " days";
	    }

	    return result.toString().trim();
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

        Lesson lesson = (Lesson) object;
        return id == lesson.id &&
            Objects.equals(startDate, lesson.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate);
    }

    @Override
    public String toString() {
        return "Lesson:" +
           "id=" + id +
           ", date=" + startDate + '\'' + 
           ", " + location + '\'' +
           ", " + lessonType + '\'' + 
           ", " + bookings.toString() + '\'' +
           ", " + instructor.getFullNameFormattedForDisplay();  
       }
}
