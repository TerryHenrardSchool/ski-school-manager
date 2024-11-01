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
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.lang.invoke.StringConcatFactory;
import java.time.LocalDate;
import java.time.ZoneId;
import java.awt.event.ActionEvent;

public class AddASkier extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final Font TITLE_FONT = new Font("Tahoma", Font.PLAIN, 24);
    private final Font FIELD_FONT = new Font("Tahoma", Font.PLAIN, 16);
    private final Font BUTTON_FONT = new Font("Tahoma", Font.PLAIN, 14);
	
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
					AddASkier frame = new AddASkier();
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
	public AddASkier() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 630, 554);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Skier information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 72, 600, 435);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Last name");
		lblNewLabel.setFont(FIELD_FONT);
		lblNewLabel.setBounds(10, 33, 90, 31);
		panel.add(lblNewLabel);
		
		lastNameField = new JTextField();
		lblNewLabel.setLabelFor(lastNameField);
		lastNameField.setFont(FIELD_FONT);
		lastNameField.setBounds(121, 33, 154, 31);
		panel.add(lastNameField);
		lastNameField.setColumns(10);
		
		firstNameField = new JTextField();
		firstNameField.setFont(FIELD_FONT);
		firstNameField.setColumns(10);
		firstNameField.setBounds(428, 33, 154, 31);
		panel.add(firstNameField);
		
		JLabel lblFirstName = new JLabel("First name");
		lblFirstName.setLabelFor(firstNameField);
		lblFirstName.setFont(FIELD_FONT);
		lblFirstName.setBounds(304, 33, 90, 31);
		panel.add(lblFirstName);
		
		JLabel lblDateOfBirth = new JLabel("Date of birth");
		lblDateOfBirth.setFont(FIELD_FONT);
		lblDateOfBirth.setBounds(10, 96, 90, 31);
		panel.add(lblDateOfBirth);
		
		dateOfBirthField = new JDateChooser();
		lblDateOfBirth.setLabelFor(dateOfBirthField);
		dateOfBirthField.setBounds(121, 96, 154, 31);
		panel.add(dateOfBirthField);
		
		JLabel lblPhoneNumber = new JLabel("Phone number");
		lblPhoneNumber.setFont(FIELD_FONT);
		lblPhoneNumber.setBounds(304, 96, 114, 31);
		panel.add(lblPhoneNumber);
		
		phoneNumberField = new JTextField();
		lblPhoneNumber.setLabelFor(phoneNumberField);
		phoneNumberField.setFont(FIELD_FONT);
		phoneNumberField.setColumns(10);
		phoneNumberField.setBounds(428, 96, 154, 31);
		panel.add(phoneNumberField);
		
		JLabel lblEmail = new JLabel("Email");
		lblEmail.setFont(FIELD_FONT);
		lblEmail.setBounds(10, 161, 90, 31);
		panel.add(lblEmail);
		
		emailField = new JTextField();
		lblEmail.setLabelFor(emailField);
		emailField.setFont(FIELD_FONT);
		emailField.setColumns(10);
		emailField.setBounds(121, 161, 154, 31);
		panel.add(emailField);
		
		cityField = new JTextField();
		cityField.setFont(FIELD_FONT);
		cityField.setColumns(10);
		cityField.setBounds(428, 161, 154, 31);
		panel.add(cityField);
		
		JLabel lblFcity = new JLabel("City");
		lblFcity.setLabelFor(cityField);
		lblFcity.setFont(FIELD_FONT);
		lblFcity.setBounds(304, 161, 90, 31);
		panel.add(lblFcity);
		
		JLabel lblPostcode = new JLabel("Postcode");
		lblPostcode.setFont(FIELD_FONT);
		lblPostcode.setBounds(10, 228, 90, 31);
		panel.add(lblPostcode);
		
		postcodeField = new JTextField();
		lblPostcode.setLabelFor(postcodeField);
		postcodeField.setFont(FIELD_FONT);
		postcodeField.setColumns(10);
		postcodeField.setBounds(121, 228, 154, 31);
		panel.add(postcodeField);
		
		JLabel lblStreetName = new JLabel("Street name");
		lblStreetName.setFont(FIELD_FONT);
		lblStreetName.setBounds(304, 228, 114, 31);
		panel.add(lblStreetName);
		
		streetNameField = new JTextField();
		lblStreetName.setLabelFor(streetNameField);
		streetNameField.setFont(FIELD_FONT);
		streetNameField.setColumns(10);
		streetNameField.setBounds(428, 228, 154, 31);
		panel.add(streetNameField);
		
		streetNumberField = new JTextField();
		streetNumberField.setFont(FIELD_FONT);
		streetNumberField.setColumns(10);
		streetNumberField.setBounds(121, 292, 154, 31);
		panel.add(streetNumberField);
		
		JLabel lblStreetNumber = new JLabel("street Number");
		lblStreetNumber.setLabelFor(streetNumberField);
		lblStreetNumber.setFont(FIELD_FONT);
		lblStreetNumber.setBounds(10, 292, 108, 31);
		panel.add(lblStreetNumber);
		
		JButton CancelBtn = new JButton("Cancel");
		CancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainMenu mainMenu = new MainMenu();
				mainMenu.setVisible(true);
				
				dispose();
			}
		});
		CancelBtn.setFont(BUTTON_FONT);
		CancelBtn.setContentAreaFilled(true);
		CancelBtn.setOpaque(true);
		CancelBtn.setBorderPainted(false);
		CancelBtn.setBackground(new Color(255, 57, 57));
		CancelBtn.setBounds(121, 360, 154, 51);
		panel.add(CancelBtn);
		
		JButton SubmitBtn = new JButton("Submit");
		SubmitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				areFieldsValid();
			}
		});
		SubmitBtn.setFont(BUTTON_FONT);
		SubmitBtn.setContentAreaFilled(true);
		SubmitBtn.setOpaque(true);
		SubmitBtn.setBorderPainted(false);
		SubmitBtn.setBackground(new Color(139, 255, 96));
		SubmitBtn.setBounds(304, 360, 154, 51);
		panel.add(SubmitBtn);
		
		JLabel lblNewLabel_1 = new JLabel("Add a new skier");
		lblNewLabel_1.setFont(TITLE_FONT);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBackground(new Color(0, 153, 255));
		lblNewLabel_1.setOpaque(true);
		lblNewLabel_1.setBounds(10, 10, 600, 52);
		contentPane.add(lblNewLabel_1);
	}
	
	private boolean areFieldsValid() {
	    // Vérification de base pour les champs requis
	    if (!validateField(lastNameField, "Last name is required") ||
	        !validateField(firstNameField, "First name is required") ||
	        !validateField(phoneNumberField, "Phone number is required") ||
	        !validateField(emailField, "Email is required") ||
	        !validateField(cityField, "City is required") ||
	        !validateField(postcodeField, "Postcode is required") ||
	        !validateField(streetNameField, "Street name is required") ||
	        !validateField(streetNumberField, "Street number is required")) {
	        return false;
	    }

	    // Validation spécifique pour la date de naissance
	    LocalDate skierDateOfBirth = dateOfBirthField.getDate() != null 
	        ? dateOfBirthField.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() 
	        : null;
	    if (skierDateOfBirth == null) {
	        JOptionPane.showMessageDialog(this, "Date of birth is required.", "Error", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }

	    // Validation du format du numéro de téléphone
	    String phoneRegex = "^\\+?[0-9. ()-]{7,}$";
	    if (!validateRegex(phoneNumberField, phoneRegex, "Phone number format is invalid. Please enter a valid phone number.")) {
	        return false;
	    }

	    // Validation du format de l'email
	    String emailRegex = "^[\\w\\.-]+@[\\w\\.-]+\\.\\w{2,}$";
	    if (!validateRegex(emailField, emailRegex, "Email format is invalid. Please enter a valid email address.")) {
	        return false;
	    }

	    return true;
	}

	private boolean validateField(JTextField field, String errorMessage) {
	    if (field.getText().trim().isEmpty()) {
	        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    return true;
	}

	// Nouvelle fonction pour la validation par regex
	private boolean validateRegex(JTextField field, String regex, String errorMessage) {
	    if (!field.getText().trim().matches(regex)) {
	        JOptionPane.showMessageDialog(this, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    return true;
	}
}