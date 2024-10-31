package be.th.ski_school_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class SkiSchoolConnection {
	
	private static Connection instance = null;
    private static final String DATABASE_URL = "jdbc:ucanaccess://./Ecole.accdb"; // Database URL
	
	private SkiSchoolConnection(){
		try{
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			instance = DriverManager.getConnection(DATABASE_URL);
		} catch (ClassNotFoundException ex){
			JOptionPane.showMessageDialog(null, "Driver class is nowhere to be found."+ ex.getMessage());
			System.exit(0);			
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "JDBC error : " +ex.getMessage());
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
