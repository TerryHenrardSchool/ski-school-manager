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
		String sqlPerson = """
	        INSERT INTO persons
		    (
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
			VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

	    
	    String sqlSkier = """
    		INSERT INTO skiers(person_id)
    		VALUES(?)
		""";

	    try (
    		PreparedStatement pstmtPerson = connection.prepareStatement(sqlPerson, new String[] {"person_id"});
    		PreparedStatement pstmtSkier = connection.prepareStatement(sqlSkier);
		) {
	    	connection.setAutoCommit(false);
	    	
	        pstmtPerson.setString(1, DatabaseFormatter.format(skier.getLastName()));
	        pstmtPerson.setString(2, DatabaseFormatter.format(skier.getFirstName()));
	        pstmtPerson.setDate(3, java.sql.Date.valueOf(skier.getDateOfBirth()));
	        pstmtPerson.setString(4, DatabaseFormatter.format(skier.getPhoneNumber()));
	        pstmtPerson.setString(5, DatabaseFormatter.format(skier.getEmail()));
	        pstmtPerson.setString(6, DatabaseFormatter.format(skier.getAddress().getCity()));
	        pstmtPerson.setString(7, DatabaseFormatter.format(skier.getAddress().getPostcode()));
	        pstmtPerson.setString(8, DatabaseFormatter.format(skier.getAddress().getStreetName()));
	        pstmtPerson.setString(9, DatabaseFormatter.format(skier.getAddress().getStreetNumber()));

	        int affectedRows = pstmtPerson.executeUpdate();
	        
	        if (affectedRows == 0) {
	            throw new SQLException("Creating person failed, no rows affected.");
	        }

	        try (ResultSet generatedKeys = pstmtPerson.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                int personId = generatedKeys.getInt(1);

	                pstmtSkier.setInt(1, personId);

	                connection.commit();
	                return pstmtSkier.executeUpdate() > 0;
	            } else {
	                throw new SQLException("Creating person failed, no ID obtained.");
	            }
	        }
	    } catch (SQLException e) {
	        DatabaseTransaction.rollbackTransaction(connection);
	        return false;
	    } finally {
            DatabaseTransaction.restoreAutoCommit(connection);
	    }
	}

	@Override
	public boolean delete(int id) {
		String sqlGetPersonId = """
		    SELECT person_id 
		    FROM skiers 
		    WHERE skier_id = ?
		""";

		String sqlDeleteSkier = """
		    DELETE FROM skiers 
		    WHERE skier_id = ?
		""";

		String sqlDeletePerson = """
		    DELETE FROM persons 
		    WHERE person_id = ?
		""";
		
	    try (
	        PreparedStatement pstmtGetPersonId = connection.prepareStatement(sqlGetPersonId);
	        PreparedStatement pstmtDeleteSkier = connection.prepareStatement(sqlDeleteSkier);
	        PreparedStatement pstmtDeletePerson = connection.prepareStatement(sqlDeletePerson);
	    ) {
	        connection.setAutoCommit(false);

	        pstmtGetPersonId.setInt(1, id);
	        ResultSet rs = pstmtGetPersonId.executeQuery();

	        if (rs.next()) {
	            int personId = rs.getInt("person_id");

	            pstmtDeleteSkier.setInt(1, id);
	            int affectedRowsSkier = pstmtDeleteSkier.executeUpdate();

	            if (affectedRowsSkier > 0) {
	                pstmtDeletePerson.setInt(1, personId);
	                pstmtDeletePerson.executeUpdate();

	                connection.commit();
	                return true;
	            }
	        }

	        return false;
	    } catch (SQLException e) {
	        DatabaseTransaction.rollbackTransaction(connection);
	        return false;
	    } finally {
	        DatabaseTransaction.restoreAutoCommit(connection);
	    }
	}


	@Override
	public boolean update(Skier skier) {
		String sqlUpdatePerson = """
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
			WHERE person_id = (SELECT person_id FROM skiers WHERE skier_id = ?)
        """;

		String sqlUpdateSkier = """
			UPDATE skiers 
		    SET skier_id = ? 
			WHERE skier_id = ?
		""";


	    try (
    		PreparedStatement pstmtUpdatePerson = connection.prepareStatement(sqlUpdatePerson);
    		PreparedStatement pstmtUpdateSkier = connection.prepareStatement(sqlUpdateSkier);
		) {
	        connection.setAutoCommit(false);

	        pstmtUpdatePerson.setString(1, DatabaseFormatter.format(skier.getLastName()));
	        pstmtUpdatePerson.setString(2, DatabaseFormatter.format(skier.getFirstName()));
	        pstmtUpdatePerson.setDate(3, java.sql.Date.valueOf(skier.getDateOfBirth()));
	        pstmtUpdatePerson.setString(4, DatabaseFormatter.format(skier.getPhoneNumber()));
	        pstmtUpdatePerson.setString(5, DatabaseFormatter.format(skier.getEmail()));
	        pstmtUpdatePerson.setString(6, DatabaseFormatter.format(skier.getAddress().getCity()));
	        pstmtUpdatePerson.setString(7, DatabaseFormatter.format(skier.getAddress().getPostcode()));
	        pstmtUpdatePerson.setString(8, DatabaseFormatter.format(skier.getAddress().getStreetName()));
	        pstmtUpdatePerson.setString(9, DatabaseFormatter.format(skier.getAddress().getStreetNumber()));
	        pstmtUpdatePerson.setInt(10, skier.getId());
	        pstmtUpdatePerson.executeUpdate();

	        pstmtUpdateSkier.setInt(1, skier.getId());
	        pstmtUpdateSkier.setInt(2, skier.getId());
	        
	        int affectedRows = pstmtUpdateSkier.executeUpdate();

	        if (affectedRows > 0) {
	            connection.commit();
	            return true;
	        }
	        
	        return false;
	    } catch (SQLException e) {
	        DatabaseTransaction.rollbackTransaction(connection);
	        return false;
	    } finally {
	        DatabaseTransaction.restoreAutoCommit(connection);
	    }
	}

	@Override
	public Skier find(int id) {
		String sql = """
			SELECT *
			FROM skiers 
			NATURAL JOIN persons 
			WHERE skier_id = ?
		""";


	    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

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
		String sql = """
			SELECT *
			FROM skiers 
			NATURAL JOIN persons
			ORDER BY skier_id DESC
		""";


	    List<Skier> skiers = new ArrayList<>();

	    try (PreparedStatement stmt = connection.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

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
