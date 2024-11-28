package be.th.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import be.th.dao.InstructorDAO;
import be.th.validators.IntegerValidator;
import be.th.validators.ObjectValidator;

public class Instructor extends Person {
	
	// References
	private Set<Accreditation> accreditations;
	private Set<Lesson> lessons; 

	// Constructors
    public Instructor(
		int id, 
		String lastName, 
		String firstName, 
		LocalDate dateOfBirth,
		String city, 
		String postcode, 
		String streetName, 
		String streetNumber,
		String phoneNumber, 
		String email,
		Set<Accreditation> accreditations
	) {
        super(id, lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email);
        
        this.accreditations = new LinkedHashSet<Accreditation>();
        this.lessons = new LinkedHashSet<Lesson>();
        setAccreditations(accreditations);
    }
    
    public Instructor(
		String lastName, 
		String firstName, 
		LocalDate dateOfBirth,
		String city, 
		String postcode, 
		String streetName, 
		String streetNumber,
		String phoneNumber, 
		String email,
		Set<Accreditation> accreditations
	) {
    	this(0, lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email, accreditations);
    }
    
    public Instructor(
		String lastName, 
		String firstName, 
		LocalDate dateOfBirth,
		String city, 
		String postcode, 
		String streetName, 
		String streetNumber,
		String phoneNumber, 
		String email,
		Accreditation accreditation
	) {
        this(0, lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email, Set.of(accreditation));
    }
    
    // Getters
    public Set<Accreditation> getAccreditations(){
        return Collections.unmodifiableSet(accreditations);
    }
    
	public Set<Lesson> getLessons() {
		return Collections.unmodifiableSet(lessons);
	}
    
    // Setters
    public void setAccreditations(Set<Accreditation> accreditations) {
    	if(!ObjectValidator.hasValue(accreditations)) {
    		throw new IllegalArgumentException("Accreditations must be set.");
    	}
    	
    	if(!IntegerValidator.isGreaterOrEqual(accreditations.size(), 1)) {
        	throw new IllegalArgumentException("Accreditations must have at least one value.");
        }
    	
		accreditations.forEach(accreditation -> addAccreditation(accreditation));
    }
    
    // Private methods
    private boolean isLessonSlotFree(Lesson newLesson) {
		LocalDateTime lessonDate = newLesson.getDate();
		
		return lessons.stream().noneMatch(instructorLesson -> {
			return instructorLesson.getDate().equals(lessonDate);
		});
	}
    
    private boolean isAvailable(LocalDateTime dateTime) {
    	return lessons.stream().noneMatch(lesson -> lesson.getDate().equals(dateTime));
    }
    
    private boolean isLessonInMonth(Lesson lesson, Month month) {
	    return lesson.getDate().getMonth().equals(month);
	}
	
	private boolean isLessonInYear(Lesson lesson, int year) {
		return lesson.getDate().getYear() == year;
   }
	
	private double calculateTotalRevenue(Collection<Lesson> lessons) {
        return lessons.stream()
            .mapToDouble(this::calculateTotalRevenueForLesson)
            .sum();
    }
	
	private double calculateTotalRevenueForLesson(Lesson lesson) {
	    return lesson.getBookings().stream()
	        .mapToDouble(Booking::calculatePrice)
	        .sum();
	}

    // Public methods
	public void clearAccreditations() {
		accreditations.clear();
	}
	
	public boolean removeAccreditation(Accreditation accreditation) {
		if (!ObjectValidator.hasValue(accreditation)) {
			throw new IllegalArgumentException("Accreditation must have value");
		}
		
		return accreditations.remove(accreditation);
	}
	
	public boolean addAccreditation(Accreditation accreditation) {
		if (!ObjectValidator.hasValue(accreditation)) {
			throw new IllegalArgumentException("Accreditation must have value");
		}
		
		return accreditations.add(accreditation); 
	}
	
	public boolean isAccreditate(Accreditation accreditationToCheck) {
        return accreditations.stream().anyMatch(accreditation -> accreditation.equals(accreditationToCheck));
    }
	
	public Lesson findLessonById(int lessonId) {
		if (!IntegerValidator.isPositiveOrEqualToZero(lessonId)) {
			throw new IllegalArgumentException("Lesson ID must be positive.");
		}

		return lessons.stream()
			.filter(lesson -> lesson.getId() == lessonId)
			.findFirst()
			.orElse(null);
	}
	
    public boolean removeLesson(Lesson lesson) {
		if (!ObjectValidator.hasValue(lesson)) {
			throw new IllegalArgumentException("Lesson must have value");
		}
		
		return lessons.remove(lesson);
    }
    
    public boolean addLesson(Lesson lesson) {
        if (!ObjectValidator.hasValue(lesson)) {
        	throw new IllegalArgumentException("Lesson must have value");
        }
        
		if (!isLessonSlotFree(lesson)) {
			throw new IllegalArgumentException("Instructor already has a lesson for this date and time.");
		}
		
        return lessons.add(lesson);
    }
    
    public boolean isEligible(Accreditation accreditation, LocalDateTime lessonStartTime) {
        return isAccreditate(accreditation) && isAvailable(lessonStartTime);
    }
    
    public double calculateGeneratedRevenue() {
        return calculateTotalRevenue(lessons);
    }
	
    public double calculateGeneratedRevenueForCurrentMonthOfCurrentYear() {
        LocalDate now = LocalDate.now();
        Month currentMonth = now.getMonth();
        int currentYear = now.getYear();

        return calculateTotalRevenue(
    		getLessons().stream()
            .filter(lesson -> isLessonInMonth(lesson, currentMonth) && isLessonInYear(lesson, currentYear))
            .toList()
        );
    }
	
    // Database methods
    public boolean insertIntoDatabase(InstructorDAO instructorDAO) {
    	return instructorDAO.create(this);
    }
    
    public boolean updateInDatabase(InstructorDAO instructorDAO) {
    	return instructorDAO.update(this);
    }
    
    public boolean deleteFromDatabase(InstructorDAO instructorDAO) {
    	return instructorDAO.delete(getId());
    }
    
    public static List<Instructor> findAllInDatabase(InstructorDAO instructorDAO){
    	return instructorDAO.findAll();
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
    	return super.hashCode();
    }
    
    @Override
    public String toString() {
    	String result = "";
    	
    	result += super.toString() + " ";
    	
    	result += "List of accreditations:";
    	for(Accreditation accreditation: accreditations) {
    		result += accreditation;
    	}
    	
    	result += "List of lessons:";
    	for(Lesson lesson: lessons) {
    		result += lesson;
		}
    	
    	return result;
    }
}
