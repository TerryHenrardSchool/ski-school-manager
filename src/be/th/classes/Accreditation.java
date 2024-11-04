package be.th.classes;

import java.util.Objects;

public class Accreditation {
	
	// Attributes
	private int id;
    private String sportType;
    private String ageCategory;
    private String lessonLevel;

    // Constructor
    public Accreditation(int id, String sportType, String ageCategory, String lessonLevel) {
    	setId(id);
        setSportType(sportType);
        setAgeCategory(ageCategory);
        setLessonLevel(lessonLevel);
    }

    // Getters
    public int getId() {
    	return id;
    }
    
    public String getSportType() {
        return sportType;
    }

    public String getAgeCategory() {
        return ageCategory;
    }

    public String getLessonLevel() {
        return lessonLevel;
    }

    // Setters
    public void setId(int id) {
    	if (!Utils.isPositiveInteger(id)) {
            throw new IllegalArgumentException("ID must be positive.");
    	}
    	this.id = id;
    }
    
    public void setSportType(String sportType) {
        if (!Utils.isNonEmptyString(sportType)) {
            throw new IllegalArgumentException("Sport type cannot be null or empty.");
        }
        this.sportType = sportType;
    }

    public void setAgeCategory(String ageCategory) {
        if (!Utils.isNonEmptyString(ageCategory)) {
            throw new IllegalArgumentException("Age category cannot be null or empty.");
        }
        this.ageCategory = ageCategory;
    }

    public void setLessonLevel(String lessonLevel) {
        if (!Utils.isNonEmptyString(lessonLevel)) {
            throw new IllegalArgumentException("Lesson level cannot be null or empty.");
        }
        this.lessonLevel = lessonLevel;
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

        Accreditation accreditation = (Accreditation) object;
        return id == accreditation.id &&
           Objects.equals(sportType, accreditation.sportType) &&
           Objects.equals(ageCategory, accreditation.ageCategory) &&
           Objects.equals(lessonLevel, accreditation.lessonLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sportType, ageCategory, lessonLevel);
    }

    @Override
    public String toString() {
        return "Accreditation:" +
           "id=" + id +
           ", sportType='" + sportType + '\'' +
           ", ageCategory='" + ageCategory + '\'' +
           ", lessonLevel='" + lessonLevel + '\'';
    }
}
