package be.th.models;

import java.util.Objects;

import be.th.dao.DatabaseConstants;
import be.th.validators.IntegerValidator;
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

    // Constructor
    public LessonType(int id, String level, double price, String name, String ageCategoryName) {
        setId(id);
        setLevel(level);
        setPrice(price);
        setName(name);
        setAgeCategoryName(ageCategoryName);
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
        
        if(!StringValidator.isLengthSmallerOrEqual(level, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Level length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
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
        
        if(!StringValidator.isLengthSmallerOrEqual(level, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Lesson type's name length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
        }
        this.name = name;
    }
    
    public void setAgeCategoryName(String ageCategoryName) {
        if (!StringValidator.hasValue(ageCategoryName)) {
            throw new IllegalArgumentException("Age category name must be a non-empty string. Null or empty values are not allowed.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(ageCategoryName, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Age category name's name length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
        }
        this.ageCategoryName = ageCategoryName;
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
           Double.compare(lessonType.price, price) == 0 &&
           Objects.equals(level, lessonType.level) &&
           Objects.equals(name, lessonType.name) &&
           Objects.equals(ageCategoryName, lessonType.ageCategoryName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, price, name, ageCategoryName);
    }

    @Override
    public String toString() {
        return "LessonType:" +
           "id=" + id +
           ", level='" + level + '\'' +
           ", price=" + price +
           ", name='" + name + '\'' +
           ", ageCategoryName='" + ageCategoryName + '\'';
    }
}