package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseSequence {
	public static int getNextSequenceValue(Connection connection, String sequenceName) throws SQLException {
        String sql = "SELECT " + sequenceName + ".NEXTVAL FROM DUAL";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("Impossible to get the next sequence value : " + sequenceName);
            }
        }
    }
	
	public static int getCurrentSequenceValue(Connection connection, String sequenceName) throws SQLException {
		String sql = "SELECT " + sequenceName + ".CURRVAL FROM DUAL";
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				throw new SQLException("Impossible to get the current sequence value : " + sequenceName);
			}
		}
	}
}
