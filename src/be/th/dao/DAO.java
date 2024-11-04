package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;

public abstract class DAO<T> {
	
	protected Connection connection = null;
	
	public DAO(Connection conn){
		this.connection = conn;
	}
	
	public abstract boolean create(T obj);
	public abstract boolean delete(int id);
	public abstract boolean update(T obj);
	public abstract T find(int id);
	public abstract List<T> findAll();
	public abstract List<T> findAll(Map<String, Object> criteria);
}