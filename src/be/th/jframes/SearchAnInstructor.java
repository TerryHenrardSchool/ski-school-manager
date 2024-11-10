package be.th.jframes;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import be.th.dao.DAO;
import be.th.dao.DAOFactory;
import be.th.dao.InstructorDAO;
import be.th.formatters.DatabaseFormatter;
import be.th.models.Address;
import be.th.models.Instructor;
import be.th.models.Person;
import be.th.parsers.DateParser;
import be.th.styles.ColorStyles;
import be.th.styles.FontStyles;
import be.th.validators.DateValidator;
import be.th.validators.IntegerValidator;
import be.th.validators.ObjectValidator;
import be.th.validators.StringValidator;

import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.border.EtchedBorder;
import com.toedter.calendar.JDateChooser;

public class SearchAnInstructor extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private DAO<Instructor> instructorDao = new DAOFactory().getInstructorDAO();
	
	private JPanel contentPane;
	private JTable table;
	private JTextField idSearchTxtField;
	private LinkedHashMap<Integer, Instructor> instructorMap = new LinkedHashMap<>();
	private JTextField lastNameSearchTxtField;
	private JTextField firstNameSearchTxtField;
	private JDateChooser birthDateTextField;
	private JTextField emailSearchTxtField;
	private JTextField addressSearchTxtField;
	private JTextField phoneNumberTextField;
	
	private Instructor selectedInstructor;

	public SearchAnInstructor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1539, 1003);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Instructors", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(282, 64, 1239, 313);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 24, 1219, 242);
		panel.add(scrollPane);
		
		Object[][] data = {}; 
		String[] columnNames = { "Id", "Full name", "Birthdate", "Address", "Phone number", "Email" };
		int[] columnWidths = { 10, 75, 50, 200, 80, 180 };

		table = createJTable(data, columnNames, columnWidths);
		MouseListener doubleClickListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        handleDoubleClickOnRows(e);
		    }
		};
		addRowSelectionListener(table, this::handleClickOnRows);
		addDoubleClickListener(table, doubleClickListener);
		
		scrollPane.setViewportView(table);
		
		JButton btnDeleteInstructor = new JButton("Delete instructor");
		btnDeleteInstructor.setBounds(10, 271, 150, 31);
		panel.add(btnDeleteInstructor);
		btnDeleteInstructor.addActionListener(this::handleClickOnDeleteButton);
		btnDeleteInstructor.setFont(FontStyles.BUTTON);
		btnDeleteInstructor.setBackground(ColorStyles.RED);
		
		JButton btnUpdateInformation = new JButton("Update instructor");
		btnUpdateInformation.setBounds(170, 271, 150, 31);
		panel.add(btnUpdateInformation);
		btnUpdateInformation.addActionListener(this::handleClickOnUpdateButton);
		btnUpdateInformation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnUpdateInformation.setBackground(ColorStyles.ORANGE);
		
		JLabel lblNewLabel = new JLabel("(or double click on the row...)");
		lblNewLabel.setBounds(330, 281, 196, 14);
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Search criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(20, 64, 252, 313);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblSearchInstructorId = new JLabel("Id:");
		lblSearchInstructorId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchInstructorId.setBounds(10, 24, 64, 25);
		panel_1.add(lblSearchInstructorId);
		
		idSearchTxtField = createFilterTextField();
		idSearchTxtField.setToolTipText("");
		idSearchTxtField.setBounds(130, 24, 110, 31);
		panel_1.add(idSearchTxtField);
		idSearchTxtField.setColumns(10);
		
		JLabel lblSearchLastName = new JLabel("Last name:");
		lblSearchLastName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchLastName.setBounds(10, 60, 85, 25);
		panel_1.add(lblSearchLastName);
		
		lastNameSearchTxtField = createFilterTextField();
		lastNameSearchTxtField.setToolTipText("");
		lastNameSearchTxtField.setColumns(10);
		lastNameSearchTxtField.setBounds(130, 60, 110, 31);
		panel_1.add(lastNameSearchTxtField);
		
		JLabel lblSearchFirstName = new JLabel("First name:");
		lblSearchFirstName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchFirstName.setBounds(10, 96, 85, 25);
		panel_1.add(lblSearchFirstName);
		
		firstNameSearchTxtField = createFilterTextField();
		firstNameSearchTxtField.setToolTipText("");
		firstNameSearchTxtField.setColumns(10);
		firstNameSearchTxtField.setBounds(130, 96, 110, 31);
		panel_1.add(firstNameSearchTxtField);
		
		JButton btnResetSearch = new JButton("Reset");
		btnResetSearch.setBackground(ColorStyles.BLUE);
		btnResetSearch.addActionListener(this::handleClickOnResetFiltersButton);
		btnResetSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnResetSearch.setBounds(130, 271, 110, 31);
		panel_1.add(btnResetSearch);
		
		JLabel lblSearchBirthdate = new JLabel("Birthdate:");
		lblSearchBirthdate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchBirthdate.setBounds(10, 131, 85, 25);
		panel_1.add(lblSearchBirthdate);
		
		JLabel lblSearchAddress = new JLabel("Address:");
		lblSearchAddress.setBounds(10, 166, 110, 25);
		panel_1.add(lblSearchAddress);
		lblSearchAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		addressSearchTxtField = createFilterTextField();
		addressSearchTxtField.setBounds(130, 166, 110, 31);
		panel_1.add(addressSearchTxtField);
		addressSearchTxtField.setToolTipText("");
		addressSearchTxtField.setColumns(10);
		
		JLabel lblSearchPhoneNumber = new JLabel("Phone number:");
		lblSearchPhoneNumber.setBounds(10, 201, 110, 25);
		panel_1.add(lblSearchPhoneNumber);
		lblSearchPhoneNumber.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		phoneNumberTextField = createFilterTextField();
		phoneNumberTextField.setBounds(130, 201, 110, 31);
		panel_1.add(phoneNumberTextField);
		phoneNumberTextField.setToolTipText("");
		phoneNumberTextField.setColumns(10);
		
		JLabel lblSearchEmailAddress = new JLabel("Email:");
		lblSearchEmailAddress.setBounds(10, 236, 110, 25);
		panel_1.add(lblSearchEmailAddress);
		lblSearchEmailAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		emailSearchTxtField = createFilterTextField();
		emailSearchTxtField.setBounds(130, 236, 110, 31);
		panel_1.add(emailSearchTxtField);
		emailSearchTxtField.setToolTipText("");
		emailSearchTxtField.setColumns(10);
		
		birthDateTextField = createFilterJDateChooser();
		birthDateTextField.setBounds(130, 131, 110, 31);
		panel_1.add(birthDateTextField);
		
		JLabel lblTitle = new JLabel("Search for a instructor");
		lblTitle.setBounds(20, 10, 1501, 52);
		contentPane.add(lblTitle);
		lblTitle.setOpaque(true);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblTitle.setBackground(new Color(0, 153, 255));
		
		JButton cancelBtn = new JButton("Back");
		cancelBtn.addActionListener(this::handleClickOnBackButton);
		cancelBtn.setOpaque(true);
		cancelBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelBtn.setContentAreaFilled(true);
		cancelBtn.setBorderPainted(false);
		cancelBtn.setBackground(new Color(255, 57, 57));
		cancelBtn.setBounds(20, 10, 154, 52);
		contentPane.add(cancelBtn);
				
		loadInstructorMap();
		displayInstructorsInTable(instructorMap.values());
	}
	
	private void warnThereIsNoInstructorSlected() {
		JOptionPane.showMessageDialog(
			null, 
			"Please, select an instructor.", 
			"Watch out!",
			JOptionPane.WARNING_MESSAGE
		);
	}
	
	private void confirmDeletion() {
		JOptionPane.showMessageDialog(
			null, 
			selectedInstructor.getLastnameFormattedForDisplay() + " " + 
			selectedInstructor.getFirstNameFormattedForDisplay() + " has been successfully deleted.",
			"Success",
			JOptionPane.WARNING_MESSAGE
		);
	}
	
	private int askConfirmationBeforeDeletion() {
		return JOptionPane.showConfirmDialog(
			null, 
			"This is a critical operation. Are you sure that you want to delete " + 
			selectedInstructor.getLastnameFormattedForDisplay() + " " + 
			selectedInstructor.getFirstNameFormattedForDisplay(),
			"Are you sure?",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		);
	}
	
	private void sendErrorWhileDeleting() {
		JOptionPane.showMessageDialog(
			null, 
			"Error while deleting " + 
			selectedInstructor.getLastnameFormattedForDisplay() + " " + 
			selectedInstructor.getFirstNameFormattedForDisplay() + 
			".Please, try again later."
		);
	}
	
	private void displayInstructorsInTable(Collection<Instructor> instructors) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		
		for (final Instructor instructor : instructors) {
			model.addRow(getPreparedInstructorInfoForTableModel(instructor));
		}
	}
	
	private void loadInstructorMap() {	
		List<Instructor> instructors = instructorDao.findAll();
		
		if(!instructorMap.isEmpty()) {
			instructorMap.clear();
		}
		for (final Instructor instructor : instructors) {
            instructorMap.put(instructor.getId(), instructor);
        }
	}
	
	private List<Instructor> searchInstructors(
		Integer id, 
		String lastName, 
		String firstName, 
		LocalDate birthdate,
		String email, 
		String address, 
		String phoneNumber
	) {
	    if (IntegerValidator.hasValue(id)) {
	        final Instructor instructor = instructorMap.get(id);
	        
	        return ObjectValidator.hasValue(instructor) 
        		? List.of(instructor) 
				: List.of();
	    }

	    Stream<Instructor> stream = instructorMap.values().stream();

	    stream = applyFilter(stream, lastName, Instructor::getLastName);
	    stream = applyFilter(stream, firstName, Instructor::getFirstName);
	    stream = applyFilter(stream, birthdate, Instructor::getDateOfBirth);
	    stream = applyFilter(stream, email, Instructor::getEmail);
	    stream = applyFilter(stream, address, instructor -> instructor.getAddress().getAddressFormattedForDisplay());
	    stream = applyFilter(stream, phoneNumber, Instructor::getPhoneNumber);

	    return stream.collect(Collectors.toList());
	}

	private Stream<Instructor> applyFilter(Stream<Instructor> stream, String searchValue, Function<Instructor, String> getter) {
	    if (!StringValidator.hasValue(searchValue)) {
	    	return stream;
	    }
	    
	    String searchValueLower = searchValue.toLowerCase();
        return stream.filter(instructor -> getter.apply(instructor).toLowerCase().contains(searchValueLower));
	}
	
	 private Stream<Instructor> applyFilter(Stream<Instructor> stream, LocalDate searchValue, Function<Instructor, LocalDate> getter) {
        if (!DateValidator.hasValue(searchValue)) {
            return stream;
        }

        return stream.filter(instructor -> getter.apply(instructor).equals(searchValue));
    }
	 
	 private void applyFilters() {
		    final Integer id = getNumberField(idSearchTxtField, "id");
		    final String lastName = getTextField(lastNameSearchTxtField);
		    final String firstName = getTextField(firstNameSearchTxtField);
		    final LocalDate birthdate = DateParser.toLocalDate(birthDateTextField.getDate());
		    final String email = getTextField(emailSearchTxtField);
		    final String address = getTextField(addressSearchTxtField);
		    final String phoneNumber = getTextField(phoneNumberTextField);

		    displayInstructorsInTable(searchInstructors(id, lastName, firstName, birthdate, email, address, phoneNumber));
		}

	
	private Object[] getPreparedInstructorInfoForTableModel(Instructor instructor) {
		return new Object[] {
			instructor.getId(),
			instructor.getLastnameFormattedForDisplay() + " " + instructor.getFirstNameFormattedForDisplay(),
			DatabaseFormatter.toBelgianFormat(instructor.getDateOfBirth()),
			instructor.getAddress().getAddressFormattedForDisplay(),
			instructor.getPhoneNumber(),
			instructor.getEmail()
		};
	}
	
	private String getTextField(JTextField textField) {
		final String texte = textField.getText();
		
		return texte.isBlank() 
			? null 
			: texte.trim();
	}
	
	private Integer getNumberField(JTextField textField, String fieldName) {
		final String texte = getTextField(textField);

		if(!StringValidator.hasValue(texte)) {
			return null;
		}
		
		try {
			return Integer.parseInt(texte.trim());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "The field " + fieldName + " must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
	        return null;
	    }
	}
	
	private void resetTextFields() {
		idSearchTxtField.setText("");
		lastNameSearchTxtField.setText("");
		firstNameSearchTxtField.setText("");
		birthDateTextField.setDate(null);
		emailSearchTxtField.setText("");
		addressSearchTxtField.setText("");
		phoneNumberTextField.setText("");
	}
	
	private JTextField createFilterTextField() {
	    JTextField textField = new JTextField();
	    textField.getDocument().addDocumentListener(new DocumentListener() {
	        @Override
	        public void insertUpdate(DocumentEvent e) {
	            applyFilters();
	        }

	        @Override
	        public void removeUpdate(DocumentEvent e) {
	            applyFilters();
	        }

	        @Override
	        public void changedUpdate(DocumentEvent e) {
	            applyFilters();
	        }
	    });
	    
	    return textField;
	}
	
	private JDateChooser createFilterJDateChooser() {
	    JDateChooser dateChooser = new JDateChooser();
	    dateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
	        applyFilters();
	    });
	    
	    return dateChooser;
	}
	
	private void handleDoubleClickOnRows(MouseEvent e) {
		if(e.getClickCount() != 2) {
			return;
		}
		
		int row = table.rowAtPoint(e.getPoint());
		if (row != -1) { 
			openUpdateInstructorWindow();
		}
	}
	
	private void handleClickOnRows(ListSelectionEvent ev) {
	    if (ev.getValueIsAdjusting()) {
	        return;
	    }
	    
	    int selectedRow = table.getSelectedRow();
	    
	    if (selectedRow < 0) { 
	        return;
	    }
	    
	    try {
	        Integer id = (Integer) table.getValueAt(selectedRow, 0);
	        Map<String, String> nameMap = Person.splitLastNameAndFirstName((String) table.getValueAt(selectedRow, 1));
	        LocalDate birthdate = DateParser.toLocalDate((String) table.getValueAt(selectedRow, 2));
	        Map<String, String> addressMap = Address.destructureFormattedAddress((String) table.getValueAt(selectedRow, 3));
	        String phoneNumber = (String) table.getValueAt(selectedRow, 4);
	        String email = (String) table.getValueAt(selectedRow, 5);
	        
	        selectedInstructor = new Instructor(
        		id, 
        		nameMap.get("lastName"), 
        		nameMap.get("firstName"), 
        		birthdate, 
        		addressMap.get("city"), 
        		addressMap.get("postcode"), 
        		addressMap.get("streetName"), 
        		addressMap.get("streetNumber"), 
        		phoneNumber, 
        		email
    		);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(
        		null, 
        		"Error while fetching instructor data.", 
        		"Erreur", 
        		JOptionPane.ERROR_MESSAGE
    		);
	    }
	}
	
	private void handleUpdateResult(Boolean isUpdated) {
        if (isUpdated) {
        	loadInstructorMap();
    		displayInstructorsInTable(instructorMap.values());
        } 
    }
	

	private void handleClickOnUpdateButton(ActionEvent ev) {
		openUpdateInstructorWindow();
	}
	
	private void openUpdateInstructorWindow() {
		if(!ObjectValidator.hasValue(selectedInstructor)) {
			warnThereIsNoInstructorSlected();
			return;
		}
		
		UpdateAnInstructor updateAnInstructorFrame = new UpdateAnInstructor(selectedInstructor, SearchAnInstructor.this::handleUpdateResult);
		updateAnInstructorFrame.setVisible(true);
	}
	
	private JTable createJTable(
		    Object[][] data,
		    String[] columnNames,
		    int[] columnWidths
		) {
		    JTable table = new JTable();

		    DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
		        private static final long serialVersionUID = -4108980079580312070L;

		        @Override
		        public boolean isCellEditable(int row, int column) {
		            return false;
		        }
		    };
		    table.setModel(tableModel);

		    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		    table.getTableHeader().setReorderingAllowed(false);

		    int columnCount = table.getColumnModel().getColumnCount();
		    int widthCount = columnWidths.length;
		    int columnsToConfigure = Math.min(columnCount, widthCount);

		    for (int i = 0; i < columnsToConfigure; i++) {
		        table.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
		    }


		    return table;
		}
	
	private void addRowSelectionListener(JTable table, ListSelectionListener selectionListener) {
        table.getSelectionModel().addListSelectionListener(selectionListener);
    }

	private void addDoubleClickListener(JTable table, MouseListener doubleClickListener) {
        table.addMouseListener(doubleClickListener);
    }
	
	private boolean removeInstructorFromInstructorMap(int id) {
		return instructorMap.remove(id) != null 
			? true 
			: false;
	}
	
	private void handleClickOnDeleteButton(ActionEvent ev) {
		if(!ObjectValidator.hasValue(selectedInstructor)) {
			warnThereIsNoInstructorSlected();
			return;
		}
		
		final int userResponse = askConfirmationBeforeDeletion();
		if (userResponse != 0) {
			return;
		}
		
		final boolean isDeleted = selectedInstructor.deleteFromDatabase((InstructorDAO) instructorDao);
		if(!isDeleted) {
			sendErrorWhileDeleting();
		}
		
		confirmDeletion();
		removeInstructorFromInstructorMap(selectedInstructor.getId());
		displayInstructorsInTable(instructorMap.values());
	}
	
	private void handleClickOnResetFiltersButton(ActionEvent ev) {
		displayInstructorsInTable(instructorMap.values());
		resetTextFields();
	}
	
	private void handleClickOnBackButton(ActionEvent e) {
		MainMenu mainMenuFrame = new MainMenu();
		mainMenuFrame.setVisible(true);
		
		dispose();
	}
}
