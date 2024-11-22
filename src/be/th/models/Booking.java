package be.th.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import be.th.validators.IntegerValidator;
import be.th.validators.ObjectValidator;
import be.th.dao.BookingDAO;
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
    private Lesson lesson;

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
		Lesson lesson,
		Skier skier
	) {
        setId(id);
        setBookingDate(bookingDate);
        setInsured(isInsured);
        setSkier(skier);
        setLesson(lesson);
        this.period = new Period(periodId, startDate, endDate, isVacation, name);
        lesson.addBooking(this);
    }
    
    public Booking(
		LocalDateTime bookingDate, 
		boolean isInsured, 
		int periodId, 
		LocalDate startDate, 
		LocalDate endDate, 
		boolean isVacation, 
		String name,
		Lesson lesson,
		Skier skier
	) {
    	this(0, bookingDate, isInsured, periodId, startDate, endDate, isVacation, name, lesson, skier);
    }
    
    public Booking(
		LocalDateTime bookingDate, 
		boolean isInsured, 
		Period period,
		Lesson lesson,
		Skier skier
		) {
    	this(0, bookingDate, isInsured, period.getId(), period.getStartDate(), period.getEndDate(), period.isVacation(), period.getName(), lesson, skier);
    }

    // Getters
    public int getId() {
        return id;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public boolean getInsurance() {
        return isInsured;
    }
    
	public Period getPeriod() {
		return period;
	}
	
	public Skier getSkier() {
		return skier;
	}
	
	public Lesson getLesson() {
		return lesson;
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
    
    public void setLesson(Lesson lesson) {
    	if (!ObjectValidator.hasValue(lesson)) {
    		throw new IllegalArgumentException("Lesson must have a value.");
    	}
    	this.lesson = lesson;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
    	if (!DateValidator.hasValue(bookingDate)) {
            throw new IllegalArgumentException("Booking date must have a value.");
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
        return 0.0; // TODO: Implement price calculation logic (insurance + 15% if evening and afternoon)
    }
    
    public long calculateDaysUntilLesson() {
        LocalDate now = LocalDate.now();
        LocalDateTime lessonDate = lesson.getDate();

        return Math.max(0, ChronoUnit.DAYS.between(now, lessonDate));
    }
    
	public LocalDate getLessonDate() {
		return lesson.getDate().toLocalDate();
	}
    
    // Database methods
	public boolean insertIntoDatabase(BookingDAO bookingDAO) {
		return bookingDAO.create(this);
	}
    
	public boolean deleteFromDatabase(BookingDAO bookingDAO) {
		return bookingDAO.delete(id);
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
        return Objects.hash(id, isInsured, bookingDate);
    }
    
    @Override
    public String toString() {
        return "Booking: " +
            "id=" + id +
            ", insured=" + isInsured +
            ", booking date=" + bookingDate + 
            ", period=" + period.getName() + 
            ", skier=" + skier.getFullNameFormattedForDisplay();
    }
}
