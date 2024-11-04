package be.th.ski_school_manager;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class LessonDAO extends DAO<Lesson>{

	public LessonDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Lesson obj) {
		return false; // TODO
	}

	@Override
	public boolean delete(Lesson obj) {
		return false; // TODO
	}

	@Override
	public boolean update(Lesson obj) {
		return false; // TODO
	}

	@Override
	public Lesson find(int id) {
		return null; // TODO
	}

	@Override
	public List<Lesson> findAll(Map<String, Object> criteria) {
 		return null; // TODO
	}
}
