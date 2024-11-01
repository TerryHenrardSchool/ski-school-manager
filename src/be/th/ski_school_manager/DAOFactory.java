package be.th.ski_school_manager;

import java.sql.Connection;

public class DAOFactory {
	protected static final Connection CONNECTION  = SkiSchoolConnection.getInstance();
	
	public DAO<Lesson> getLessonDAO(){
		return new LessonDAO(CONNECTION);
	}
	
	public DAO<Booking> getBookingDAO(){
		return new BookingDAO(CONNECTION);
	}
	
	public DAO<Skier> getSkierDAO(){
		return new SkierDAO(CONNECTION);
	}
	
	public DAO<Instructor> getInstructorDAO(){
		return new InstructorDAO(CONNECTION);
	}
	
	public DAO<Secretary> getSecretaryDAO(){
		return new SecretaryDAO(CONNECTION);
	}
}
