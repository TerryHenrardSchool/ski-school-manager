package be.th.ski_school_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class SkiSchoolConnection {
	
	private static final String DATABASE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String DATABASE_URL = "jdbc:oracle:thin:@//193.190.64.10:1522/xepdb1";
	private static final String DATABASE_USERNAME = "STUDENT03_05";
	private static final String DATABASE_PASSWORD = "dontchangeme";
	
	private static Connection instance = null;
	
	private SkiSchoolConnection(){
		try{
			Class.forName(DATABASE_DRIVER);
			
			System.setProperty("oracle.jdbc.Trace", "true");
			instance = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
			System.out.println("Successfully connected!");
		} catch (ClassNotFoundException ex){
			JOptionPane.showMessageDialog(null, "Driver class is nowhere to be found."+ ex.getMessage());
			System.exit(0);			
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "JDBC error : " + ex.getMessage());
			System.exit(0);			
		}
		
		if (instance == null) {
			JOptionPane.showMessageDialog(null, "The database isn't accessible, program closing.");
			System.exit(0);
		}
	}
	
	public static Connection getInstance() {
		if(instance == null){
			new SkiSchoolConnection();
		}
		
		return instance;
	}
	
	public static void closeConnection() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                instance = null;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error closing the connection: " + ex.getMessage());
        }
    }
}
