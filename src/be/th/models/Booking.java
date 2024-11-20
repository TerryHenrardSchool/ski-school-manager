package be.th.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import be.th.validators.IntegerValidator;
import be.th.validators.ObjectValidator;
import be.th.validators.BooleanValidator;
import be.th.validators.DateValidator;

public class Booking implements Serializable {
    
    // Static attributes
    private static final long serialVersionUID = 6020444951685458127L;
    
    // Attributes
    private int id;
    private LocalDateTime bookingDate;
    private Boolean isInsured;
    
    // References
    private Period period;
    private Skier skier;

    // Constructor
    public Booking(
		int id, 
		LocalDateTime bookingDate, 
		boolean isInsured, 
		int periodId, 
		LocalDate startDate, 
		LocalDate endDate, 
		boolean isVacation, 
		String name,
		Skier skier
	) {
        setId(id);
        setBookingDate(bookingDate);
        setInsured(isInsured);
        setSkier(skier);
        this.period = new Period(periodId, startDate, endDate, isVacation, name);
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
    
	public Period getPeriod() {
		return period;
	}
	
	public Skier getSkier() {
		return skier;
	}

    // Setters
    public void setId(int id) {
        if (!IntegerValidator.isPositiveOrEqualToZero(id)) {
            throw new IllegalArgumentException("ID must be positive or equals to 0.");
        }
        this.id = id;
    }
    
    public void setSkier(Skier skier) {
		if (!ObjectValidator.hasValue(skier)) {
			throw new IllegalArgumentException("Skier must have a value.");
		}
		this.skier = skier;
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
            Objects.equals(bookingDate, booking.bookingDate) &&
            period.equals(booking.period) &&
            skier.equals(booking.skier);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, isInsured, bookingDate, period.hashCode(), skier.hashCode());
    }
    
    @Override
    public String toString() {
        return "Booking: " +
            "id=" + id +
            ", insured=" + isInsured +
            ", booking date=" + bookingDate + 
            ", period=" + period + 
            ", skier=" + skier.getFullNameFormattedForDisplay();
    }
}
