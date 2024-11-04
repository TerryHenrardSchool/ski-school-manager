package be.th.jframes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import com.toedter.calendar.JDateChooser;

import be.th.dao.DAOFactory;
import be.th.formatters.DatabaseFormatter;
import be.th.models.Instructor;
import be.th.parsers.DateParser;
import be.th.styles.ColorStyles;
import be.th.styles.FontStyles;

import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.awt.event.ActionEvent;

public class AddAnInstructor extends JFrame {

	private static final long serialVersionUID = 1L;
		
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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddAnInstructor frame = new AddAnInstructor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AddAnInstructor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 554);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Instructor information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 72, 600, 435);
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
		streetNumberField.setBounds(121, 292, 154, 31);
		panel.add(streetNumberField);
		
		JLabel lblStreetNumber = new JLabel("street Number");
		lblStreetNumber.setLabelFor(streetNumberField);
		lblStreetNumber.setFont(FontStyles.FIELD);
		lblStreetNumber.setBounds(10, 292, 108, 31);
		panel.add(lblStreetNumber);
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainMenu mainMenu = new MainMenu();
				mainMenu.setVisible(true);
				
				dispose();
			}
		});
		cancelBtn.setFont(FontStyles.BUTTON);
		cancelBtn.setContentAreaFilled(true);
		cancelBtn.setOpaque(true);
		cancelBtn.setBorderPainted(false);
		cancelBtn.setBackground(ColorStyles.CANCEL_BUTTON);
		cancelBtn.setBounds(121, 360, 154, 51);
		panel.add(cancelBtn);
		
		JButton submitBtn = new JButton("Submit");
		submitBtn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        try {
		            String lastName = DatabaseFormatter.format(lastNameField.getText());
		            String firstName = DatabaseFormatter.format(firstNameField.getText());
		            LocalDate dateOfBirth = DateParser.toLocalDate(dateOfBirthField.getDate());
		            String phoneNumber = DatabaseFormatter.format(phoneNumberField.getText());
		            String email = DatabaseFormatter.format(emailField.getText());
		            String city = DatabaseFormatter.format(cityField.getText());
		            String postcode = DatabaseFormatter.format(postcodeField.getText());
		            String streetName = DatabaseFormatter.format(streetNameField.getText());
		            String streetNumber = DatabaseFormatter.format(streetNumberField.getText());
		            
		            Instructor instructor = new Instructor(lastName, firstName, dateOfBirth, city, postcode, streetName, streetNumber, phoneNumber, email);
		            
		            boolean isAdded = new DAOFactory().getInstructorDAO().create(instructor);
		            
		            if (isAdded) {
		                JOptionPane.showMessageDialog(null, "Instructor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
		                resetFormFields();
		            } else {
		                JOptionPane.showMessageDialog(null, "Failed to add instructor. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        } catch (Exception ex) {
		            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		        }
		    }
		});

		submitBtn.setFont(FontStyles.BUTTON);
		submitBtn.setContentAreaFilled(true);
		submitBtn.setOpaque(true);
		submitBtn.setBorderPainted(false);
		submitBtn.setBackground(ColorStyles.SUBMIT_BUTTON);
		submitBtn.setBounds(304, 360, 154, 51);
		panel.add(submitBtn);
		
		JLabel lblNewLabel_1 = new JLabel("Add a new instructor");
		lblNewLabel_1.setFont(FontStyles.TITLE);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBackground(new Color(0, 153, 255));
		lblNewLabel_1.setOpaque(true);
		lblNewLabel_1.setBounds(10, 10, 600, 52);
		contentPane.add(lblNewLabel_1);
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