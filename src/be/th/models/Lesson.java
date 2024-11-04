package be.th.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import be.th.validators.IntegerValidator;
import be.th.dao.DatabaseConstants;
import be.th.validators.DateValidator;
import be.th.validators.StringValidator;

public abstract class Lesson implements Serializable {

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
        if (!IntegerValidator.isPositiveOrEqualToZero(id)) {
            throw new IllegalArgumentException("ID must be a positive or equal to 0. Negative values are not allowed.");
        }
        this.id = id;
    }

    public void setBookingRange(int minBookings, int maxBookings) {
        if (!IntegerValidator.isPositiveOrEqualToZero(minBookings)) {
            throw new IllegalArgumentException("Invalid booking range: minBookings must be positive or equal to 0.");
        }
        
        if (!IntegerValidator.isPositiveOrEqualToZero(maxBookings)) {
            throw new IllegalArgumentException("Invalid booking range: maxBookings must be positive or equal to 0.");
        }
        
        if (!IntegerValidator.isSmallerOrEqual(minBookings, maxBookings)) {
            throw new IllegalArgumentException("Invalid booking range: minBookings must be smaller or equal to maxBookings.");
        }

        this.minBookings = minBookings;
        this.maxBookings = maxBookings;
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

    public void setLocation(String location) {
        if (!StringValidator.hasValue(location)) {
            throw new IllegalArgumentException("Location must be a non-empty string. Null or empty values are not allowed.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(location, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Location's length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
        }
        this.location = location;
    }

    public void setDuration(int duration) {
        if (!IntegerValidator.isPositiveOrEqualToZero(duration)) {
            throw new IllegalArgumentException("Duration must be a positive integer. Zero or negative values are not allowed.");
        }
        this.duration = duration;
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
            minBookings == lesson.minBookings &&
            maxBookings == lesson.maxBookings &&
            duration == lesson.duration &&
            Objects.equals(date, lesson.date) &&
            Objects.equals(location, lesson.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, minBookings, maxBookings, duration, date, location);
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
