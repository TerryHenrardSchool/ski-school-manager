package be.th.ski_school_manager;

import java.time.LocalDate;
import java.util.Objects;

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
        setStartDate(startDate);
        setEndDate(endDate);
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
    	if (!Utils.isPositiveInteger(id)) {
            throw new IllegalArgumentException("ID must be positive.");
    	}
    	this.id = id;
    }
    
    public void setStartDate(LocalDate startDate) {
        if (!Utils.isValidDate(startDate)) {
            throw new IllegalArgumentException("Start date must be a valid date in the past or present.");
        }
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after the start date.");
        }
        this.endDate = endDate;
    }

    public void setIsVacation(Boolean isVacation) {
    	if (!Utils.isBooleanValid(isVacation)) {
            throw new IllegalArgumentException("Vacation status must be specified.");
        }
        this.isVacation = isVacation;
    }

    public void setName(String name) {
        if (!Utils.isNonEmptyString(name)) {
            throw new IllegalArgumentException("Name cannot be null or empty.");
        }
        this.name = name;
    }
    
    //Override methods
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
