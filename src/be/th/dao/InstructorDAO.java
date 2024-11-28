package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import be.th.formatters.DatabaseFormatter;
import be.th.models.Accreditation;
import be.th.models.Booking;
import be.th.models.Instructor;
import be.th.models.Lesson;
import be.th.models.LessonType;
import be.th.models.Skier;
import be.th.validators.IntegerValidator;

public class InstructorDAO extends DAO<Instructor> {

	public InstructorDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Instructor instructor) {
	    try {
	        connection.setAutoCommit(false);

	        int personId = insertPerson(instructor);
	        int instructorId = insertInstructor(personId);
	        insertAccreditations(instructorId, instructor.getAccreditations());
	        
	        connection.commit();
	        
	        return true;
	    } catch (SQLException e) {
            DatabaseTransaction.rollback(connection);
	        e.printStackTrace();
	        return false;
	    } finally {
            DatabaseTransaction.restoreAutoCommit(connection);
	    }
	}


	@Override
	public boolean delete(int id) {
		try {
	        connection.setAutoCommit(false);

			deleteInstructorAccreditationDetails(id);
			deleteInstructor(id);
			
	        connection.commit();
	        return true;
		} catch (SQLException e) {
            DatabaseTransaction.rollback(connection);
	        e.printStackTrace();
	        return false;
		} finally {
            DatabaseTransaction.restoreAutoCommit(connection);
	    }
	}

	@Override
	public boolean update(Instructor instructor) {
	    try {
	        connection.setAutoCommit(false);

	        boolean isUpdated = updateInstructorInfo(instructor);
	        if (!isUpdated) {
	            DatabaseTransaction.rollback(connection);
	            return false;
	        }

	        Set<Integer> currentAccreditations = getCurrentAccreditations(instructor.getId());
	        Set<Integer> newAccreditations = instructor.getAccreditations().stream()
                .map(Accreditation::getId)
                .collect(Collectors.toSet());

	        deleteObsoleteAccreditations(instructor.getId(), currentAccreditations, newAccreditations);
	        insertNewAccreditations(instructor.getId(), currentAccreditations, newAccreditations);

	        connection.commit();
	        return true;
	    } catch (SQLException e) {
            DatabaseTransaction.rollback(connection);
	        return false;
	    } finally {
            DatabaseTransaction.restoreAutoCommit(connection);
	    }
	}


	@Override
	public Instructor find(int id) {
	    String sql = """
	        SELECT * 
	        FROM persons
	        NATURAL JOIN instructors 
	        NATURAL JOIN instructor_accreditation_details 
	        NATURAL JOIN accreditations 
	        WHERE instructor_id = ?
	    """;

	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, id);
	        
	        ResultSet rs = stmt.executeQuery();
	        
	        Instructor instructor = null;
	        
	        while (rs.next()) {
	            Accreditation accreditation = new Accreditation(
	                rs.getInt("accreditation_id"),
	                rs.getString("sport"),
	                rs.getString("age_category_name")
	            );
	            
	            if (instructor == null) {
	                instructor = new Instructor(
	                    rs.getInt("instructor_id"),
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
	            } else {
	                instructor.addAccreditation(accreditation);
	            }
	        }
	        
	        return instructor;
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return null;
	}

	public List<Instructor> findAll() {
	    String sql = """
	        SELECT * 
	        FROM persons
	        NATURAL JOIN instructors 
	        NATURAL JOIN instructor_accreditation_details 
	        NATURAL JOIN accreditations 
	        ORDER BY instructor_id DESC
	    """;

	    Map<Integer, Instructor> instructorMap = new HashMap<>();

	    try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
	        while (rs.next()) {
	            int instructorId = rs.getInt("instructor_id");
	            Accreditation accreditation = mapAccreditation(rs);

	            Instructor instructor = instructorMap.get(instructorId);
	            if (instructor == null) {
	                instructor = mapInstructor(rs, accreditation);
	                loadInstructorLessons(instructor);
	                instructorMap.put(instructorId, instructor);
	            } else {
	                instructor.addAccreditation(accreditation);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return instructorMap.values().stream().toList();
	}

	@Override
	public List<Instructor> findAll(Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Accreditation mapAccreditation(ResultSet rs) throws SQLException {
	    return new Accreditation(
	        rs.getInt("accreditation_id"),
	        rs.getString("sport"),
	        rs.getString("age_category_name")
	    );
	}

	private Instructor mapInstructor(ResultSet rs, Accreditation accreditation) throws SQLException {
	    return new Instructor(
	        rs.getInt("instructor_id"),
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
	}


	private void loadInstructorLessons(Instructor instructor) throws SQLException {
	    String sql = """
	        SELECT
	            l.*,
	            lt.*,
	            a.*,
	            loc.location_id AS location_id_1, loc.name AS name_1
	        FROM
	            lessons l
	        INNER JOIN
	            lesson_types lt ON lt.lesson_type_id = l.lesson_type_id
	        INNER JOIN
	            accreditations a ON a.accreditation_id = lt.accreditation_id
	        INNER JOIN
	            locations loc ON loc.location_id = l.location_id
	        WHERE
	            instructor_id = ?
	    """;
	
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, instructor.getId());
	        ResultSet rs2 = pstmt.executeQuery();
	
	        while (rs2.next()) {
	            Lesson lesson = mapLesson(rs2, instructor);
	            loadLessonBookings(lesson);
	            instructor.addLesson(lesson);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
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
	        ResultSet rs3 = pstmt.executeQuery();
	
	        while (rs3.next()) {
	        	Booking booking = new Booking(
        			rs3.getInt("booking_id"),
        			rs3.getTimestamp("booking_date").toLocalDateTime(),
        			rs3.getBoolean("is_insured"),
        			rs3.getInt("period_id"),
        			rs3.getDate("start_date").toLocalDate(),
        			rs3.getDate("end_date").toLocalDate(),
        			rs3.getBoolean("is_vacation"),
        			rs3.getString("name"),
        			lesson,
        			mapSkier(rs3.getInt("skier_id_1"))
	            );
	        		
	            lesson.addBooking(booking);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private Lesson mapLesson(ResultSet rs, Instructor instructor) throws SQLException {
	    int maxAge = rs.getInt("max_age");
	    Optional<Integer> maxAgeOptional = rs.wasNull() ? Optional.empty() : Optional.of(maxAge);

	    LessonType lessonType = new LessonType(
	        rs.getInt("lesson_type_id"),
	        rs.getString("skill_level"),
	        rs.getDouble("price"),
	        rs.getString("name"),
	        rs.getString("age_category_name"),
	        rs.getInt("min_age"),
	        maxAgeOptional,
	        rs.getInt("min_bookings"),
	        rs.getInt("max_bookings"),
	        rs.getBoolean("is_private"),
	        new Accreditation(
	            rs.getInt("accreditation_id"),
	            rs.getString("sport"),
	            rs.getString("age_category_name")
	        )
	    );

	    return new Lesson(
	        rs.getInt("lesson_id"),
	        rs.getTimestamp("start_date").toLocalDateTime(),
	        lessonType,
	        instructor,
	        rs.getInt("location_id_1"),
	        rs.getString("name_1")
	    );
	}

	private int insertPerson(Instructor instructor) throws SQLException {
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
	        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
	    """;
		
	    try (PreparedStatement pstmtPerson = connection.prepareStatement(sqlPerson, new String[] {"person_id"})) {
	    	pstmtPerson.setString(1, DatabaseFormatter.format(instructor.getLastName()));
	    	pstmtPerson.setString(2, DatabaseFormatter.format(instructor.getFirstName()));
	    	pstmtPerson.setDate(3, java.sql.Date.valueOf(instructor.getDateOfBirth()));
	    	pstmtPerson.setString(4, DatabaseFormatter.format(instructor.getPhoneNumber()));
	    	pstmtPerson.setString(5, DatabaseFormatter.format(instructor.getEmail()));
	    	pstmtPerson.setString(6, DatabaseFormatter.format(instructor.getAddress().getCity()));
	    	pstmtPerson.setString(7, DatabaseFormatter.format(instructor.getAddress().getPostcode()));
	    	pstmtPerson.setString(8, DatabaseFormatter.format(instructor.getAddress().getStreetName()));
	    	pstmtPerson.setString(9, DatabaseFormatter.format(instructor.getAddress().getStreetNumber()));

	        int affectedRows = pstmtPerson.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Creating instructor failed, no rows affected.");
	        }
	        
	        try (ResultSet generatedKeys = pstmtPerson.getGeneratedKeys()){
	        	if(generatedKeys.next()) {
	        		return generatedKeys.getInt(1);
	        	}
	        	
	        	return -1;
	        }
	    } catch (SQLException ex) {
	    	throw new SQLException("Error while inserting person. Error: " + ex.getMessage());
	    }
	}
	
	private int insertInstructor(int personId) throws SQLException {
		String sqlInstructor = """
    		INSERT INTO instructors (person_id)
    		VALUES (?)
		""";
		
		try (PreparedStatement pstmtInstructor = connection.prepareStatement(sqlInstructor, new String[] {"instructor_id"})) {
			pstmtInstructor.setInt(1, personId);
			
			int affectedRows = pstmtInstructor.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Creating instructor failed, no rows affected.");
	        }
			
			try (ResultSet generatedKeys = pstmtInstructor.getGeneratedKeys()){
	        	if(generatedKeys.next()) {
	        		return generatedKeys.getInt(1);
	        	}
	        	
	        	return -1;
	        }
		} catch (SQLException ex) {
	    	throw new SQLException("Error while inserting instructor. Error: " + ex.getMessage());
		}
	}

	private void insertAccreditations(int instructorId, Set<Accreditation> accreditations) throws SQLException {
		String sqlAccreditation = """
	        INSERT INTO instructor_accreditation_details(
			    instructor_id, 
			    accreditation_id
		    )
	        VALUES (?, ?)
	    """;
		
	    try (PreparedStatement pstmtAccreditation = connection.prepareStatement(sqlAccreditation)) {
	        for (Accreditation accreditation : accreditations) {
	            pstmtAccreditation.setInt(1, instructorId);
	            pstmtAccreditation.setInt(2, accreditation.getId());
	            pstmtAccreditation.addBatch();
	        }
	        
	        pstmtAccreditation.executeBatch();
	    } catch (SQLException ex) {
	    	throw new SQLException("Error while inserting instructor's accreditations. Error: " + ex.getMessage());
	    }
	}
	
	private void deleteInstructorAccreditationDetails(int instructorId) throws SQLException {
		String sqlInstructorAccreditationDetails = """
			DELETE FROM instructor_accreditation_details
			WHERE instructor_id = ?
		""";
		
	    try (PreparedStatement pstmt = connection.prepareStatement(sqlInstructorAccreditationDetails)) {
	        pstmt.setInt(1, instructorId);
	        
	        int affectedRows = pstmt.executeUpdate();
	        
	        if (affectedRows == 0) {
	            throw new SQLException("No accreditation details found for instructor with ID: " + instructorId);
	        }
	    } catch (SQLException ex) {
	        throw new SQLException("Error while deleting instructor accreditation details. Error: " + ex.getMessage());
	    }
	}

	private void deleteInstructor(int instructorId) throws SQLException {
		String sqlInstructor = """
		    DELETE FROM instructors 
		    WHERE instructor_id = ?
	    """;
		
	    try (PreparedStatement pstmt = connection.prepareStatement(sqlInstructor)) {
	        pstmt.setInt(1, instructorId);
	        
	        int affectedRows = pstmt.executeUpdate();
	        
	        if (affectedRows == 0) {
	            throw new SQLException("No instructor found with ID: " + instructorId);
	        }
	    } catch (SQLException ex) {
	        throw new SQLException("Error while deleting instructor. Error: " + ex.getMessage(), ex);
	    }
	}
	
	private Skier mapSkier(int id) throws SQLException {
	    return Skier.findInDatabaseById(id, (SkierDAO) new DAOFactory().getSkierDAO());
	}

	private boolean updateInstructorInfo(Instructor instructor) {
	    String sql = """
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
	        WHERE person_id = (SELECT person_id FROM instructors WHERE instructor_id = ?)
	    """;

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, DatabaseFormatter.format(instructor.getLastName()));
	        pstmt.setString(2, DatabaseFormatter.format(instructor.getFirstName()));
	        pstmt.setDate(3, java.sql.Date.valueOf(instructor.getDateOfBirth()));
	        pstmt.setString(4, DatabaseFormatter.format(instructor.getPhoneNumber()));
	        pstmt.setString(5, DatabaseFormatter.format(instructor.getEmail()));
	        pstmt.setString(6, DatabaseFormatter.format(instructor.getAddress().getCity()));
	        pstmt.setString(7, DatabaseFormatter.format(instructor.getAddress().getPostcode()));
	        pstmt.setString(8, DatabaseFormatter.format(instructor.getAddress().getStreetName()));
	        pstmt.setString(9, DatabaseFormatter.format(instructor.getAddress().getStreetNumber()));
	        pstmt.setInt(10, instructor.getId());

	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        return false;
	    }
	}

	private Set<Integer> getCurrentAccreditations(int instructorId) throws SQLException {
	    String selectSql = """		
    		SELECT accreditation_id 
    		FROM instructor_accreditation_details 
    		WHERE instructor_id = ?
		""";
	    
	    Set<Integer> accreditations = new HashSet<>();

	    try (PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {
	        selectStmt.setInt(1, instructorId);
	        ResultSet rs = selectStmt.executeQuery();
	        
	        while (rs.next()) {
	            accreditations.add(rs.getInt("accreditation_id"));
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error while getting current accreditations. Error: " + e.getMessage());
	    }
	    return accreditations;
	}

	private void deleteObsoleteAccreditations(int instructorId, Set<Integer> currentAccreditations, Set<Integer> newAccreditations) throws SQLException {
	    String deleteSql = """
    		DELETE FROM instructor_accreditation_details 
    		WHERE instructor_id = ? AND accreditation_id = ?
		""";

	    try (PreparedStatement deleteStmt = connection.prepareStatement(deleteSql)) {
	        for (Integer currentAccreditation : currentAccreditations) {
	            if (!newAccreditations.contains(currentAccreditation)) {
	                deleteStmt.setInt(1, instructorId);
	                deleteStmt.setInt(2, currentAccreditation);
	                deleteStmt.executeUpdate();
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error while deleting obslete accreditations. Error: " + e.getMessage());
	    }
	}

	private void insertNewAccreditations(
		int instructorId, 
		Set<Integer> currentAccreditations, 
		Set<Integer> newAccreditations
	) 
		throws SQLException 
	{
	    String insertSql = """
    		INSERT INTO instructor_accreditation_details 
    		(
	    		instructor_id, 
	    		accreditation_id
    		) 
    		VALUES (?, ?)
		""";

	    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
	        for (Integer newAccreditation : newAccreditations) {
	            if (!currentAccreditations.contains(newAccreditation)) {
	                insertStmt.setInt(1, instructorId);
	                insertStmt.setInt(2, newAccreditation);
	                insertStmt.executeUpdate();
	            }
	        }
	    } catch (SQLException e) {
	        throw new SQLException("Error while inserting new accreditations. Error: " + e.getMessage());
	    }
	}
}