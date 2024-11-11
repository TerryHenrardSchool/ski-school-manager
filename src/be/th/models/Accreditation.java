package be.th.models;

import java.util.List;
import java.util.Objects;

import be.th.dao.AccreditationDAO;
import be.th.dao.DatabaseConstants;
import be.th.validators.IntegerValidator;
import be.th.validators.StringValidator;

public class Accreditation {
	
	// Attributes
	private int id;
    private String sportType;
    private String ageCategory;

    // Constructor
    public Accreditation(int id, String sportType, String ageCategory) {
    	setId(id);
        setSportType(sportType);
        setAgeCategory(ageCategory);
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
    // Setters
    public void setId(int id) {
    	if (!IntegerValidator.isPositiveOrEqualToZero(id)) {
            throw new IllegalArgumentException("ID must be positive or equals to 0.");
    	}
    	this.id = id;
    }
    
    public void setSportType(String sportType) {
        if (!StringValidator.hasValue(sportType)) {
            throw new IllegalArgumentException("Sport type must have value.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(sportType, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Sport type's length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
        }
        this.sportType = sportType;
    }

    public void setAgeCategory(String ageCategory) {
        if (!StringValidator.hasValue(ageCategory)) {
            throw new IllegalArgumentException("Age category must have value.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(ageCategory, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Age category's length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
        }
        this.ageCategory = ageCategory;
    }
    
    // Database methods
    public static List<Accreditation> findAllInDatabase(AccreditationDAO accreditationDAO){
    	return accreditationDAO.findAll();
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
    		Objects.equals(ageCategory, accreditation.ageCategory);           
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sportType, ageCategory);
    }

    @Override
    public String toString() {
        return "Accreditation:" +
           "id=" + id +
           ", sportType='" + sportType + '\'' +
           ", ageCategory='" + ageCategory + '\'';
    }
}
