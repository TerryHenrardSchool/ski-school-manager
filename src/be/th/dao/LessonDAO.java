package be.th.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import be.th.models.Lesson;

public class LessonDAO extends DAO<Lesson>{

	public LessonDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Lesson lesson) {
	    String query = """
	    	INSERT INTO lessons (
    		   	start_date, 
    		   	location_id, 
    		   	secretary_id, 
    		   	instructor_id, 
    		   	lesson_type_id
		   	) 
    		VALUES (?, ?, ?, ?, ?)
		""";
	    try (PreparedStatement ps = connection.prepareStatement(query)) {
	        ps.setDate(1, Date.valueOf(lesson.getDate().toLocalDate()));
	        ps.setInt(2, lesson.getLocation().getId());
	        ps.setInt(3, 21);
	        ps.setInt(4, lesson.getInstructor().getId());
	        ps.setInt(5, lesson.getLessonType().getId());
	        return ps.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	@Override
	public boolean delete(int id) {
		return false; // TODO
	}

	@Override
	public boolean update(Lesson obj) {
		return false; // TODO
	}

	@Override
	public Lesson find(int id) {
		return null; // TODO
	}

	@Override
	public List<Lesson> findAll(Map<String, Object> criteria) {
 		return null; // TODO
	}

	@Override
	public List<Lesson> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
