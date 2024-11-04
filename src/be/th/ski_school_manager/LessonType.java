package be.th.ski_school_manager;

import java.util.Objects;

public class LessonType {
	
	// Attributes
	private int id;
    private String level;
    private double price;
    private String name;
    private String ageCategory;

    // Constructor
    public LessonType(int id, String level, double price, String name, String ageCategory) {
    	setId(id);
        setLevel(level);
        setPrice(price);
        setName(name);
        setAgeCategory(ageCategory);
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

    public String getAgeCategory() {
        return ageCategory;
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

    public void setAgeCategory(String ageCategory) {
        if (!Utils.isNonEmptyString(ageCategory)) {
            throw new IllegalArgumentException("Age category cannot be null or empty.");
        }
        this.ageCategory = ageCategory;
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
           Objects.equals(ageCategory, lessonType.ageCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, level, price, name, ageCategory);
    }

    @Override
    public String toString() {
        return "LessonType:" +
           "id=" + id +
           ", level='" + level + '\'' +
           ", price=" + price +
           ", name='" + name + '\'' +
           ", ageCategory='" + ageCategory + '\'';
    }

}
