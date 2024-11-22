package be.th.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import be.th.formatters.DatabaseFormatter;
import be.th.models.Accreditation;
import be.th.models.Booking;
import be.th.models.Instructor;
import be.th.models.Lesson;
import be.th.models.LessonType;
import be.th.models.Location;
import be.th.models.Period;
import be.th.models.Skier;

public class SkierDAO extends DAO<Skier>{

	public SkierDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Skier skier) {
		String sqlPerson = """
	        INSERT INTO persons(
			    last_name,
			    first_name,
			    date_of_birth,
			    phone_number,
			    email,
			    city,
			    postcode,
			    street_name,
			    street_number
		    )
			VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

	    
	    String sqlSkier = """
    		INSERT INTO skiers(person_id)
    		VALUES(?)
		""";

	    try (
    		PreparedStatement pstmtPerson = connection.prepareStatement(sqlPerson, new String[] {"person_id"});
    		PreparedStatement pstmtSkier = connection.prepareStatement(sqlSkier);
		) {
	    	connection.setAutoCommit(false);
	    	
	        pstmtPerson.setString(1, DatabaseFormatter.format(skier.getLastName()));
	        pstmtPerson.setString(2, DatabaseFormatter.format(skier.getFirstName()));
	        pstmtPerson.setDate(3, Date.valueOf(skier.getDateOfBirth()));
	        pstmtPerson.setString(4, DatabaseFormatter.format(skier.getPhoneNumber()));
	        pstmtPerson.setString(5, DatabaseFormatter.format(skier.getEmail()));
	        pstmtPerson.setString(6, DatabaseFormatter.format(skier.getAddress().getCity()));
	        pstmtPerson.setString(7, DatabaseFormatter.format(skier.getAddress().getPostcode()));
	        pstmtPerson.setString(8, DatabaseFormatter.format(skier.getAddress().getStreetName()));
	        pstmtPerson.setString(9, DatabaseFormatter.format(skier.getAddress().getStreetNumber()));

	        int affectedRows = pstmtPerson.executeUpdate();
	        
	        if (affectedRows == 0) {
	            throw new SQLException("Creating skier failed, no rows affected.");
	        }

	        try (ResultSet generatedKeys = pstmtPerson.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                int personId = generatedKeys.getInt(1);

	                pstmtSkier.setInt(1, personId);

	                connection.commit();
	                return pstmtSkier.executeUpdate() > 0;
	            } else {
	                throw new SQLException("Creating skier failed, no ID obtained.");
	            }
	        }
	    } catch (SQLException e) {
	        DatabaseTransaction.rollbackTransaction(connection);
	        return false;
	    } finally {
            DatabaseTransaction.restoreAutoCommit(connection);
	    }
	}

	@Override
	public boolean delete(int id) {
	    String sqlGetPersonId = """
	        SELECT person_id 
	        FROM skiers 
	        WHERE skier_id = ?
	    """;

	    String sqlDeleteSkier = """
	        DELETE FROM skiers 
	        WHERE skier_id = ?
	    """;

	    String sqlDeletePerson = """
	        DELETE FROM persons 
	        WHERE person_id = ?
	    """;

	    String sqlDeleteBooking = """
	        DELETE FROM bookings 
	        WHERE skier_id = ?
	    """;

	    try {
	        connection.setAutoCommit(false);

	        int personId = getPersonId(id, sqlGetPersonId);
	        if (personId == -1) {
	            return false;
	        }

	        deleteBooking(id, sqlDeleteBooking);
	        if (
        		deleteSkier(id, sqlDeleteSkier) && 
        		deletePerson(personId, sqlDeletePerson)
            ) {
	            connection.commit();
	            return true;
	        } else {
	            connection.rollback();
	            return false;
	        }
	    } catch (SQLException e) {
	        DatabaseTransaction.rollbackTransaction(connection);
	        return false;
	    } finally {
	        DatabaseTransaction.restoreAutoCommit(connection);
	    }
	}

	@Override
	public boolean update(Skier skier) {
		String sqlUpdatePerson = """
	        UPDATE persons
	        SET last_name = ?, 
	            first_name = ?, 
	            date_of_birth = ?, 
	            phone_number = ?, 
	            email = ?, 
	            city = ?, 
	            postcode = ?, 
	            street_name = ?, 
	            street_number = ? 
			WHERE person_id = (SELECT person_id FROM skiers WHERE skier_id = ?)
        """;

		String sqlUpdateSkier = """
			UPDATE skiers 
		    SET skier_id = ? 
			WHERE skier_id = ?
		""";


	    try (
    		PreparedStatement pstmtUpdatePerson = connection.prepareStatement(sqlUpdatePerson);
    		PreparedStatement pstmtUpdateSkier = connection.prepareStatement(sqlUpdateSkier);
		) {
	        connection.setAutoCommit(false);

	        pstmtUpdatePerson.setString(1, DatabaseFormatter.format(skier.getLastName()));
	        pstmtUpdatePerson.setString(2, DatabaseFormatter.format(skier.getFirstName()));
	        pstmtUpdatePerson.setDate(3, java.sql.Date.valueOf(skier.getDateOfBirth()));
	        pstmtUpdatePerson.setString(4, DatabaseFormatter.format(skier.getPhoneNumber()));
	        pstmtUpdatePerson.setString(5, DatabaseFormatter.format(skier.getEmail()));
	        pstmtUpdatePerson.setString(6, DatabaseFormatter.format(skier.getAddress().getCity()));
	        pstmtUpdatePerson.setString(7, DatabaseFormatter.format(skier.getAddress().getPostcode()));
	        pstmtUpdatePerson.setString(8, DatabaseFormatter.format(skier.getAddress().getStreetName()));
	        pstmtUpdatePerson.setString(9, DatabaseFormatter.format(skier.getAddress().getStreetNumber()));
	        pstmtUpdatePerson.setInt(10, skier.getId());
	        pstmtUpdatePerson.executeUpdate();

	        pstmtUpdateSkier.setInt(1, skier.getId());
	        pstmtUpdateSkier.setInt(2, skier.getId());
	        
	        int affectedRows = pstmtUpdateSkier.executeUpdate();

	        if (affectedRows > 0) {
	            connection.commit();
	            return true;
	        }
	        
	        return false;
	    } catch (SQLException e) {
	        DatabaseTransaction.rollbackTransaction(connection);
	        return false;
	    } finally {
	        DatabaseTransaction.restoreAutoCommit(connection);
	    }
	}

	@Override
	public Skier find(int id) {
		String sql = """
			SELECT *
			FROM skiers 
			NATURAL JOIN persons 
			WHERE skier_id = ?
		""";


	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            return new Skier(
	                rs.getInt("skier_id"),
	                rs.getString("last_name"),
	                rs.getString("first_name"),
	                rs.getDate("date_of_birth").toLocalDate(),
	                rs.getString("city"),
	                rs.getString("postcode"),
	                rs.getString("street_name"),
	                rs.getString("street_number"),
	                rs.getString("phone_number"),
	                rs.getString("email")
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null;
	}


	@Override
	public List<Skier> findAll(Map<String, Object> criteria) {
		return null; // TODO
	}

	@Override
	public List<Skier> findAll() {
	    String sql = """
	        SELECT 
			    pe.*, 
			    b.*,
			    p.*,
			    l.*, l.start_date AS lesson_start_date, 
			    lt.*, lt.name AS lesson_type_name,
			    a.*,
			    loc.*, loc.name AS location_name,
			    i.instructor_id AS instructor_instructor_id,
			    per.last_name AS instructor_last_name,
			    per.first_name AS instructor_first_name,
			    per.date_of_birth AS instructor_date_of_birth,
			    per.phone_number AS instructor_phone_number,
			    per.email AS instructor_email,
			    per.city AS instructor_city,
			    per.postcode AS instructor_postcode,
			    per.street_number AS instructor_street_number,
			    per.street_name AS instructor_street_name,
                s.skier_id AS skier_skier_id
			FROM persons pe
			INNER JOIN skiers s ON s.person_id = pe.person_id
			LEFT JOIN bookings b ON b.skier_id = s.skier_id
			LEFT JOIN periods p ON b.period_id = p.period_id
			LEFT JOIN lessons l ON l.lesson_id = b.lesson_id
			LEFT JOIN lesson_types lt ON lt.lesson_type_id = l.lesson_type_id
			LEFT JOIN accreditations a ON a.accreditation_id = lt.accreditation_id
			LEFT JOIN locations loc ON loc.location_id = l.location_id
			LEFT JOIN instructors i ON i.instructor_id = l.instructor_id
			LEFT JOIN persons per ON per.person_id = i.person_id
			ORDER BY s.skier_id DESC
	    """;

	    List<Skier> skiers = new ArrayList<>();
	    Map<Integer, Skier> skierMap = new HashMap<>();

	    try (PreparedStatement stmt = connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            int skierId = rs.getInt("skier_skier_id");

	            Skier skier = skierMap.get(skierId);
	            if (skier == null) {
	                skier = new Skier(
	                    skierId,
	                    rs.getString("last_name"),
	                    rs.getString("first_name"),
	                    rs.getDate("date_of_birth").toLocalDate(),
	                    rs.getString("city"),
	                    rs.getString("postcode"),
	                    rs.getString("street_name"),
	                    rs.getString("street_number"),
	                    rs.getString("phone_number"),
	                    rs.getString("email")
	                );

	                skierMap.put(skierId, skier);
	                skiers.add(skier);
	            }

	            Booking booking = mapBooking(rs, skier);
	            if (booking != null) {
	                skier.addBooking(booking);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return skiers;
	}
	
	private int getPersonId(int skierId, String sql) throws SQLException {
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, skierId);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt("person_id");
	        }
	    }
	    
	    return -1; 
	}

	private void deleteBooking(int skierId, String sql) throws SQLException {
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, skierId);
	        pstmt.executeUpdate();
	    }
	}

	private boolean deleteSkier(int skierId, String sql) throws SQLException {
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, skierId);
	        return pstmt.executeUpdate() > 0;
	    }
	}

	private boolean deletePerson(int personId, String sql) throws SQLException {
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, personId);
	        return pstmt.executeUpdate() > 0;
	    }
	}

	private Booking mapBooking(ResultSet rs, Skier skier) throws SQLException {
	    if (rs.getInt("booking_id") == 0) {
	        return null;
	    }

	    Lesson lesson = mapLesson(rs);
	    return new Booking(
	        rs.getInt("booking_id"),
	        rs.getDate("booking_date").toLocalDate().atStartOfDay(),
	        rs.getBoolean("is_insured"),
	        rs.getInt("period_id"), 
			rs.getDate("start_date").toLocalDate(),
			rs.getDate("end_date").toLocalDate(),
			rs.getBoolean("is_vacation"),
			rs.getString("name"),
	        lesson,
	        skier
	    );
	}
	
	private LessonType mapLessonType(ResultSet rs) throws SQLException {
        if (rs.getInt("lesson_type_id") == 0) {
            return null;
        }
        
        Accreditation accreditation = new Accreditation(
            rs.getInt("accreditation_id"),
            rs.getString("sport"),
            rs.getString("age_category_name")
        );
        
        int maxAgeDatabase = rs.getInt("max_age");
        Optional<Integer> maxAge = rs.wasNull() ? Optional.empty() : Optional.of(maxAgeDatabase); 
        return new LessonType(
            rs.getInt("lesson_type_id"),
            rs.getString("skill_level"),
            rs.getDouble("price"),
            rs.getString("lesson_type_name"),
            rs.getString("age_category_name"),
            rs.getInt("min_age"),
            maxAge,
            rs.getInt("min_bookings"),
            rs.getInt("max_bookings"),
            accreditation
        );
    }

	private Lesson mapLesson(ResultSet rs) throws SQLException {
	    if (rs.getInt("lesson_id") == 0) {
	        return null;
	    }

	    LessonType lessonType = mapLessonType(rs);
	    Instructor instructor = mapInstructor(rs);
	    return new Lesson(
	        rs.getInt("lesson_id"),
	        rs.getTimestamp("lesson_start_date").toLocalDateTime(),
	        lessonType,
	        instructor,
	        rs.getInt("location_id"),
	        rs.getString("location_name")
	    );
	}

	private Instructor mapInstructor(ResultSet rs) throws SQLException {
	    if (rs.getInt("instructor_id") == 0) {
	        return null;
	    }

	    Integer instructorId = rs.getInt("instructor_id");
	    List<Accreditation> accreditations = fetchInstructorAccreditations(instructorId);
	    Instructor instructor = new Instructor(
    		instructorId,
	        rs.getString("instructor_last_name"),
	        rs.getString("instructor_first_name"),
	        rs.getDate("instructor_date_of_birth").toLocalDate(),
	        rs.getString("instructor_city"),
	        rs.getString("instructor_postcode"),
	        rs.getString("instructor_street_name"),
	        rs.getString("instructor_street_number"),
	        rs.getString("instructor_phone_number"),
	        rs.getString("instructor_email"),
	        new HashSet<>(accreditations)
	    );


	    return instructor;
	}

	private List<Accreditation> fetchInstructorAccreditations(int instructorId) {
	    String sql = """
	        SELECT *
	        FROM instructors 
	        NATURAL JOIN instructor_accreditation_details
	        NATURAL JOIN accreditations
	        WHERE instructor_id = ?
	    """;

	    List<Accreditation> accreditations = new ArrayList<>();

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, instructorId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Accreditation accreditation = new Accreditation(
	                    rs.getInt("accreditation_id"),
	                    rs.getString("sport"),
	                    rs.getString("age_category_name")
	                );
	                accreditations.add(accreditation);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return accreditations;
	}
}
