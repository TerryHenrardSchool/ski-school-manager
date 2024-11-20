package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.th.models.Period;

public class PeriodDAO extends DAO<Period>{

	public PeriodDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Period obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Period obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Period find(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Period find(LocalDate date) {
		String sql = """
		    SELECT *
		    FROM periods
		    WHERE ? BETWEEN start_date AND end_date
		""";

		Period period = null;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setDate(1, java.sql.Date.valueOf(date));
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				period = new Period(
					rs.getInt("period_id"), 
					rs.getDate("start_date").toLocalDate(),
					rs.getDate("end_date").toLocalDate(), 
					rs.getBoolean("is_vacation"), 
					rs.getString("name")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return period;
	}

	@Override
	public List<Period> findAll() {
		String sql = """
		    SELECT * 
		    FROM periods
		    ORDER BY start_date
		""";
		
		List<Period> periods = new ArrayList<>();
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
	        	Period period = new Period(
                    rs.getInt("period_id"),
                    rs.getDate("start_date").toLocalDate(),
                    rs.getDate("end_date").toLocalDate(),
                    rs.getBoolean("is_vacation"),
                    rs.getString("name")
        		);
	        	
            	periods.add(period);
            }
        } catch (SQLException e) {
            e.printStackTrace();
		}
		
		return periods;
	}

	@Override
	public List<Period> findAll(Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
