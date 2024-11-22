package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import be.th.models.Booking;

public class BookingDAO extends DAO<Booking>{

	public BookingDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Booking booking) {
		String query = """
			INSERT INTO bookings (
				booking_date, 
				is_insured, 
				period_id,
				secretary_id,
				lesson_id, 
				skier_id
			) 
			VALUES (?, ?, ?, ?, ?, ?)
		"""; 

		try (PreparedStatement statement = this.connection.prepareStatement(query)) {
			statement.setObject(1, booking.getBookingDate());
			statement.setBoolean(2, booking.getInsurance());
			statement.setInt(3, booking.getPeriod().getId());
			statement.setInt(4, 21);
			statement.setInt(5, booking.getLesson().getId());
			statement.setInt(6, booking.getSkier().getId());

			int rowsInserted = statement.executeUpdate();
			return rowsInserted > 0;
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
	public boolean update(Booking obj) {
		return false; // TODO
	}

	@Override
	public Booking find(int id) {
		return null; // TODO
	}

	@Override
	public List<Booking> findAll(Map<String, Object> criteria) {
 		return null; // TODO
	}

	@Override
	public List<Booking> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
