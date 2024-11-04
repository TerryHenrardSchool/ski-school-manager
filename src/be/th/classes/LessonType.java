package be.th.classes;

import java.util.Objects;

public class LessonType {
	
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
    	if (!Utils.isPositiveInteger(id)) {
            throw new IllegalArgumentException("ID must be positive.");
    	}
    	this.id = id;
    }
    
    public void setLevel(String level) {
        if (!Utils.isNonEmptyString(level)) {
            throw new IllegalArgumentException("Level cannot be null or empty.");
        }
        this.level = level;
    }

    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price must be non-negative.");
        }
        this.price = price;
    }

    public void setName(String name) {
        if (!Utils.isNonEmptyString(name)) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }
    
    public void setAgeCategoryName(String ageCategoryName) {
    	if (!Utils.isNonEmptyString(ageCategoryName)) {
    		throw new IllegalArgumentException("Age category name cannot be null or empty.");
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
           Objects.equals(name, lessonType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, price, name);
    }

    @Override
    public String toString() {
        return "LessonType:" +
           "id=" + id +
           ", level='" + level + '\'' +
           ", price=" + price +
           ", name='" + name + '\'';
    }
}
