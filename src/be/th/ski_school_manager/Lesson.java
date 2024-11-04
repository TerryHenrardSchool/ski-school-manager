package be.th.ski_school_manager;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Lesson implements Serializable{
	
	// Static attributes
	private static final long serialVersionUID = 5815631340419350701L;
	
	// Attributes
    private int id;
    private int minBookings;
    private int maxBookings;
    private LocalDateTime date;
    private String location;
    private int duration;

    // Constructor
    public Lesson(int id, int minBookings, int maxBookings, LocalDateTime date, String location, int duration) {
        setId(id);
        setBookingRange(minBookings, maxBookings);
        setDate(date);
        setLocation(location);
        setDuration(duration);
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getMinBookings() {
        return minBookings;
    }

    public int getMaxBookings() {
        return maxBookings;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public int getDuration() {
        return duration;
    }

    // Setters
    public void setId(int id) {
        if (!Utils.isPositiveInteger(id)) {
            throw new IllegalArgumentException("ID must be positive.");
        }
        this.id = id;
    }

    // Combined setter to ensure minBookingd is always smaller than mawBookings and vice versa
    public void setBookingRange(int minBookings, int maxBookings) {
        if (!Utils.isValidBookingRange(minBookings, maxBookings)) {
            throw new IllegalArgumentException("Invalid booking range: minBookings must be non-negative and smaller or equals to maxBookings.");
        }
        this.minBookings = minBookings;
        this.maxBookings = maxBookings;
    }

    public void setDate(LocalDateTime date) {
        if (!Utils.isFutureDate(date)) {
            throw new IllegalArgumentException("Date must be in the future.");
        }
        this.date = date;
    }

    public void setLocation(String location) {
        if (!Utils.isNonEmptyString(location)) {
            throw new IllegalArgumentException("Location cannot be null or empty.");
        }
        this.location = location;
    }

    public void setDuration(int duration) {
        if (!Utils.isPositiveInteger(duration)) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
        this.duration = duration;
    }

    // Methods
    public double calculatePrice() {
        return 0.0; // TODO
    }

    public boolean hasBooking() {
        return false; // TODO
    }
    
    //Override methods
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
            minBookings == lesson.minBookings &&
            maxBookings == lesson.maxBookings && 
            duration == lesson.duration &&
            Objects.equals(date, lesson.date) &&
            Objects.equals(location, lesson.location);
    }
    
    @Override
    public int hashCode() {
    	return Objects.hash(
			id, 
			minBookings,
			maxBookings,
			duration,
			date,
			location
		);
    }
    
    @Override
    public String toString() {
        return "Lesson:" +
           "id=" + id +
           ", minBookings=" + minBookings +
           ", maxBookings=" + maxBookings +
           ", duration=" + duration +
           ", date=" + date +
           ", location='" + location + '\'';
    }

}
