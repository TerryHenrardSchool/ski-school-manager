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
import java.util.Set;
import java.util.stream.Collectors;

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
            DatabaseTransaction.rollbackTransaction(connection);
	        e.printStackTrace();
	        return false;
	    } finally {
            DatabaseTransaction.restoreAutoCommit(connection);
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
            DatabaseTransaction.rollbackTransaction(connection);
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
	            DatabaseTransaction.rollbackTransaction(connection);
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
            DatabaseTransaction.rollbackTransaction(connection);
	        e.printStackTrace();
	        return false;
	    } finally {
            DatabaseTransaction.restoreAutoCommit(connection);
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
	                rs.getString("age_category_name")
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

	private boolean updateInstructorInfo(Instructor instructor) {
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
	        return false;
	    }
	}

	private Set<Integer> getCurrentAccreditations(int instructorId) throws SQLException {
	    String selectSql = "SELECT accreditation_id FROM instructor_accreditation_details WHERE instructor_id = ?";
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
	    String deleteSql = "DELETE FROM instructor_accreditation_details WHERE instructor_id = ? AND accreditation_id = ?";

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

	private void insertNewAccreditations(int instructorId, Set<Integer> currentAccreditations, Set<Integer> newAccreditations) throws SQLException {
	    String insertSql = "INSERT INTO instructor_accreditation_details (instructor_id, accreditation_id) VALUES (?, ?)";

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
