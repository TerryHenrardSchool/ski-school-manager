package be.th.jframes;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import com.toedter.calendar.JDateChooser;

import be.th.dao.AccreditationDAO;
import be.th.dao.DAO;
import be.th.dao.DAOFactory;
import be.th.dao.InstructorDAO;
import be.th.models.Accreditation;
import be.th.models.Instructor;
import be.th.parsers.DateParser;
import be.th.styles.ColorStyles;
import be.th.styles.FontStyles;

import java.awt.Color;
import java.awt.Component;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JCheckBox;

public class AddAnInstructor extends JFrame {

	private static final long serialVersionUID = 1L;
	
	
	private DAO<Instructor> instructorDAO;
	private DAO<Accreditation> accreditationDAO;
		
	private JPanel contentPane;
	private JTextField lastNameField;
	private JTextField firstNameField;
	private JDateChooser dateOfBirthField;
	private JTextField phoneNumberField;
	private JTextField emailField;
	private JTextField cityField;
	private JTextField postcodeField;
	private JTextField streetNameField;
	private JTextField streetNumberField;

	public AddAnInstructor() {
		DAOFactory daoFactory = new DAOFactory();
		
		instructorDAO = daoFactory.getInstructorDAO();
		accreditationDAO = daoFactory.getAccreditationDAO();
		
		// I'll add some library
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 662);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Instructor information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 72, 600, 540);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Last name");
		lblNewLabel.setFont(FontStyles.FIELD);
		lblNewLabel.setBounds(10, 33, 90, 31);
		panel.add(lblNewLabel);
		
		lastNameField = new JTextField();
		lblNewLabel.setLabelFor(lastNameField);
		lastNameField.setFont(FontStyles.FIELD);
		lastNameField.setBounds(121, 33, 154, 31);
		panel.add(lastNameField);
		lastNameField.setColumns(10);
		
		firstNameField = new JTextField();
		firstNameField.setFont(FontStyles.FIELD);
		firstNameField.setColumns(10);
		firstNameField.setBounds(428, 33, 154, 31);
		panel.add(firstNameField);
		
		JLabel lblFirstName = new JLabel("First name");
		lblFirstName.setLabelFor(firstNameField);
		lblFirstName.setFont(FontStyles.FIELD);
		lblFirstName.setBounds(304, 33, 90, 31);
		panel.add(lblFirstName);
		
		JLabel lblDateOfBirth = new JLabel("Date of birth");
		lblDateOfBirth.setFont(FontStyles.FIELD);
		lblDateOfBirth.setBounds(10, 96, 90, 31);
		panel.add(lblDateOfBirth);
		
		dateOfBirthField = new JDateChooser();
		lblDateOfBirth.setLabelFor(dateOfBirthField);
		dateOfBirthField.setBounds(121, 96, 154, 31);
		panel.add(dateOfBirthField);
		
		JLabel lblPhoneNumber = new JLabel("Phone number");
		lblPhoneNumber.setFont(FontStyles.FIELD);
		lblPhoneNumber.setBounds(304, 96, 114, 31);
		panel.add(lblPhoneNumber);
		
		phoneNumberField = new JTextField();
		lblPhoneNumber.setLabelFor(phoneNumberField);
		phoneNumberField.setFont(FontStyles.FIELD);
		phoneNumberField.setColumns(10);
		phoneNumberField.setBounds(428, 96, 154, 31);
		panel.add(phoneNumberField);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(FontStyles.FIELD);
		lblEmail.setBounds(10, 161, 90, 31);
		panel.add(lblEmail);
		
		emailField = new JTextField();
		lblEmail.setLabelFor(emailField);
		emailField.setFont(FontStyles.FIELD);
		emailField.setColumns(10);
		emailField.setBounds(121, 161, 154, 31);
		panel.add(emailField);
		
		cityField = new JTextField();
		cityField.setFont(FontStyles.FIELD);
		cityField.setColumns(10);
		cityField.setBounds(428, 161, 154, 31);
		panel.add(cityField);
		
		JLabel lblFcity = new JLabel("City");
		lblFcity.setLabelFor(cityField);
		lblFcity.setFont(FontStyles.FIELD);
		lblFcity.setBounds(304, 161, 90, 31);
		panel.add(lblFcity);
		
		JLabel lblPostcode = new JLabel("Postcode");
		lblPostcode.setFont(FontStyles.FIELD);
		lblPostcode.setBounds(10, 228, 90, 31);
		panel.add(lblPostcode);
		
		postcodeField = new JTextField();
		lblPostcode.setLabelFor(postcodeField);
		postcodeField.setFont(FontStyles.FIELD);
		postcodeField.setColumns(10);
		postcodeField.setBounds(121, 228, 154, 31);
		panel.add(postcodeField);
		
		JLabel lblStreetName = new JLabel("Street name");
		lblStreetName.setFont(FontStyles.FIELD);
		lblStreetName.setBounds(304, 228, 114, 31);
		panel.add(lblStreetName);
		
		streetNameField = new JTextField();
		lblStreetName.setLabelFor(streetNameField);
		streetNameField.setFont(FontStyles.FIELD);
		streetNameField.setColumns(10);
		streetNameField.setBounds(428, 228, 154, 31);
		panel.add(streetNameField);
		
		streetNumberField = new JTextField();
		streetNumberField.setFont(FontStyles.FIELD);
		streetNumberField.setColumns(10);
		streetNumberField.setBounds(428, 292, 154, 31);
		panel.add(streetNumberField);
		
		JLabel lblStreetNumber = new JLabel("Street Number");
		lblStreetNumber.setLabelFor(streetNumberField);
		lblStreetNumber.setFont(FontStyles.FIELD);
		lblStreetNumber.setBounds(304, 292, 108, 31);
		panel.add(lblStreetNumber);
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(this::handleCLickOnCancelButton);
		cancelBtn.setFont(FontStyles.BUTTON);
		cancelBtn.setContentAreaFilled(true);
		cancelBtn.setOpaque(true);
		cancelBtn.setBorderPainted(false);
		cancelBtn.setBackground(ColorStyles.RED);
		cancelBtn.setBounds(121, 470, 154, 51);
		panel.add(cancelBtn);
		
		JButton submitBtn = new JButton("Add");
		submitBtn.addActionListener(this::handleClickOnAddButton);

		submitBtn.setFont(FontStyles.BUTTON);
		submitBtn.setContentAreaFilled(true);
		submitBtn.setOpaque(true);
		submitBtn.setBorderPainted(false);
		submitBtn.setBackground(ColorStyles.GREEN);
		submitBtn.setBounds(304, 470, 154, 51);
		panel.add(submitBtn);
		
		JLabel lblAccreditation = new JLabel("Accreditations");
		lblAccreditation.setFont(FontStyles.FIELD);
		lblAccreditation.setBounds(10, 292, 114, 31);
		panel.add(lblAccreditation);
		
		List<Accreditation> accreditations = Accreditation.findAllInDatabase((AccreditationDAO) accreditationDAO);
		List<JCheckBox> checkBoxes = createAccreditationCheckBoxes(accreditations, FontStyles.CHECK_BOX, 121, 294, 180, 31, 26);
		
		addComponentsIntoPanel(checkBoxes, panel);
		repaintPanel(panel);
		
		JLabel lblNewLabel_1 = new JLabel("Add a new instructor");
		lblNewLabel_1.setFont(FontStyles.TITLE);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBackground(new Color(0, 153, 255));
		lblNewLabel_1.setOpaque(true);
		lblNewLabel_1.setBounds(10, 10, 600, 52);
		contentPane.add(lblNewLabel_1);
	}
	
	private void repaintPanel(JPanel panel) {
		panel.revalidate();
		panel.repaint();
	}
	
	private void addComponentsIntoPanel(List<? extends Component> components, JPanel panel) {
		for (Component component : components) {
		    panel.add(component);
		}
	}
	
	public List<JCheckBox> createAccreditationCheckBoxes(
	    List<Accreditation> accreditations, 
	    Font font,
	    int startX, 
	    int startY, 
	    int width, 
	    int height, 
	    int columnGap
	) {
	    List<JCheckBox> checkBoxes = new ArrayList<>();
	    int yPosition = startY;

	    for (Accreditation accreditation : accreditations) {
	        String label = accreditation.getSportType() + " - " + accreditation.getAgeCategory();
	        
	        JCheckBox checkBox = new JCheckBox(label);
	        checkBox.setFont(font); 
	        checkBox.setBounds(startX, yPosition, width, height); 

	        checkBoxes.add(checkBox);
	        yPosition += columnGap; 
	    }

	    return checkBoxes;
	}
	
	private void handleCLickOnCancelButton(ActionEvent ev) {
		openMainMenu();
		dispose();
	}
	
	private void handleClickOnAddButton(ActionEvent ev) {
		try {
	        Instructor instructor = buildInstructorFromTextFields();
	        boolean isAdded = insertInstructorIntoDatabase(instructor);	 
	        
	        if(isAdded) {
	        	displayAddInstructorSuccess(JOptionPane.INFORMATION_MESSAGE);
	        } else {
	        	displayAddInstructorFailure(JOptionPane.ERROR_MESSAGE);
	        }
	        resetFormFields();
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	private void openMainMenu() {
		MainMenu mainMenu = new MainMenu();
		mainMenu.setVisible(true);		
	}
	
	private void displayAddInstructorSuccess(int messageType) {
        JOptionPane.showMessageDialog(null, "Instructor added successfully!", "Success", messageType);
	}
	
	private void displayAddInstructorFailure(int messageType) {
        JOptionPane.showMessageDialog(null, "Failed to add instructor. Please try again.", "Error", messageType);
	}
	
	private List<Accreditation> getAccreditationsFromDatabase() {
		return Accreditation.findAllInDatabase((AccreditationDAO) accreditationDAO);
	}
	
	/*private Accreditation buildAccreditationFromCheckBoxes() {
		
	}*/
	
	private Instructor buildInstructorFromTextFields() {
		String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        LocalDate dateOfBirth = DateParser.toLocalDate(dateOfBirthField.getDate());
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String city = cityField.getText();
        String postcode = postcodeField.getText();
        String streetName = streetNameField.getText();
        String streetNumber = streetNumberField.getText();

        return new Instructor(lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email);
	}
	
	private boolean insertInstructorIntoDatabase(Instructor instructor) {
	    InstructorDAO instructorDAO = (InstructorDAO) new DAOFactory().getInstructorDAO();
	    return instructor.insertIntoDatabase(instructorDAO);
	}
	    
	private void resetFormFields() {
	    lastNameField.setText("");
	    firstNameField.setText("");
	    dateOfBirthField.setDate(null);
	    phoneNumberField.setText("");
	    emailField.setText("");
	    cityField.setText("");
	    postcodeField.setText("");
	    streetNameField.setText("");
	    streetNumberField.setText("");
	}
}