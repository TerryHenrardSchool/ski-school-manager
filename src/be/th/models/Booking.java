package be.th.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import be.th.validators.IntegerValidator;
import be.th.validators.BooleanValidator;
import be.th.validators.DateValidator;

public class Booking implements Serializable {
    
    // Static attributes
    private static final long serialVersionUID = 6020444951685458127L;
    
    // Attributes
    private int id;
    private LocalDateTime bookingDate;
    private Boolean isInsured;

    // Constructor
    public Booking(int id, LocalDateTime bookingDate, boolean isInsured) {
        setId(id);
        setBookingDate(bookingDate);
        setInsured(isInsured);
    }

    // Getters
    public int getId() {
        return id;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public boolean isInsured() {
        return isInsured;
    }

    // Setters
    public void setId(int id) {
        if (!IntegerValidator.isPositiveOrEqualToZero(id)) {
            throw new IllegalArgumentException("ID must be positive or equals to 0.");
        }
        this.id = id;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
    	if (!DateValidator.hasValue(bookingDate)) {
            throw new IllegalArgumentException("Booking date must have a value.");
        }
    	
        if (!DateValidator.isInFuture(bookingDate)) {
            throw new IllegalArgumentException("Booking date must be set to a future date. Past dates or today's date are not allowed.");
        }
        this.bookingDate = bookingDate;
    }

    public void setInsured(Boolean isInsured) {
        if (!BooleanValidator.hasValue(isInsured)) {
            throw new IllegalArgumentException("Insurance status must be specified as either true or false.");
        }
        this.isInsured = isInsured;
    }

    // Methods
    public double calculatePrice() {
        return 0.0; // TODO: Implement price calculation logic
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

        Booking booking = (Booking) object;
        return id == booking.id &&
            isInsured == booking.isInsured &&
            Objects.equals(bookingDate, booking.bookingDate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, isInsured, bookingDate);
    }
    
    @Override
    public String toString() {
        return "Booking: " +
            "id=" + id +
            ", insured=" + isInsured +
            ", booking date=" + bookingDate;
    }
}
