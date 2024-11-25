package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import be.th.models.Accreditation;
import be.th.models.LessonType;

public class LessonTypeDAO extends DAO<LessonType>{

	public LessonTypeDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean create(LessonType obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(LessonType obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LessonType find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LessonType> findAll() {
	    String sql = """
	        SELECT 
	            lesson_type_id, name, price, skill_level, 
	            age_category_name, min_age, max_age, 
	            min_bookings, max_bookings, is_private,
	            accreditation_id, sport
	        FROM 
	            lesson_types 
	        NATURAL JOIN 
	            accreditations
	        ORDER BY 
	            lesson_type_id
	    """;

	    List<LessonType> lessonTypes = new ArrayList<>();

	    try (PreparedStatement stmt = super.connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Accreditation accreditation = new Accreditation(
	                rs.getInt("accreditation_id"),
	                rs.getString("sport"),
	                rs.getString("age_category_name")
	            );

	            Optional<Integer> maxAge = Optional.of(rs.getInt("max_age"));
	            if (rs.wasNull()) maxAge = Optional.empty();

	            LessonType lessonType = new LessonType(
	                rs.getInt("lesson_type_id"),
	                rs.getString("skill_level"),
	                rs.getDouble("price"),
	                rs.getString("name"),
	                rs.getString("age_category_name"),
	                rs.getInt("min_age"),
	                maxAge,
	                rs.getInt("min_bookings"),
	                rs.getInt("max_bookings"),
	                rs.getBoolean("is_private"),
	                accreditation
	            );

	            lessonTypes.add(lessonType);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lessonTypes;
	}


	@Override
	public List<LessonType> findAll(Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
