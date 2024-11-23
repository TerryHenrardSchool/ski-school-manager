package be.th.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import be.th.dao.InstructorDAO;
import be.th.validators.IntegerValidator;
import be.th.validators.ObjectValidator;

public class Instructor extends Person {
	
	// Static attributes
	private static final long serialVersionUID = -5724827101822519690L;
	
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
    	
    	for(Accreditation accreditation: accreditations) {
        	addAccreditation(accreditation);
        }
    }
    
	public void setLessons(Set<Lesson> lessons) {
		if (!ObjectValidator.hasValue(lessons)) {
			throw new IllegalArgumentException("Lessons must be set.");
		}

		if (!IntegerValidator.isGreaterOrEqual(lessons.size(), 1)) {
			throw new IllegalArgumentException("Lessons must have at least one value.");
		}

		for (Lesson lesson : lessons) {
			addLesson(lesson);
		}
	}

    // Methods
	public Lesson findLessonById(int lessonId) {
		if (!IntegerValidator.isPositiveOrEqualToZero(lessonId)) {
			throw new IllegalArgumentException("Lesson ID must be positive.");
		}

		return lessons.stream().
			filter(lesson -> lesson.getId() == lessonId)
			.findFirst()
			.orElse(null);
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
    	
		if (accreditations.contains(accreditation)) {
			throw new IllegalArgumentException("Accreditation already exists.");
		}
    	
    	return accreditations.add(accreditation); 
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
        
		if (lessons.contains(lesson)) {
			throw new IllegalArgumentException("Lesson already exists.");
        }
        return lessons.add(lesson);
    }
    
    public boolean isAccreditate(Accreditation accreditationToCheck) {
        return accreditations.stream().anyMatch(accreditation -> accreditation.equals(accreditationToCheck));
    }
    
    public boolean isAvailable(LocalDate date) {
    	return !lessons.stream().anyMatch(lesson -> lesson.getDate().toLocalDate().equals(date));
    }

    public boolean hasScheduledLesson() {
        return !lessons.isEmpty();
    }

    public ArrayList<Lesson> getSchedule(int instructorId) {
        return new ArrayList<>(); //TODO
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
