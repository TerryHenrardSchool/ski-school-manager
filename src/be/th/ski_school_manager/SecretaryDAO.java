package be.th.ski_school_manager;

import java.sql.Connection;
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
		return null; // TODO
	}

	@Override
	public List<Secretary> findAll(Map<String, Object> criteria) {
 		return null; // TODO
	}
}
