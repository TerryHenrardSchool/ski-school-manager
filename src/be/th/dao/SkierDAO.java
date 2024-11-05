package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.th.formatters.DatabaseFormatter;
import be.th.models.Skier;

public class SkierDAO extends DAO<Skier>{

	public SkierDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Skier skier) {
	    String sql = "INSERT INTO skiers (" + 
	    			 	"last_name, " + 
	    			 	"first_name, " + 
	    			 	"date_of_birth, " + 
	    			 	"phone_number, " + 
	    			 	"email, " + 
	    			 	"city, " + 
	    			 	"postcode, " + 
	    			 	"street_name, " + 
	    			 	"street_number" +
    			 	 ") " + 
	    			 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	    	pstmt.setString(1, DatabaseFormatter.format(skier.getLastName()));
	        pstmt.setString(2, DatabaseFormatter.format(skier.getFirstName()));
	        pstmt.setDate(3, java.sql.Date.valueOf(skier.getDateOfBirth()));
	        pstmt.setString(4, DatabaseFormatter.format(skier.getPhoneNumber()));
	        pstmt.setString(5, DatabaseFormatter.format(skier.getEmail()));
	        pstmt.setString(6, DatabaseFormatter.format(skier.getAddress().getCity()));
	        pstmt.setString(7, DatabaseFormatter.format(skier.getAddress().getPostcode()));
	        pstmt.setString(8, DatabaseFormatter.format(skier.getAddress().getStreetName()));
	        pstmt.setString(9, DatabaseFormatter.format(skier.getAddress().getStreetNumber()));
	        pstmt.setInt(10, skier.getId());  
	        
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public boolean delete(int id) {
	    String sql = "DELETE FROM skiers WHERE skier_id = ?";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setInt(1, id);
	        
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	@Override
	public boolean update(Skier skier) {
	    String sql = "UPDATE skiers " + 
    				 "SET " + 
    				 	"last_name = ?, " +
				 		"first_name = ?, " + 
    				 	"date_of_birth = ?, " +
				 		"phone_number = ?, " + 
    				 	"email = ?, " +
    				 	"city = ?, " + 
    				 	"postcode = ?, " + 
    				 	"street_name = ?, " + 
    				 	"street_number = ? " + 
				 	 "WHERE skier_id = ?";	
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, DatabaseFormatter.format(skier.getLastName()));
	        pstmt.setString(2, DatabaseFormatter.format(skier.getFirstName()));
	        pstmt.setDate(3, java.sql.Date.valueOf(skier.getDateOfBirth()));
	        pstmt.setString(4, DatabaseFormatter.format(skier.getPhoneNumber()));
	        pstmt.setString(5, DatabaseFormatter.format(skier.getEmail()));
	        pstmt.setString(6, DatabaseFormatter.format(skier.getAddress().getCity()));
	        pstmt.setString(7, DatabaseFormatter.format(skier.getAddress().getPostcode()));
	        pstmt.setString(8, DatabaseFormatter.format(skier.getAddress().getStreetName()));
	        pstmt.setString(9, DatabaseFormatter.format(skier.getAddress().getStreetNumber()));
	        pstmt.setInt(10, skier.getId());  
	        	        	        
	        return pstmt.executeUpdate() > 0;
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    	return false;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return false;	    	
	    }
	}

	@Override
	public Skier find(int id) {
	    String sql = "SELECT * FROM skiers WHERE skier_id = ?";
	    
	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, id);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
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
	    String sql = "SELECT * FROM skiers ORDER BY skier_id DESC";
	    List<Skier> skiers = new ArrayList<>();
	    
	    try (ResultSet rs = connection.prepareStatement(sql).executeQuery()) {
	        while (rs.next()) {
	            Skier skier = new Skier(
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
	            
	            skiers.add(skier);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return skiers;
	}

}
