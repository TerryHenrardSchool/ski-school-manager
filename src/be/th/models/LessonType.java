package be.th.models;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import be.th.dao.DatabaseConstant;
import be.th.dao.LessonTypeDAO;
import be.th.validators.IntegerValidator;
import be.th.validators.ObjectValidator;
import be.th.validators.StringValidator;

public class LessonType {
	
	// Constant attributes
	private final int MIN_PRICE = 0;
    
    // Attributes
    private int id;
    private String level;
    private double price;
    private String name;
    private String ageCategoryName;
    private int minAge;
    private Optional<Integer> maxAge;
    private int minBookings;
    private int maxBookings;
    private boolean isPrivate;
    
    // References
    private Accreditation accreditation;

    // Constructor
    public LessonType(
		int id, 
		String level, 
		double price, 
		String name, 
		String ageCategoryName, 
		int minAge, 
		Optional<Integer> maxAge, 
		int minBookings, 
		int maxBookings,
		boolean isPrivate,
		Accreditation accreditation
	) {
        setId(id);
        setLevel(level);
        setPrice(price);
        setName(name);
        setAgeCategoryName(ageCategoryName);
        setMinAge(minAge);
        setMaxAge(maxAge);
        setMinBookings(minBookings);
        setMaxBookings(maxBookings);
        setIsPrivate(isPrivate);
        setAccreditation(accreditation);
    }

    // Getters
    public int getId() {
        return id;
    }
    
    public String getLevel() {
        return level;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
    
    public String getAgeCategoryName() {
        return ageCategoryName;
    }
    
    public int getMinAge() {
    	return minAge;
    }
    
    public Optional<Integer> getMaxAge(){
    	return maxAge;
    }
    
    public int getMinBookings() {
    	return minBookings;
    }
    
    public int getMaxBookings() {
    	return maxBookings;
    }
    
	public boolean getIsPrivate() {
		return isPrivate;
	}
    
    public Accreditation getAccreditation() {
    	return accreditation;
    }

    // Setters
    public void setId(int id) {
        if (!IntegerValidator.isPositiveOrEqualToZero(id)) {
            throw new IllegalArgumentException("ID must be a positive integer. Negative values are not allowed.");
        }
        
        this.id = id;
    }
    
    public void setLevel(String level) {
        if (!StringValidator.hasValue(level)) {
            throw new IllegalArgumentException("Level must be a non-empty string. Null or empty values are not allowed.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(level, DatabaseConstant.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Level length must be smaller than " + DatabaseConstant.MAX_CHARACTERS);
        }
        
        this.level = level;
    }

    public void setPrice(double price) {
        if (!IntegerValidator.isGreaterOrEqual(price, MIN_PRICE)) {
            throw new IllegalArgumentException("Price must be a non-negative value. Negative values are not allowed.");
        }
        
        this.price = price;
    }

    public void setName(String name) {
        if (!StringValidator.hasValue(name)) {
            throw new IllegalArgumentException("Name must be a non-empty string. Null or empty values are not allowed.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(level, DatabaseConstant.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Lesson type's name length must be smaller than " + DatabaseConstant.MAX_CHARACTERS);
        }
        
        this.name = name;
    }
    
    public void setAgeCategoryName(String ageCategoryName) {
        if (!StringValidator.hasValue(ageCategoryName)) {
            throw new IllegalArgumentException("Age category name must be a non-empty string. Null or empty values are not allowed.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(ageCategoryName, DatabaseConstant.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Age category name's name length must be smaller than " + DatabaseConstant.MAX_CHARACTERS);
        }
        
        this.ageCategoryName = ageCategoryName;
    }
    
    public void setMinAge(int minAge) {
    	if(!IntegerValidator.isGreaterOrEqual(minAge, 1)) {
    		throw new IllegalArgumentException("Min age must be a positive number.");    		
    	}
    	
    	this.minAge = minAge;
    }
    
    public void setMaxAge(Optional<Integer> maxAge) {
    	if(maxAge.isPresent() && !IntegerValidator.isGreaterOrEqual(maxAge, 1)) {
    		throw new IllegalArgumentException("Max age must be a positive number.");    		
    	}
    	
    	this.maxAge = maxAge;
    }
    
    public void setMinBookings(int minBookings) {
    	if(!IntegerValidator.isPositiveOrEqualToZero(minBookings)) {
    		throw new IllegalArgumentException("Min bookings must be positive or equal to zero.");    		    		
    	}
    	
    	this.minBookings = minBookings;
    }
    
    public void setMaxBookings(int maxBookings) {
    	if(!IntegerValidator.isPositiveOrEqualToZero(maxBookings)) {
    		throw new IllegalArgumentException("Max bookings must be positive or equal to zero.");    		    		
    	}
    	
    	this.maxBookings = maxBookings;
    }
    
	public void setIsPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
    
    public void setAccreditation(Accreditation accreditation) {
    	if(!ObjectValidator.hasValue(accreditation)) {    		
    		throw new IllegalArgumentException("A lesson type must have an accreditation.");    		    		
    	}
    	
    	this.accreditation = accreditation;
    }
    
    // Methods 
    public boolean isValidAge(int age) {
        if (this.maxAge.isPresent()) {
            return age >= this.minAge && age <= this.maxAge.get();
        } else {
            return age >= this.minAge;
        }
    }
    
    public String getLessonTypeInfoFormattedForDisplay() {
    	return name + " " + ageCategoryName  + " - " + level;
    }
    
    // Database methods
    public static Collection<LessonType> findAllInDatabase(LessonTypeDAO lessonTypeDao){
    	return lessonTypeDao.findAll();
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

        LessonType lessonType = (LessonType) object;
        return id == lessonType.id &&
    		minAge == lessonType.minAge &&
    		minBookings == lessonType.minBookings && 
    		maxBookings == lessonType.maxBookings &&
    		Objects.equals(maxAge, lessonType.maxAge) &&
            Double.compare(lessonType.price, price) == 0 &&
            Objects.equals(level, lessonType.level) &&
            Objects.equals(name, lessonType.name) &&
            Objects.equals(ageCategoryName, lessonType.ageCategoryName) &&
            Objects.equals(isPrivate, lessonType.isPrivate) &&
        	accreditation.equals(lessonType.accreditation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, price, name, ageCategoryName, minAge, maxAge, minBookings, maxBookings, isPrivate, accreditation.hashCode());
    }

    @Override
    public String toString() {
        return "LessonType:" +
           "id=" + id +
           ", level='" + level + '\'' +
           ", price=" + price +
           ", name='" + name + '\'' +
           ", ageCategoryName='" + ageCategoryName + '\'' + 
           ", minAge'" + minAge + '\'' + 
           ", maxAge'" + maxAge + '\'' +
           ", minBookings'" + minBookings + '\'' +
           ", maxBookings'" + maxBookings + '\'' + 
           ", isPrivate'" + isPrivate + '\'' +
           ", Accreditation'" + accreditation + '\'';
    }
}
