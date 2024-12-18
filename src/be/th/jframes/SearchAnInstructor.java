package be.th.jframes;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
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
import be.th.dao.LessonDAO;
import be.th.formatters.DatabaseFormatter;
import be.th.formatters.NumericFormatter;
import be.th.models.Accreditation;
import be.th.models.Instructor;
import be.th.models.Lesson;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.border.EtchedBorder;
import com.toedter.calendar.JDateChooser;

public class SearchAnInstructor extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private DAO<Instructor> instructorDAO;
	private DAO<Lesson> lessonDAO;
	
	private JPanel contentPane;
	private JTable instructorsTable;
	private JTable accreditationsTable;
	private JTextField idSearchTxtField;
	private LinkedHashMap<Integer, Instructor> instructorMap;
	private JTextField lastNameSearchTxtField;
	private JTextField firstNameSearchTxtField;
	private JDateChooser birthDateTextField;
	private JTextField emailSearchTxtField;
	private JTextField addressSearchTxtField;
	private JTextField phoneNumberTextField;
	private JTable upcomingLessonsTable;
	
	private Instructor selectedInstructor;

	public SearchAnInstructor() {
		DAOFactory daoFactory = new DAOFactory();
		
		this.instructorDAO = daoFactory.getInstructorDAO();
		this.lessonDAO = daoFactory.getLessonDAO();
		
		this.instructorMap = new LinkedHashMap<>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1539, 689);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel instructorsPanel = new JPanel();
		instructorsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Instructors", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		instructorsPanel.setBounds(272, 82, 977, 313);
		contentPane.add(instructorsPanel);
		instructorsPanel.setLayout(null);
		
		JScrollPane instructorsJScrollPane = new JScrollPane();
		instructorsJScrollPane.setBounds(10, 24, 957, 242);
		instructorsPanel.add(instructorsJScrollPane);
		
		Object[][] instructorsTableData = {}; 
		String[] instructorsTableColumnNames = { "Id", "Full name", "Birthdate", "Age", "Address", "Phone number", "Email", "Total revenu", "Revenue this month" };
		int[] instructorsTableColumnWidths = { 10, 85, 50, 15, 210, 60, 160, 60 };

		instructorsTable = createJTable(instructorsTableData, instructorsTableColumnNames, instructorsTableColumnWidths);
		MouseListener doubleClickListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        handleDoubleClickOnRows(e);
		    }
		};
		addRowSelectionListener(instructorsTable, this::handleClickOnRows);
		addDoubleClickListener(instructorsTable, doubleClickListener);
		
		instructorsJScrollPane.setViewportView(instructorsTable);
		
		JButton btnDeleteInstructor = new JButton("Delete");
		btnDeleteInstructor.setBounds(10, 271, 150, 31);
		instructorsPanel.add(btnDeleteInstructor);
		btnDeleteInstructor.addActionListener(this::handleClickOnDeleteButton);
		btnDeleteInstructor.setFont(FontStyles.BUTTON);
		btnDeleteInstructor.setBackground(ColorStyles.RED);
		
		JButton btnUpdateInformation = new JButton("Update");
		btnUpdateInformation.setBounds(170, 271, 150, 31);
		instructorsPanel.add(btnUpdateInformation);
		btnUpdateInformation.addActionListener(this::handleClickOnUpdateButton);
		btnUpdateInformation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnUpdateInformation.setBackground(ColorStyles.ORANGE);
		
		JLabel lblNewLabel = new JLabel("(or double click on the row...)");
		lblNewLabel.setBounds(330, 281, 196, 14);
		instructorsPanel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Search criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 82, 252, 313);
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
		
		JLabel lblTitle = new JLabel("Search for an instructor");
		lblTitle.setBounds(10, 28, 1501, 52);
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
		cancelBtn.setBounds(10, 28, 154, 52);
		contentPane.add(cancelBtn);
		
		JPanel accreditationsPanel = new JPanel();
		accreditationsPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Accreditations", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		accreditationsPanel.setBounds(1259, 82, 252, 313);
		contentPane.add(accreditationsPanel);
		accreditationsPanel.setLayout(null);
		
		JScrollPane accreditationsJScrollPane = new JScrollPane();
		accreditationsJScrollPane.setBounds(10, 21, 232, 281);
		accreditationsPanel.add(accreditationsJScrollPane);
		
		accreditationsTable = new JTable();
		accreditationsTable.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "Sport", "Category" }
		) {

			private static final long serialVersionUID = 1L;
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		accreditationsTable.getColumnModel().getColumn(0).setResizable(false);
		accreditationsTable.getColumnModel().getColumn(1).setResizable(false);
		accreditationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		accreditationsTable.getTableHeader().setReorderingAllowed(false);
		accreditationsJScrollPane.setViewportView(accreditationsTable);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Upcoming lessons", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(272, 406, 1239, 240);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 1219, 167);
		panel.add(scrollPane);
		
		upcomingLessonsTable = new JTable();
		String[] columnNames = { "Id", "Lesson type", "Start date", "Time until start", "Participants", "Availability", "Location", "Revenue" };
		boolean[] columnEditables = new boolean[columnNames.length];
		Arrays.fill(columnEditables, false); 
		upcomingLessonsTable.setModel(new DefaultTableModel(
		    new Object[][] {},
		    columnNames
		) {
		    private static final long serialVersionUID = 1L;
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return columnEditables[column];
		    }
		});

		for (int i = 0; i < columnNames.length; i++) {
		    upcomingLessonsTable.getColumnModel().getColumn(i).setResizable(false);
		}
        upcomingLessonsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        upcomingLessonsTable.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(upcomingLessonsTable);
		
		JButton btnDeleteUpcomingLesson = new JButton("Delete");
		btnDeleteUpcomingLesson.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDeleteUpcomingLesson.setBackground(new Color(255, 57, 57));
		btnDeleteUpcomingLesson.setBounds(10, 198, 150, 31);
		btnDeleteUpcomingLesson.addActionListener(this::handleClickOnDeleteUpcomingLessonButton);
		panel.add(btnDeleteUpcomingLesson);

				
		loadInstructorMap();
		displayInstructorsInTable(instructorMap.values());
	}
	
	private void warnThereIsNoInstructorSelected() {
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
			selectedInstructor.getFullNameFormattedForDisplay() + " has been successfully deleted.",
			"Success",
			JOptionPane.WARNING_MESSAGE
		);
	}
	
	private int askConfirmationBeforeDeletion() {
		return JOptionPane.showConfirmDialog(
			null, 
			"This is a critical operation. Are you sure that you want to delete " + 
			selectedInstructor.getFullNameFormattedForDisplay(),
			"Are you sure?",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		);
	}
	
	private void sendErrorWhileDeleting() {
		JOptionPane.showMessageDialog(
			null, 
			"Error while deleting " + 
			selectedInstructor.getFullNameFormattedForDisplay() + 
			".Please, try again later."
		);
	}
	
	private void displayLessonsInTable(Collection<Lesson> lessons) {
        DefaultTableModel model = (DefaultTableModel) upcomingLessonsTable.getModel();
		lessons = lessons.stream()
			.filter(lesson -> lesson.getDate().toLocalDate().isAfter(LocalDate.now()))
			.sorted((lesson1, lesson2) -> lesson1.getDate().compareTo(lesson2.getDate()))
			.collect(Collectors.toList());
		
        model.setRowCount(0);
        
        lessons.forEach(lesson -> model.addRow(getPreparedLessonInfoForTableModel(lesson)));
    }
	
	private void displayAccreditaionsInTable(Collection<Accreditation> accreditations) {		
		DefaultTableModel model = (DefaultTableModel) accreditationsTable.getModel();
		model.setRowCount(0);
		
		accreditations.forEach(accreditation -> model.addRow(getPreparedAccreditationsInfoForTableModel(accreditation)));
	}
	
	private void displayInstructorsInTable(Collection<Instructor> instructors) {
		DefaultTableModel model = (DefaultTableModel) instructorsTable.getModel();
		model.setRowCount(0);
		
		instructors.forEach(instructor -> model.addRow(getPreparedInstructorInfoForTableModel(instructor)));
	}	
	
	private void loadInstructorMap() {	
		List<Instructor> instructors = Instructor.findAllInDatabase((InstructorDAO) instructorDAO);
		
		if (!instructorMap.isEmpty()) {
			instructorMap.clear();
		}
		
		instructors.forEach(instructor -> instructorMap.put(instructor.getId(), instructor));
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
			instructor.getFullNameFormattedForDisplay(),
			DatabaseFormatter.toBelgianFormat(instructor.getDateOfBirth()),
			instructor.getCalculatedAgeFormattedForDisplay(),
			instructor.getAddress().getAddressFormattedForDisplay(),
			instructor.getPhoneNumber(),
			instructor.getEmail(),
			NumericFormatter.toCurrency(instructor.calculateGeneratedRevenue(), '€'),
			NumericFormatter.toCurrency(instructor.calculateGeneratedRevenueForCurrentMonthOfCurrentYear(), '€')
		};
	}
	
	private Object[] getPreparedAccreditationsInfoForTableModel(Accreditation accreditation) {
		return new Object[] {
			accreditation.getSportType(),
			accreditation.getAgeCategory()
		};
	}
	
	private Object[] getPreparedLessonInfoForTableModel(Lesson lesson) {
		return new Object[] { 
			lesson.getId(),
			lesson.getLessonType().getLessonTypeInfoFormattedForDisplay(), 
			DatabaseFormatter.toBelgianFormat(lesson.getDate()),
			lesson.getCalculatedDaysUntilStartDateFormattedForDisplay(),
			lesson.getBookingCount(),
			lesson.isFullyBooked() ? "Full" : "Available",
			lesson.getLocation().getName(),
			NumericFormatter.toCurrency(lesson.calculatePrice(), '€')
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
		
		int row = instructorsTable.rowAtPoint(e.getPoint());
		if (row != -1) { 
			openUpdateInstructorWindow();
		}
	}
	
	private void handleClickOnRows(ListSelectionEvent ev) {
	    if (ev.getValueIsAdjusting()) {
	        return;
	    }
	    
	    int selectedRow = instructorsTable.getSelectedRow();
	    
	    if (selectedRow < 0) { 
	        return;
	    }
	    
	    try {
	        int id = (int) instructorsTable.getValueAt(selectedRow, 0);
	        selectedInstructor = instructorMap.get(id);
	        selectedInstructor.calculateGeneratedRevenue();
	        displayAccreditaionsInTable(selectedInstructor.getAccreditations());    
	        displayLessonsInTable(selectedInstructor.getLessons());
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
	
	private void handleUpdateResult(Boolean isUpdated, Instructor updatedInstructor) {
        if (isUpdated) {
        	instructorMap.replace(updatedInstructor.getId(), updatedInstructor);
    		displayInstructorsInTable(instructorMap.values());
    		displayAccreditaionsInTable(updatedInstructor.getAccreditations());
        } 
    }
	

	private void handleClickOnUpdateButton(ActionEvent ev) {
		openUpdateInstructorWindow();
	}
	
	private void openUpdateInstructorWindow() {
		if(!ObjectValidator.hasValue(selectedInstructor)) {
			warnThereIsNoInstructorSelected();
			return;
		}
		
		UpdateAnInstructor updateAnInstructorFrame = new UpdateAnInstructor(selectedInstructor, this::handleUpdateResult);
		ClockElement.create(updateAnInstructorFrame);
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
			warnThereIsNoInstructorSelected();
			return;
		}
		
		final int userResponse = askConfirmationBeforeDeletion();
		if (userResponse != 0) {
			return;
		}
		
		final boolean isDeleted = selectedInstructor.deleteFromDatabase((InstructorDAO) instructorDAO);
		if(!isDeleted) {
			sendErrorWhileDeleting();
			return;
		}
		
		confirmDeletion();
		removeInstructorFromInstructorMap(selectedInstructor.getId());
		displayInstructorsInTable(instructorMap.values());
		displayAccreditaionsInTable(List.of());
		displayLessonsInTable(List.of());
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
	
	private void handleClickOnDeleteUpcomingLessonButton(ActionEvent ev) {
	    if (!validateSelectedInstructor()) {
	    	return;	    	
	    }

	    int selectedRow = upcomingLessonsTable.getSelectedRow();
	    if (!validateSelectedRow(selectedRow)) {
	    	return;
	    }

	    int lessonId = (int) upcomingLessonsTable.getValueAt(selectedRow, 0);
	    Lesson lesson = getSelectedLesson(lessonId);
	    if (!validateLesson(lesson)) {
	    	return;
	    }
	    
	    if (lesson.hasBooking()) {
	        if (!confirmWithBookings(lesson)) {
	        	return;
	        }
	    } else {
	        if (!confirmDeletionWithoutBookings()) {
	        	return;
	        }
	    }

	    if (!deleteLesson(lesson)) {
	    	return;
	    }

	    showSuccessMessage();
	    updateInstructorLessons(lesson);
	}

	private boolean validateSelectedInstructor() {
	    if (!ObjectValidator.hasValue(selectedInstructor)) {
	        warnThereIsNoInstructorSelected();
	        return false;
	    }
	    return true;
	}

	private boolean validateSelectedRow(int selectedRow) {
	    if (selectedRow < 0) {
	        JOptionPane.showMessageDialog(
	            null,
	            "Please, select a lesson to delete.",
	            "Watch out!",
	            JOptionPane.WARNING_MESSAGE
	        );
	        return false;
	    }
	    return true;
	}

	private Lesson getSelectedLesson(int lessonId) {
	    return instructorMap.get(selectedInstructor.getId()).findLessonById(lessonId);
	}

	private boolean validateLesson(Lesson lesson) {
	    if (!ObjectValidator.hasValue(lesson)) {
	        JOptionPane.showMessageDialog(
	            null,
	            "Lesson does not exist.",
	            "Error",
	            JOptionPane.ERROR_MESSAGE
	        );
	        return false;
	    }
	    return true;
	}

	private boolean confirmWithBookings(Lesson lesson) {
	    int bookingCount = lesson.getBookingCount();
	    String participantWord = bookingCount == 1 ? "participant" : "participants";

	    int choice = JOptionPane.showConfirmDialog(
	        null,
	        "This is a critical operation. This lesson has " + bookingCount + " " + participantWord +
	        " registered. If you pursue, their bookings will be deleted.",
	        "Warning",
	        JOptionPane.YES_NO_OPTION,
	        JOptionPane.WARNING_MESSAGE
	    );

	    return choice == JOptionPane.YES_OPTION;
	}

	private boolean confirmDeletionWithoutBookings() {
	    int choice = JOptionPane.showConfirmDialog(
	        null,
	        "This is a critical operation. Are you sure that you want to pursue? Once you've clicked 'yes', there's no turning back.",
	        "Warning",
	        JOptionPane.YES_NO_OPTION,
	        JOptionPane.WARNING_MESSAGE
	    );

	    return choice == JOptionPane.YES_OPTION;
	}

	private boolean deleteLesson(Lesson lesson) {
	    boolean isDeleted = lesson.deleteFromDatabase((LessonDAO) lessonDAO);

	    if (!isDeleted) {
	        JOptionPane.showMessageDialog(
	            null,
	            "Something went wrong... Please try again later",
	            "Error",
	            JOptionPane.ERROR_MESSAGE
	        );
	        return false;
	    }

	    return true;
	}

	private void showSuccessMessage() {
	    JOptionPane.showMessageDialog(
	        null,
	        "Lesson successfully deleted.",
	        "Success",
	        JOptionPane.PLAIN_MESSAGE
	    );
	}

	private void updateInstructorLessons(Lesson lesson) {
	    removeLessonFromInstructor(lesson, selectedInstructor);
	    displayLessonsInTable(selectedInstructor.getLessons());
	}

	private boolean removeLessonFromInstructor(Lesson lesson, Instructor instructor) {
	    return instructor.removeLesson(lesson);
	}

}
