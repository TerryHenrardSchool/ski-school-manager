package be.th.ski_school_manager;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Booking implements Serializable {
	
	// Static attributes
	private static final long serialVersionUID = 6020444951685458127L;
	
	// Attributes
    private int id;
    private LocalDateTime bookingDate;
    private boolean isInsured;

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
        if (!Utils.isPositiveInteger(id)) {
            throw new IllegalArgumentException("ID must be positive.");
        }
        this.id = id;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        if (!Utils.isFutureDate(bookingDate)) {
            throw new IllegalArgumentException("Booking date must be in the future.");
        }
        this.bookingDate = bookingDate;
    }

    public void setInsured(Boolean isInsured) {
        if (!Utils.isBooleanValid(isInsured)) {
            throw new IllegalArgumentException("Insurance status must be specified.");
        }
        this.isInsured = isInsured;
    }

    // Methods
    public double calculatePrice() {
        return 0.0; // TODO
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
        return "Lesson: " +
	        "id=" + id +
	        ", min bookings='" + isInsured + '\'' +
	        ", max bookings='" + bookingDate + '\'';
    }
}
