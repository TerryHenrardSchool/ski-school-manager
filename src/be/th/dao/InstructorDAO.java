package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
		return false; // TODO
	}

	@Override
	public boolean update(Instructor instructor) {
		return false; // TODO
	}

	@Override
	public Instructor find(int id) {
		return null; // TODO
	}

	@Override
	public List<Instructor> findAll(Map<String, Object> criteria) {
 		return null; // TODO
	}

	@Override
	public List<Instructor> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
