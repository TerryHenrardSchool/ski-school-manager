package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.th.models.Instructor;

public class InstructorDAO extends DAO<Instructor> {

	public InstructorDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Instructor instructor) {
		String sql = 
			"INSERT INTO instructors (last_name, first_name, date_of_birth, phone_number, email, city, postcode, street_name, street_number) " + 
	 		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement pstmt = super.connection.prepareStatement(sql)) {
			pstmt.setString(1, instructor.getLastName());
			pstmt.setString(2, instructor.getFirstName());
			pstmt.setDate(3, java.sql.Date.valueOf(instructor.getDateOfBirth()));
			pstmt.setString(4, instructor.getPhoneNumber());
			pstmt.setString(5, instructor.getEmail());
			pstmt.setString(6, instructor.getAddress().getCity());
			pstmt.setString(7, instructor.getAddress().getPostcode());
			pstmt.setString(8, instructor.getAddress().getStreetName());
			pstmt.setString(9, instructor.getAddress().getStreetNumber());
       
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean delete(int id) {
		String sql = "DELETE FROM instructors WHERE instructor_id = ?";
	    
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
	        
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean update(Instructor instructor) {
		String sql = 
			"UPDATE instructors " + 
			"SET last_name = ?, first_name = ?, date_of_birth = ?, phone_number = ?, email = ?, " +
			"city = ?, postcode = ?, street_name = ?, street_number = ? " +
			"WHERE instructor_id = ?";
	    
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, instructor.getLastName());
			pstmt.setString(2, instructor.getFirstName());
			pstmt.setDate(3, java.sql.Date.valueOf(instructor.getDateOfBirth()));
			pstmt.setString(4, instructor.getPhoneNumber());
			pstmt.setString(5, instructor.getEmail());
			pstmt.setString(6, instructor.getAddress().getCity());
			pstmt.setString(7, instructor.getAddress().getPostcode());
			pstmt.setString(8, instructor.getAddress().getStreetName());
			pstmt.setString(9, instructor.getAddress().getStreetNumber());
			pstmt.setInt(10, instructor.getId());
	        
			return pstmt.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Instructor find(int id) {
		String sql = "SELECT * FROM instructors WHERE instructor_id = ?";
	    
		try (PreparedStatement stmt = connection.prepareStatement(sql)) {
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			if (rs.next()) {
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
					rs.getString("email")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
		return null;
	}

	@Override
	public List<Instructor> findAll() {
		String sql = "SELECT * FROM instructors ORDER BY instructor_id DESC";
		List<Instructor> instructors = new ArrayList<>();
	    
		try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
			while (rs.next()) {
				Instructor instructor = new Instructor(
					rs.getInt("instructor_id"),
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
	            
				instructors.add(instructor);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	    
		return instructors;
	}

	@Override
	public List<Instructor> findAll(Map<String, Object> criteria) {
		// Implementation can be done as needed based on specific criteria
		return null; 
	}
}
