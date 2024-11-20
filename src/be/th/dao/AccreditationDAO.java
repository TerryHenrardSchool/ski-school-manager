package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.th.models.Accreditation;

public class AccreditationDAO extends DAO<Accreditation>{

	public AccreditationDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Accreditation obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Accreditation obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Accreditation find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Accreditation> findAll() {
	    String sql = """
	        SELECT * 
	        FROM accreditations
	        ORDER BY accreditation_id
	    """;
	    
	    List<Accreditation> accreditations = new ArrayList<>();
	    
	    try (PreparedStatement stmt = connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Accreditation accreditation = new Accreditation(
	                rs.getInt("accreditation_id"),
	                rs.getString("sport"),
	                rs.getString("age_category_name")
	            );
	            accreditations.add(accreditation);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return accreditations;
	}

	@Override
	public List<Accreditation> findAll(Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
