package be.th.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.th.models.Location;

public class LocationDAO extends DAO<Location>{
	public LocationDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Location obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Location obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Location find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Location> findAll() {
		String sql = """
			SELECT * 
			FROM locations
			ORDER BY 1	
		""";
		
		List<Location> locations = new ArrayList<>();
		
		try(ResultSet rs = connection.prepareStatement(sql).executeQuery()){
			while (rs.next()) {
				Location location = new Location(
					rs.getInt("location_id"),
					rs.getString("name")
				);
				
				locations.add(location);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
				
		return locations;
	}

	@Override
	public List<Location> findAll(Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return null;
	}
}
