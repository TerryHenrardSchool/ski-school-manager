package be.th.dao;

import java.sql.Connection;

import be.th.classes.Booking;
import be.th.classes.Instructor;
import be.th.classes.Lesson;
import be.th.classes.LessonType;
import be.th.classes.Secretary;
import be.th.classes.Skier;

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
	
	public DAO<LessonType> getLessonTypesDAO(){
		return new LessonTypesDAO(CONNECTION);
	}
}
