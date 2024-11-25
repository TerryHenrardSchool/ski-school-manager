package be.th.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import be.th.models.Accreditation;
import be.th.models.Booking;
import be.th.models.Instructor;
import be.th.models.Lesson;
import be.th.models.LessonType;
import be.th.models.Location;
import be.th.models.Skier;

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
	        ps.setTimestamp(1, Timestamp.valueOf(lesson.getDate()));
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
	    String sqlDeleteBookings = """
	        DELETE FROM bookings
	        WHERE lesson_id = ?
	    """;
	    
	    String sqlDeleteLesson = """
	        DELETE FROM lessons
	        WHERE lesson_id = ?
	    """;

	    try (
	        PreparedStatement pstmtDeleteBookings = connection.prepareStatement(sqlDeleteBookings);
	        PreparedStatement pstmtDeleteLesson = connection.prepareStatement(sqlDeleteLesson)
	    ) {
	        pstmtDeleteBookings.setInt(1, id);
	        pstmtDeleteBookings.executeUpdate();

	        pstmtDeleteLesson.setInt(1, id);
	        int affectedRows = pstmtDeleteLesson.executeUpdate();

	        return affectedRows > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
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
	    String sql = """
	        SELECT
	            l.*,
	            lt.*,
	            a.*,
	            loc.location_id AS location_id_1, loc.name AS location_name,
	            p.person_id AS instructor_person_id, p.last_name, p.first_name, p.date_of_birth, 
	            p.phone_number, p.email, p.city, p.postcode, p.street_number, p.street_name
	        FROM
	            lessons l
	        INNER JOIN
	            lesson_types lt ON lt.lesson_type_id = l.lesson_type_id
	        INNER JOIN
	            accreditations a ON a.accreditation_id = lt.accreditation_id
	        INNER JOIN
	            locations loc ON loc.location_id = l.location_id
	        INNER JOIN
	            instructors i ON i.instructor_id = l.instructor_id
	        INNER JOIN
	            persons p ON p.person_id = i.person_id
	        ORDER BY l.lesson_id
	    """;

	    Map<Integer, Lesson> lessonMap = new HashMap<>();

	    try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
	        while (rs.next()) {
	            int lessonId = rs.getInt("lesson_id");

	            Location location = new Location(
	                rs.getInt("location_id_1"),
	                rs.getString("location_name")
	            );
	            
	            Accreditation accreditation = new Accreditation(
		                rs.getInt("accreditation_id"),
		                rs.getString("sport"),
		                rs.getString("age_category_name")
		            );

	            int maxAge = rs.getInt("max_age");
	    	    Optional<Integer> maxAgeOptional = rs.wasNull() ? Optional.empty() : Optional.of(maxAge);
	            LessonType lessonType = new LessonType(
	                rs.getInt("lesson_type_id"),
	                rs.getString("name"),
	                rs.getDouble("price"),
	                rs.getString("skill_level"),
	                rs.getString("age_category_name"),
	                rs.getInt("min_age"),
	                maxAgeOptional,
	                rs.getInt("min_bookings"),
	                rs.getInt("max_bookings"),
	                accreditation
	            );

	            Instructor instructor = new Instructor(
	                rs.getInt("instructor_person_id"),
	                rs.getString("last_name"),
	                rs.getString("first_name"),
	                rs.getDate("date_of_birth").toLocalDate(),
	                rs.getString("city"),
	                rs.getString("postcode"),
	                rs.getString("street_name"),
	                rs.getString("street_number"),
	                rs.getString("phone_number"),
	                rs.getString("email"),
	                Set.of(accreditation)
	            );

	            Lesson lesson = lessonMap.get(lessonId);
	            if (lesson == null) {
	                lesson = new Lesson(
	                    lessonId,
	                    rs.getTimestamp("start_date").toLocalDateTime(),
	                    lessonType,
	                    instructor,
	                    location.getId(),
	                    location.getName()
	                );
	                lessonMap.put(lessonId, lesson);
	            }

	            lessonType.setAccreditation(accreditation);
	            lesson.setLessonType(lessonType);

	            loadLessonBookings(lesson);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lessonMap.values().stream().toList();
	}
	
	public List<Lesson> findAll(LocalDate date) {
	    String sql = """
	        SELECT
	            l.*,
	            lt.*,
	            a.*,
	            loc.location_id AS location_id_1, loc.name AS location_name,
	            p.person_id AS instructor_person_id, p.last_name, p.first_name, p.date_of_birth, 
	            p.phone_number, p.email, p.city, p.postcode, p.street_number, p.street_name
	        FROM
	            lessons l
	        INNER JOIN
	            lesson_types lt ON lt.lesson_type_id = l.lesson_type_id
	        INNER JOIN
	            accreditations a ON a.accreditation_id = lt.accreditation_id
	        INNER JOIN
	            locations loc ON loc.location_id = l.location_id
	        INNER JOIN
	            instructors i ON i.instructor_id = l.instructor_id
	        INNER JOIN
	            persons p ON p.person_id = i.person_id
	        WHERE 
	            l.start_date > ?
	        ORDER BY l.lesson_id
	    """;

	    Map<Integer, Lesson> lessonMap = new HashMap<>();
	    
	    try (PreparedStatement ps = connection.prepareStatement(sql)) {
	    	ps.setDate(1, Date.valueOf(date));
	    	ResultSet rs = ps.executeQuery();
	    	
	        while (rs.next()) {
	            int lessonId = rs.getInt("lesson_id");

	            Location location = new Location(
	                rs.getInt("location_id_1"),
	                rs.getString("location_name")
	            );
	            
	            Accreditation accreditation = new Accreditation(
		                rs.getInt("accreditation_id"),
		                rs.getString("sport"),
		                rs.getString("age_category_name")
		            );

	            int maxAge = rs.getInt("max_age");
	    	    Optional<Integer> maxAgeOptional = rs.wasNull() ? Optional.empty() : Optional.of(maxAge);
	            LessonType lessonType = new LessonType(
	                rs.getInt("lesson_type_id"),
	                rs.getString("name"),
	                rs.getDouble("price"),
	                rs.getString("skill_level"),
	                rs.getString("age_category_name"),
	                rs.getInt("min_age"),
	                maxAgeOptional,
	                rs.getInt("min_bookings"),
	                rs.getInt("max_bookings"),
	                accreditation
	            );

	            Instructor instructor = new Instructor(
	                rs.getInt("instructor_person_id"),
	                rs.getString("last_name"),
	                rs.getString("first_name"),
	                rs.getDate("date_of_birth").toLocalDate(),
	                rs.getString("city"),
	                rs.getString("postcode"),
	                rs.getString("street_name"),
	                rs.getString("street_number"),
	                rs.getString("phone_number"),
	                rs.getString("email"),
	                Set.of(accreditation)
	            );

	            Lesson lesson = lessonMap.get(lessonId);
	            if (lesson == null) {
	                lesson = new Lesson(
	                    lessonId,
	                    rs.getTimestamp("start_date").toLocalDateTime(),
	                    lessonType,
	                    instructor,
	                    location.getId(),
	                    location.getName()
	                );
	                lessonMap.put(lessonId, lesson);
	            }

	            lessonType.setAccreditation(accreditation);
	            lesson.setLessonType(lessonType);

	            loadLessonBookings(lesson);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return lessonMap.values().stream().toList();
	}
	
	private void loadLessonBookings(Lesson lesson) throws SQLException {
		String sql = """
	        SELECT
			    b.*, p.*, per.*, s.skier_id as skier_id_1
			FROM
			    bookings b
			INNER JOIN 
			    periods p ON p.period_id = b.period_id
			INNER JOIN 
			    skiers s ON s.skier_id = b.skier_id
			INNER JOIN 
			    persons per ON per.person_id = s.person_id
			WHERE
			    b.lesson_id = ?
	    """;

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, lesson.getId());
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	        	Booking booking = new Booking(
        			rs.getInt("booking_id"),
        			rs.getTimestamp("booking_date").toLocalDateTime(),
        			rs.getBoolean("is_insured"),
        			rs.getInt("period_id"),
        			rs.getDate("start_date").toLocalDate(),
        			rs.getDate("end_date").toLocalDate(),
        			rs.getBoolean("is_vacation"),
        			rs.getString("name"),
        			lesson,
        			new Skier(
    					rs.getInt("skier_id_1"),
    					rs.getString("last_name"),
    					rs.getString("first_name"),
    					rs.getDate("date_of_birth").toLocalDate(),
    					rs.getString("city"),
    					rs.getString("postcode"),
    					rs.getString("street_name"),
    					rs.getString("street_number"),
    					rs.getString("phone_number"),
    					rs.getString("email")
					)
	            );
	            lesson.addBooking(booking);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println(e.getMessage());
	    }
	}
}
