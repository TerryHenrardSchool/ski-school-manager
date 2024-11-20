package be.th.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

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
    private LocalDateTime date;
    
    // References
    Location location;
    LessonType lessonType;
    Instructor instructor;
    // Secretary secretary;
    // Set<Booking> bookings;
    
    // Constructor
    public Lesson(int id, LocalDateTime startDate, LessonType lessonType, Instructor instructor, int locationId, String LocationName) {
        setId(id);
        setDate(startDate);
        setLessonType(lessonType);
        setInstructor(instructor);
        location = new Location(locationId, LocationName);
    }
    
    public Lesson(LocalDateTime startDate, LessonType lessonType, Instructor instructor, int locationId, String LocationName) {
    	this(0, startDate, lessonType, instructor, locationId, LocationName);
    }

    // Getters
    public int getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
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

	// Setters
    public void setId(int id) {
        if (!IntegerValidator.isPositiveOrEqualToZero(id)) {
            throw new IllegalArgumentException("ID must be a positive or equal to 0. Negative values are not allowed.");
        }
        this.id = id;
    }

    public void setDate(LocalDateTime date) {
    	if (!DateValidator.hasValue(date)) {
            throw new IllegalArgumentException("Date must have a value.");
        }
    	
        if (!DateValidator.isInFuture(date)) {
            throw new IllegalArgumentException("Date must be set to a future date. Past dates or today's date are not allowed.");
        }
        this.date = date;
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

    // Methods
    public double calculatePrice() {
        return 0.0; // TODO: Implement price calculation logic
    }

    public boolean hasBooking() {
        return false; // TODO: Implement booking check logic
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
            Objects.equals(date, lesson.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date);
    }

    @Override
    public String toString() {
        return "Lesson:" +
           "id=" + id +
           ", date=" + date + '\'' + 
           ", " + location + '\'' +
           ", " + lessonType + '\'';  
       }
}
