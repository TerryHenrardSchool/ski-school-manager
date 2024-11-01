package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import be.th.classes.Skier;

public class SkierDAO extends DAO<Skier>{

	public SkierDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Skier skier) {
	    String sql = "INSERT INTO skiers (last_name, first_name, date_of_birth, phone_number, email, city, postcode, street_name, street_number) " + 
	    			 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    try (PreparedStatement pstmt = super.connection.prepareStatement(sql)) {
	        pstmt.setString(1, skier.getLastName());
	        pstmt.setString(2, skier.getFirstName());
	        pstmt.setDate(3, java.sql.Date.valueOf(skier.getDateOfBirth()));
	        pstmt.setString(4, skier.getPhoneNumber());
	        pstmt.setString(5, skier.getEmail());
	        pstmt.setString(6, skier.getCity());
	        pstmt.setString(7, skier.getPostcode());
	        pstmt.setString(8, skier.getStreetName());
	        pstmt.setString(9, skier.getStreetNumber());
	        
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	@Override
	public boolean delete(Skier obj) {
		return false; // TODO
	}

	@Override
	public boolean update(Skier obj) {
		return false; // TODO
	}

	@Override
	public Skier find(int id) {
		return null; // TODO
	}

	@Override
	public List<Skier> findAll(Map<String, Object> criteria) {
		return null; // TODO
	}

	@Override
	public List<Skier> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
