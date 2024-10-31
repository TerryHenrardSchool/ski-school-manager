package be.th.ski_school_manager;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class InstructorDAO extends DAO<Instructor> {

	public InstructorDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Instructor obj) {
		return false; // TODO
	}

	@Override
	public boolean delete(Instructor obj) {
		return false; // TODO
	}

	@Override
	public boolean update(Instructor obj) {
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
}
