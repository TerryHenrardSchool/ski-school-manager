package be.th.dao;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseTransaction {
	
	public static void rollbackTransaction(Connection connection) {
	    try {
	        connection.rollback();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	public static void restoreAutoCommit(Connection connection) {
	    try {
	        connection.setAutoCommit(true);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
}
