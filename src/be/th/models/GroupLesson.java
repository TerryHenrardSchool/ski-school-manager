package be.th.models;

import java.time.LocalDateTime;

public class GroupLesson extends Lesson {

	// Static attributes
	private static final long serialVersionUID = 3343552247416871459L;

	// Constructors
	public GroupLesson(
		int id, 
		int minBookings, 
		int maxBookings, 
		LocalDateTime date, 
		String location, 
		int duration
	) {
		super(id, minBookings, maxBookings, date, location, duration);
	}
	
	//Override methods
    @Override
    public boolean equals(Object object) {
    	if(!(object instanceof Lesson)) {
    		return false;
    	}
    	
    	return super.equals(((Lesson) object));
    }
    
    @Override
    public int hashCode() {
    	return super.hashCode();
    }
    
    @Override
    public String toString() {
    	return super.toString();
    }
}
