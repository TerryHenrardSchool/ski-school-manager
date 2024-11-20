package be.th.models;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import be.th.dao.DatabaseConstant;
import be.th.dao.LocationDAO;
import be.th.validators.IntegerValidator;
import be.th.validators.StringValidator;

public class Location {
	
	// Attributes
	private int id;
	private String name;
	
	// Constructors
	public Location(int id, String name) {
		setId(id);
		setName(name);
	}
	
	// Getters
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	// Setters
	public void setId(int id) {
		if (!IntegerValidator.isPositiveOrEqualToZero(id)) {
            throw new IllegalArgumentException("Id must be positive or equal to 0.");
        }
        this.id = id;
	}
	
	public void setName(String name) {
		if (!StringValidator.hasValue(name)) {
            throw new IllegalArgumentException("Location's name must have a value.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(name, DatabaseConstant.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Location name's length must be smaller than " + DatabaseConstant.MAX_CHARACTERS);
        }
        this.name = name;
	}
	
	// Database methods
	public static Collection<Location> findAllInDatabase(LocationDAO locationDAO){
		return locationDAO.findAll();
	}
	
	// Override methods
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Location location= (Location) object;
        return id == location.id &&
            Objects.equals(name, location.name);
	}
	
	@Override
	public String toString() {
		return "Location: " +
	        "id=" + id +
	        ", name='" + name + '\'';
	}
}
