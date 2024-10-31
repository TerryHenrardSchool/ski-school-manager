package be.th.ski_school_manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

public class SecretaryDAO extends DAO<Secretary>{

	public SecretaryDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Secretary obj) {
		return false; // TODO
	}

	@Override
	public boolean delete(Secretary obj) {
		return false; // TODO
	}

	@Override
	public boolean update(Secretary obj) {
		return false; // TODO
	}

	@Override
	public Secretary find(int id) {
        String query = "SELECT * FROM secretaries WHERE secretary_id = ?";
        try (PreparedStatement stmt = connect.prepareStatement(query)) {
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Secretary(
                    rs.getInt("secretary_id"),
                    rs.getString("last_name"),
                    rs.getString("first_name"),
                    rs.getDate("date_of_birth").toLocalDate(),
                    rs.getString("city"),
                    rs.getString("postcode"),
                    rs.getString("street_name"),
                    rs.getString("street_number"),
                    rs.getString("phone_number"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
        return null; 
    }



	@Override
	public List<Secretary> findAll(Map<String, Object> criteria) {
 		return null; // TODO
	}
}
