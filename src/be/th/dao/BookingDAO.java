package be.th.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import be.th.classes.Booking;

public class BookingDAO extends DAO<Booking>{

	public BookingDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Booking obj) {
		return false; // TODO
	}

	@Override
	public boolean delete(Booking obj) {
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
