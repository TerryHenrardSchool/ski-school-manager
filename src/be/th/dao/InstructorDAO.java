package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.th.formatters.DatabaseFormatter;
import be.th.models.Accreditation;
import be.th.models.Instructor;

public class InstructorDAO extends DAO<Instructor> {

	public InstructorDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Instructor instructor) {
	    String sqlInstructor = """
	        INSERT INTO instructors (
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

	    String sqlAccreditation = """
	        INSERT INTO instructor_accreditation_details (instructor_id, accreditation_id)
	        VALUES (?, ?)
	    """;

	    try {
	        connection.setAutoCommit(false);

	        int instructorId = insertInstructor(sqlInstructor, instructor);
	        insertAccreditations(sqlAccreditation, instructorId, instructor.getAccreditations());

	        connection.commit();
	        
	        return true;
	    } catch (SQLException e) {
	        try {
	            connection.rollback();
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        
	        e.printStackTrace();
	        
	        return false;
	    } finally {
	        try {
	            connection.setAutoCommit(true);
	        } catch (SQLException autoCommitEx) {
	            autoCommitEx.printStackTrace();
	        }
	    }
	}


	@Override
	public boolean delete(int id) {
		String sqlInstructorAccreditationDetails = """
			DELETE FROM instructor_accreditation_details
			WHERE instructor_id = ?
		""";
		
		String sqlInstructor = """
		    DELETE FROM instructors 
		    WHERE instructor_id = ?
	    """;
	    
		try {
	        connection.setAutoCommit(false);

			deleteInstructorAccreditationDetails(sqlInstructorAccreditationDetails, id);
			deleteInstructor(sqlInstructor, id);
			
	        connection.commit();
	        return true;
		} catch (SQLException e) {
			try {
	            connection.rollback();
	        } catch (SQLException rollbackEx) {
	            rollbackEx.printStackTrace();
	        }
	        
	        e.printStackTrace();
	        
	        return false;
		} finally {
	        try {
	            connection.setAutoCommit(true);
	        } catch (SQLException autoCommitEx) {
	            autoCommitEx.printStackTrace();
	        }
	    }
	}

	@Override
	public boolean update(Instructor instructor) {
		String sql = """
		    UPDATE instructors
		    SET last_name = ?, 
		        first_name = ?, 
		        date_of_birth = ?, 
		        phone_number = ?, 
		        email = ?, 
		        city = ?, 
		        postcode = ?, 
		        street_name = ?, 
		        street_number = ?
		    WHERE instructor_id = ?
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
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Instructor find(int id) {
	    String sql = """
	        SELECT * 
	        FROM instructors 
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
	                rs.getString("age_category_name"),
	                rs.getInt("min_age"),
	                rs.getInt("max_age")
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

	@Override
	public List<Instructor> findAll() {
	    String sql = """
	        SELECT * 
	        FROM instructors 
	        NATURAL JOIN instructor_accreditation_details 
	        NATURAL JOIN accreditations 
	        ORDER BY instructor_id DESC
	    """;
	    
	    List<Instructor> instructors = new ArrayList<>();
	    Map<Integer, Instructor> instructorMap = new HashMap<>();
	    
	    try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
	        while (rs.next()) {
	            int instructorId = rs.getInt("instructor_id");

	            Accreditation accreditation = new Accreditation(
	                rs.getInt("accreditation_id"),
	                rs.getString("sport"),
	                rs.getString("age_category_name"),
	                rs.getInt("min_age"),
	                rs.getInt("max_age")
	            );

	            Instructor instructor = instructorMap.get(instructorId);
	            if (instructor == null) {
	                instructor = new Instructor(
	                    instructorId,
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
	                instructorMap.put(instructorId, instructor);
	                instructors.add(instructor);
	            } else {
	                instructor.addAccreditation(accreditation);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return instructors;
	}

	@Override
	public List<Instructor> findAll(Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	private int insertInstructor(String sqlInstructor, Instructor instructor) throws SQLException {
	    try (PreparedStatement pstmtInstructor = connection.prepareStatement(sqlInstructor)) {
	    	pstmtInstructor.setString(1, DatabaseFormatter.format(instructor.getLastName()));
	    	pstmtInstructor.setString(2, DatabaseFormatter.format(instructor.getFirstName()));
	    	pstmtInstructor.setDate(3, java.sql.Date.valueOf(instructor.getDateOfBirth()));
	    	pstmtInstructor.setString(4, DatabaseFormatter.format(instructor.getPhoneNumber()));
	    	pstmtInstructor.setString(5, DatabaseFormatter.format(instructor.getEmail()));
	    	pstmtInstructor.setString(6, DatabaseFormatter.format(instructor.getAddress().getCity()));
	    	pstmtInstructor.setString(7, DatabaseFormatter.format(instructor.getAddress().getPostcode()));
	    	pstmtInstructor.setString(8, DatabaseFormatter.format(instructor.getAddress().getStreetName()));
	    	pstmtInstructor.setString(9, DatabaseFormatter.format(instructor.getAddress().getStreetNumber()));

	        int affectedRows = pstmtInstructor.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Creating instructor failed, no rows affected.");
	        }

	        return DatabaseSequence.getCurrentSequenceValue(connection, "instructors_seq");
	    } catch (SQLException ex) {
	    	throw new SQLException("Error while inserting instructor. Error: " + ex.getMessage());
	    }
	}

	private void insertAccreditations(String sqlAccreditation, int instructorId, Set<Accreditation> accreditations) throws SQLException {
	    try (PreparedStatement pstmtAccreditation = connection.prepareStatement(sqlAccreditation)) {
	        for (Accreditation accreditation : accreditations) {
	            pstmtAccreditation.setInt(1, instructorId);
	            pstmtAccreditation.setInt(2, accreditation.getId());
	            pstmtAccreditation.addBatch();
	        }
	        pstmtAccreditation.executeBatch();
	    } catch (SQLException ex) {
	    	throw new SQLException("Error while inserting instructor's accreditations.");
	    }
	}
	
	private void deleteInstructorAccreditationDetails(String sql, int instructorId) throws SQLException {
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, instructorId);
	        
	        int affectedRows = pstmt.executeUpdate();
	        
	        if (affectedRows == 0) {
	            throw new SQLException("No accreditation details found for instructor with ID: " + instructorId);
	        }
	    } catch (SQLException ex) {
	        throw new SQLException("Error while deleting instructor accreditation details. Error: " + ex.getMessage());
	    }
	}

	private void deleteInstructor(String sql, int instructorId) throws SQLException {
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, instructorId);
	        
	        int affectedRows = pstmt.executeUpdate();
	        
	        if (affectedRows == 0) {
	            throw new SQLException("No instructor found with ID: " + instructorId);
	        }
	    } catch (SQLException ex) {
	        throw new SQLException("Error while deleting instructor. Error: " + ex.getMessage(), ex);
	    }
	}

}
