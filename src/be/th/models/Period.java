package be.th.models;

import java.time.LocalDate;
import java.util.Objects;
import be.th.validators.IntegerValidator;
import be.th.validators.DateValidator;
import be.th.dao.DatabaseConstants;
import be.th.validators.BooleanValidator;
import be.th.validators.StringValidator;

public class Period {
    
    // Attributes
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isVacation;
    private String name;

    // Constructor
    public Period(int id, LocalDate startDate, LocalDate endDate, boolean isVacation, String name) {
        setId(id);
        setStartAndEndDates(startDate, endDate);
        setIsVacation(isVacation);
        setName(name);
    }

    // Getters
    public int getId() {
        return id;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isVacation() {
        return isVacation;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setId(int id) {
        if (!IntegerValidator.isPositiveOrEqualToZero(id)) {
            throw new IllegalArgumentException("ID must be a positive integer. Negative values are not allowed.");
        }
        this.id = id;
    }
    
    public void setStartAndEndDates(LocalDate startDate, LocalDate endDate) {
    	if (!DateValidator.hasValue(startDate)) {
    		throw new IllegalArgumentException("Start date must have value.");
    	}
    	
    	if (!DateValidator.hasValue(endDate)) {
    		throw new IllegalArgumentException("End date must have value.");
    	}
    	
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before the end date.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setIsVacation(Boolean isVacation) {
        if (!BooleanValidator.hasValue(isVacation)) {
            throw new IllegalArgumentException("Vacation status must be specified as either true or false.");
        }
        this.isVacation = isVacation;
    }

    public void setName(String name) {
        if (!StringValidator.hasValue(name)) {
            throw new IllegalArgumentException("Name must be a non-empty string. Null or empty values are not allowed.");
        }
        
        if(!StringValidator.isLengthSmallerOrEqual(name, DatabaseConstants.MAX_CHARACTERS)) {
            throw new IllegalArgumentException("Name length must be smaller than " + DatabaseConstants.MAX_CHARACTERS);
        }
        this.name = name;
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

        Period period = (Period) object;
        return id == period.id &&
            isVacation == period.isVacation && 
            Objects.equals(startDate, period.startDate) &&
            Objects.equals(endDate, period.endDate) &&
            Objects.equals(name, period.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, isVacation, startDate, endDate, name);
    }
    
    @Override
    public String toString() {
        return "Period: " +
                "id=" + id +
                ", isVacation=" + isVacation +
                ", startDate=" + startDate + 
                ", endDate=" + endDate + 
                ", name='" + name + '\'';
    }
}
