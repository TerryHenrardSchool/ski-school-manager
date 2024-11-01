package be.th.dao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import be.th.classes.Skier;

public class SkierDAO extends DAO<Skier>{

	public SkierDAO(Connection conn) {
		super(conn);
	}

	@Override
	public boolean create(Skier obj) {
		return false; // TODO
	}

	@Override
	public boolean delete(Skier obj) {
		return false; // TODO
	}

	@Override
	public boolean update(Skier obj) {
		return false; // TODO
	}

	@Override
	public Skier find(int id) {
		return null; // TODO
	}

	@Override
	public List<Skier> findAll(Map<String, Object> criteria) {
		return null; // TODO
	}

	@Override
	public List<Skier> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
}
