package be.th.dao;

import java.sql.Connection;

import be.th.models.Accreditation;
import be.th.models.Booking;
import be.th.models.Instructor;
import be.th.models.Lesson;
import be.th.models.LessonType;
import be.th.models.Secretary;
import be.th.models.Skier;

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
	
	public DAO<LessonType> getLessonTypeDAO(){
		return new LessonTypeDAO(CONNECTION);
	}
	
	public DAO<Accreditation> getAccreditationDAO(){
		return new AccreditationDAO(CONNECTION);
	}
}
