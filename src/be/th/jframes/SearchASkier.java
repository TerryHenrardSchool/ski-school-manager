package be.th.jframes;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

import be.th.dao.BookingDAO;
import be.th.dao.DAO;
import be.th.dao.DAOFactory;
import be.th.dao.SkierDAO;
import be.th.formatters.DatabaseFormatter;
import be.th.models.Address;
import be.th.models.Booking;
import be.th.models.Instructor;
import be.th.models.Person;
import be.th.models.Skier;
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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.KeyStore.PrivateKeyEntry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.awt.event.ActionEvent;
import javax.swing.border.EtchedBorder;
import com.toedter.calendar.JDateChooser;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;

public class SearchASkier extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private DAO<Skier> skierDAO;
	private DAO<Booking> bookingDAO;
	
	private JPanel contentPane;
	private JTable skierTable;
	private JTable bookingTable;
	private JTextField idSearchTxtField;
	private JTextField lastNameSearchTxtField;
	private JTextField firstNameSearchTxtField;
	private JTextField emailSearchTxtField;
	private JTextField addressSearchTxtField;
	private JTextField phoneNumberTextField;
	private JDateChooser birthDateTextField;
	
	private LinkedHashMap<Integer, Skier> skierMap;
	
	private Booking selectedBooking;
	private Skier selectedSkier;
	private JTextField bookingIdSearchTxtField;
	private JTextField bookingInstructorFullNameSearchTxtField;
	private JDateChooser bookingLessonDateJDateChooser;
	private JRadioButton bookingIsInsuredYesRadioButton;
	private JRadioButton bookingIsInsuredNoRadioButton;
	private JRadioButton bookingIsInsuredNoneRadioButton;
	private ButtonGroup buttonGroup;

	public SearchASkier() {
		DAOFactory daoFactory = new DAOFactory();
		
		this.skierDAO = daoFactory.getSkierDAO();
		this.bookingDAO = daoFactory.getBookingDAO();
		
		skierMap = new LinkedHashMap<>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1539, 846);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Skiers", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(282, 64, 1239, 364);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 24, 1219, 287);
		panel.add(scrollPane);
		
		Object[][] data = {}; 
		String[] columnNames = { "Id", "Full name", "Birthdate", "Age", "Address", "Phone number", "Email" };
		int[] columnWidths = { 10, 75, 50, 20, 200, 80, 180 };

		skierTable = createJTable(data, columnNames, columnWidths);
		MouseListener doubleClickListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        handleDoubleClickOnRows(e);
		    }
		};
		addRowSelectionListener(skierTable, this::handleClickOnSkierRows);
		addDoubleClickListener(skierTable, doubleClickListener);

		scrollPane.setViewportView(skierTable);
		
		JButton btnDeleteSkier = new JButton("Delete");
		btnDeleteSkier.setBounds(130, 322, 110, 31);
		panel.add(btnDeleteSkier);
		btnDeleteSkier.addActionListener(this::handleClickOnDeleteSkierButton);
		btnDeleteSkier.setFont(FontStyles.BUTTON);
		btnDeleteSkier.setBackground(ColorStyles.RED);
		
		JButton btnUpdateInformation = new JButton("Update");
		btnUpdateInformation.setBounds(250, 322, 110, 31);
		panel.add(btnUpdateInformation);
		btnUpdateInformation.addActionListener(this::handleClickOnUpdateButton);
		btnUpdateInformation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnUpdateInformation.setBackground(ColorStyles.ORANGE);
		
		JLabel lblNewLabel = new JLabel("(or double click on the row...)");
		lblNewLabel.setBounds(370, 332, 196, 14);
		panel.add(lblNewLabel);
		
		JButton btnAddBooking = new JButton("Book");
		btnAddBooking.addActionListener(this::handleClickOnAddBookingButton);
		btnAddBooking.setBounds(10, 322, 110, 31);
		panel.add(btnAddBooking);
		btnAddBooking.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAddBooking.setBackground(Color.GREEN);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Skier search criteria", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(20, 64, 252, 364);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblSearchSkierId = new JLabel("Id:");
		lblSearchSkierId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchSkierId.setBounds(10, 24, 64, 25);
		panel_1.add(lblSearchSkierId);
		
		idSearchTxtField = createFilterSkierField();
		idSearchTxtField.setToolTipText("");
		idSearchTxtField.setBounds(130, 24, 110, 31);
		panel_1.add(idSearchTxtField);
		idSearchTxtField.setColumns(10);
		
		JLabel lblSearchLastName = new JLabel("Last name:");
		lblSearchLastName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchLastName.setBounds(10, 60, 85, 25);
		panel_1.add(lblSearchLastName);
		
		lastNameSearchTxtField = createFilterSkierField();
		lastNameSearchTxtField.setToolTipText("");
		lastNameSearchTxtField.setColumns(10);
		lastNameSearchTxtField.setBounds(130, 60, 110, 31);
		panel_1.add(lastNameSearchTxtField);
		
		JLabel lblSearchFirstName = new JLabel("First name:");
		lblSearchFirstName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchFirstName.setBounds(10, 96, 85, 25);
		panel_1.add(lblSearchFirstName);
		
		firstNameSearchTxtField = createFilterSkierField();
		firstNameSearchTxtField.setToolTipText("");
		firstNameSearchTxtField.setColumns(10);
		firstNameSearchTxtField.setBounds(130, 96, 110, 31);
		panel_1.add(firstNameSearchTxtField);
		
		JButton btnResetSkierSearchCriteria = new JButton("Reset");
		btnResetSkierSearchCriteria.setBackground(ColorStyles.BLUE);
		btnResetSkierSearchCriteria.addActionListener(this::handleClickOnResetSkierFiltersButton);
		btnResetSkierSearchCriteria.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnResetSkierSearchCriteria.setBounds(130, 322, 110, 31);
		panel_1.add(btnResetSkierSearchCriteria);
		
		JLabel lblSearchBirthdate = new JLabel("Birthdate:");
		lblSearchBirthdate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchBirthdate.setBounds(10, 131, 85, 25);
		panel_1.add(lblSearchBirthdate);
		
		JLabel lblSearchAddress = new JLabel("Address:");
		lblSearchAddress.setBounds(10, 166, 110, 25);
		panel_1.add(lblSearchAddress);
		lblSearchAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		addressSearchTxtField = createFilterSkierField();
		addressSearchTxtField.setBounds(130, 166, 110, 31);
		panel_1.add(addressSearchTxtField);
		addressSearchTxtField.setToolTipText("");
		addressSearchTxtField.setColumns(10);
		
		JLabel lblSearchPhoneNumber = new JLabel("Phone number:");
		lblSearchPhoneNumber.setBounds(10, 201, 110, 25);
		panel_1.add(lblSearchPhoneNumber);
		lblSearchPhoneNumber.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		phoneNumberTextField = createFilterSkierField();
		phoneNumberTextField.setBounds(130, 201, 110, 31);
		panel_1.add(phoneNumberTextField);
		phoneNumberTextField.setToolTipText("");
		phoneNumberTextField.setColumns(10);
		
		JLabel lblSearchEmailAddress = new JLabel("Email:");
		lblSearchEmailAddress.setBounds(10, 236, 110, 25);
		panel_1.add(lblSearchEmailAddress);
		lblSearchEmailAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		emailSearchTxtField = createFilterSkierField();
		emailSearchTxtField.setBounds(130, 236, 110, 31);
		panel_1.add(emailSearchTxtField);
		emailSearchTxtField.setToolTipText("");
		emailSearchTxtField.setColumns(10);
		
		birthDateTextField = createFilterSkierJDateChooser();
		birthDateTextField.setBounds(130, 131, 110, 31);
		panel_1.add(birthDateTextField);
		
		JLabel lblTitle = new JLabel("Search for a skier");
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
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Bookings for upcoming lessons", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(282, 439, 1231, 364);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 25, 1211, 286);
		panel_2.add(scrollPane_1);
		
		bookingTable = new JTable();
		bookingTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"id", "Insurance", "Lesson type", "Instructor", "Lesson date", "Days before start", "Price", "location"
			}
		){
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		int columnCount = bookingTable.getColumnModel().getColumnCount();
		for (int i = 0; i < columnCount; i++) {
		    bookingTable.getColumnModel().getColumn(i).setResizable(false);
		}
		bookingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookingTable.getTableHeader().setReorderingAllowed(false);
		addRowSelectionListener(bookingTable, this::handleClickOnBookingRows);
		scrollPane_1.setViewportView(bookingTable);
		
		JButton btnDeleteSkierBooking = new JButton("Delete");
		btnDeleteSkierBooking.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDeleteSkierBooking.setBackground(new Color(255, 57, 57));
		btnDeleteSkierBooking.setBounds(10, 322, 110, 31);
		btnDeleteSkierBooking.addActionListener(this::handleClickOnDeleteBookingButton);
		panel_2.add(btnDeleteSkierBooking);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Booking search criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(20, 439, 252, 364);
		contentPane.add(panel_3);
		panel_3.setLayout(null);
		
		JButton btnResetBookingSearchCriteria = new JButton("Reset");
		btnResetBookingSearchCriteria.addActionListener(this::handleClickOnResetBookingFiltersButton);
		btnResetBookingSearchCriteria.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnResetBookingSearchCriteria.setBackground(new Color(0, 147, 255));
		btnResetBookingSearchCriteria.setBounds(132, 322, 110, 31);
		panel_3.add(btnResetBookingSearchCriteria);
		
		JLabel lblSearchBookingByBookingId = new JLabel("Id:");
		lblSearchBookingByBookingId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchBookingByBookingId.setBounds(10, 21, 64, 25);
		panel_3.add(lblSearchBookingByBookingId);
		
		JLabel lblSearchBookingByIsInsured = new JLabel("Insured:");
		lblSearchBookingByIsInsured.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchBookingByIsInsured.setBounds(10, 57, 64, 25);
		panel_3.add(lblSearchBookingByIsInsured);
		
		JLabel lblSearchBookingByInstructorFullName = new JLabel("Instructor:");
		lblSearchBookingByInstructorFullName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchBookingByInstructorFullName.setBounds(10, 93, 88, 25);
		panel_3.add(lblSearchBookingByInstructorFullName);
		
		bookingIdSearchTxtField = createFilterBookingField();
		bookingIdSearchTxtField.setToolTipText("");
		bookingIdSearchTxtField.setColumns(10);
		bookingIdSearchTxtField.setBounds(132, 21, 110, 31);
		panel_3.add(bookingIdSearchTxtField);
		
		bookingInstructorFullNameSearchTxtField = createFilterBookingField();
		bookingInstructorFullNameSearchTxtField.setToolTipText("");
		bookingInstructorFullNameSearchTxtField.setColumns(10);
		bookingInstructorFullNameSearchTxtField.setBounds(132, 93, 110, 31);
		panel_3.add(bookingInstructorFullNameSearchTxtField);
		
		JLabel lblSearchBookingByLessonDate = new JLabel("Lesson date:");
		lblSearchBookingByLessonDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchBookingByLessonDate.setBounds(10, 129, 88, 25);
		panel_3.add(lblSearchBookingByLessonDate);
		
		bookingLessonDateJDateChooser = createFilterBookingJDateChooser();
		bookingLessonDateJDateChooser.setBounds(132, 129, 110, 31);
		panel_3.add(bookingLessonDateJDateChooser);
		
		bookingIsInsuredYesRadioButton = createFilterBookingRadioButton("yes");
		bookingIsInsuredYesRadioButton.setBounds(90, 60, 50, 23);
		panel_3.add(bookingIsInsuredYesRadioButton);
		
		bookingIsInsuredNoRadioButton = createFilterBookingRadioButton("no");
		bookingIsInsuredNoRadioButton.setBounds(145, 60, 48, 23);
		panel_3.add(bookingIsInsuredNoRadioButton);
		
		bookingIsInsuredNoneRadioButton = createFilterBookingRadioButton("none");
		bookingIsInsuredNoneRadioButton.setBounds(190, 60, 55, 23);
		panel_3.add(bookingIsInsuredNoneRadioButton);
		
		buttonGroup = new ButtonGroup();
		buttonGroup.add(bookingIsInsuredYesRadioButton);
		buttonGroup.add(bookingIsInsuredNoRadioButton);
		buttonGroup.add(bookingIsInsuredNoneRadioButton);
				
		loadSkierMap();
		displaySkiersInTable(skierMap.values());
	}
	
	private Collection<Booking> sortBookingsByDate(Collection<Booking> bookings) {
	    return bookings
	        .stream()
	        .sorted((booking1, booking2) -> {
	            LocalDateTime date1 = booking1.getLesson().getDate();
	            LocalDateTime date2 = booking2.getLesson().getDate();
	            LocalDateTime today = LocalDateTime.now();
	
	            if (
	        		date1.isBefore(today) && date2.isBefore(today) || 
	                date1.isAfter(today) && date2.isAfter(today) || 
	                date1.isEqual(today) && date2.isEqual(today)
	            ) {
	                return date1.compareTo(date2);
	            }
	
	            if (date1.isBefore(today)) {
	                return 1; 
	            } else {
	                return -1; 
	            }
	        })
	        .collect(Collectors.toList());
	}

	
	private void displayBookingsInTable(Collection<Booking> bookings) {
        DefaultTableModel model = (DefaultTableModel) bookingTable.getModel();
        model.setRowCount(0);
        
        bookings = sortBookingsByDate(bookings);
        
        for (final Booking booking : bookings) {
            model.addRow(getPreparedBookingInfoForTableModel(booking));
        }
    }
	
	private Object[] getPreparedBookingInfoForTableModel(Booking booking) {
		return new Object[] { 
			booking.getId(), 
			booking.getInsurance() ? "Yes" : "No",
			booking.getLesson().getLessonType().getLessonTypeInfoFormattedForDisplay(), 
			booking.getLesson().getInstructor().getFullNameFormattedForDisplay(),
			DatabaseFormatter.toBelgianFormat(booking.getLesson().getDate()), 
			booking.getLesson().calculateDaysUntilStartDate() + " days",
			booking.calculatePrice(), 
			booking.getLesson().getLocation().getName() 
		};
	}
	
	private void warnThereIsNoSkierSlected() {
		JOptionPane.showMessageDialog(
			null, 
			"Please, select a skier.", 
			"Watch out!",
			JOptionPane.WARNING_MESSAGE
		);
	}
	
	private void confirmDeletion(String message) {
		JOptionPane.showMessageDialog(
			null, 
			message,
			"Success",
			JOptionPane.INFORMATION_MESSAGE
		);
	}
	
	private int askConfirmationBeforeDeletion(String message) {
		return JOptionPane.showConfirmDialog(
			null, 
			message,
			"Are you sure?",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		);
	}
	
	private void sendErrorWhileDeleting() {
		JOptionPane.showMessageDialog(
			null, 
			"Error while deleting " + 
			selectedSkier.getLastnameFormattedForDisplay() + " " + 
			selectedSkier.getFirstNameFormattedForDisplay() + 
			".Please, try again later."
		);
	}
	
	private void displaySkiersInTable(Collection<Skier> skiers) {
		DefaultTableModel model = (DefaultTableModel) skierTable.getModel();
		model.setRowCount(0);
		
		for (final Skier skier : skiers) {
			model.addRow(getPreparedSkierInfoForTableModel(skier));
		}
	}
	
	private void loadSkierMap() {	
		List<Skier> skiers = Skier.findAllInDatabase((SkierDAO) skierDAO);
		
		skierMap.clear();			
		
		for (final Skier skier : skiers) {
            skierMap.put(skier.getId(), skier);
        }
	}
	
	private List<Booking> searchBookings(
		Integer id,
		String instructorFullName,
		Optional<Boolean> isInsured,
		LocalDate lessonDate
	) {
		if (!ObjectValidator.hasValue(selectedSkier)) {
			return List.of();
		}
		
		if (IntegerValidator.hasValue(id)) {
			final Booking booking = skierMap
				.get(selectedSkier.getId())
				.getBookings()
				.stream()
				.filter(b -> b.getId() == id)
				.findFirst()
				.orElse(null);
			
			return ObjectValidator.hasValue(booking) 
				? List.of(booking) 
				: List.of();
		}
		
		Stream<Booking> stream = skierMap.get(selectedSkier.getId()).getBookings().stream();
		
		
		if (isInsured.isPresent()) {
			stream = applyFilter(stream, isInsured.get(), Booking::getInsurance);
		}
			
		stream = applyFilter(stream, instructorFullName, booking -> booking.getLesson().getInstructor().getFullNameFormattedForDisplay());
		stream = applyFilter(stream, lessonDate, booking -> booking.getLesson().getDate().toLocalDate());
		
		return stream.collect(Collectors.toList());
	}
	
	private List<Skier> searchSkiers(
		Integer id, 
		String lastName, 
		String firstName, 
		LocalDate birthdate,
		String email, 
		String address, 
		String phoneNumber
	) {
	    if (IntegerValidator.hasValue(id)) {
	        final Skier skier = skierMap.get(id);
	        
	        return ObjectValidator.hasValue(skier) 
        		? List.of(skier) 
				: List.of();
	    }

	    Stream<Skier> stream = skierMap.values().stream();

	    stream = applyFilter(stream, lastName, Skier::getLastName);
	    stream = applyFilter(stream, firstName, Skier::getFirstName);
	    stream = applyFilter(stream, birthdate, Skier::getDateOfBirth);
	    stream = applyFilter(stream, email, Skier::getEmail);
	    stream = applyFilter(stream, address, skier -> skier.getAddress().getAddressFormattedForDisplay());
	    stream = applyFilter(stream, phoneNumber, Skier::getPhoneNumber);

	    return stream.collect(Collectors.toList());
	}

	private <T> Stream<T> applyFilter(Stream<T> stream, String searchValue, Function<T, String> getter) {
		if (!StringValidator.hasValue(searchValue)) {
			return stream;
		}
		
		String searchValueLower = searchValue.toLowerCase();
		return stream.filter(element -> getter.apply(element).toLowerCase().contains(searchValueLower));
	}
	
	private <T> Stream<T> applyFilter(Stream<T> stream, boolean searchValue, Function<T, Boolean> getter) {
	    return stream.filter(element -> getter.apply(element) == searchValue);
	}
	
	 private <T> Stream<T> applyFilter(Stream<T> stream, LocalDate searchValue, Function<T, LocalDate> getter) {
        if (!DateValidator.hasValue(searchValue)) {
            return stream;
        }

        return stream.filter(element -> getter.apply(element).equals(searchValue));
    }
	 
	 private Optional<Boolean> getSelectedInsurance() {
	    if (bookingIsInsuredYesRadioButton.isSelected()) {
	        return Optional.of(true);
	    } else if (bookingIsInsuredNoRadioButton.isSelected()) {
	        return Optional.of(false);
	    }
	    
	    return Optional.empty();
	}

	 
	 private void applyBookingFilters() {
	    final Integer id = getNumberField(bookingIdSearchTxtField, "id");
	    final String instructorFullName = getTextField(bookingInstructorFullNameSearchTxtField);
	    final Optional<Boolean> isInsured = getSelectedInsurance(); 
	    final LocalDate lessonDate = DateParser.toLocalDate(bookingLessonDateJDateChooser.getDate());

	    displayBookingsInTable(searchBookings(id, instructorFullName, isInsured, lessonDate));
	}

	 
	 private void applySkierFilters() {
	    final Integer id = getNumberField(idSearchTxtField, "id");
	    final String lastName = getTextField(lastNameSearchTxtField);
	    final String firstName = getTextField(firstNameSearchTxtField);
	    final LocalDate birthdate = DateParser.toLocalDate(birthDateTextField.getDate());
	    final String email = getTextField(emailSearchTxtField);
	    final String address = getTextField(addressSearchTxtField);
	    final String phoneNumber = getTextField(phoneNumberTextField);

	    displaySkiersInTable(searchSkiers(id, lastName, firstName, birthdate, email, address, phoneNumber));
	}

	
	private Object[] getPreparedSkierInfoForTableModel(Skier skier) {
		return new Object[] {
			skier.getId(),
			skier.getFullNameFormattedForDisplay(),
			DatabaseFormatter.toBelgianFormat(skier.getDateOfBirth()),
			skier.getAgeFormattedForDisplay(),
			skier.getAddress().getAddressFormattedForDisplay(),
			skier.getPhoneNumber(),
			skier.getEmail()
		};
	}
	
	private String getTextField(JTextField textField) {
		final String texte = textField.getText();
		
		return texte.isBlank() ? null : texte.trim();
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
	
	private void resetSkierTextFields() {
		idSearchTxtField.setText("");
		lastNameSearchTxtField.setText("");
		firstNameSearchTxtField.setText("");
		birthDateTextField.setDate(null);
		emailSearchTxtField.setText("");
		addressSearchTxtField.setText("");
		phoneNumberTextField.setText("");
	}
	
	private JDateChooser createFilterBookingJDateChooser() {
	    JDateChooser dateChooser = new JDateChooser();
	    dateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
	        applyBookingFilters();
	    });
	    
	    return dateChooser;
	}
	
	private JRadioButton createFilterBookingRadioButton(String message) {
	    JRadioButton radioButton = new JRadioButton(message);
	    radioButton.addActionListener(e -> applyBookingFilters());
	    return radioButton;
	}
	
	private JTextField createFilterBookingField() {
	    JTextField textField = new JTextField();
	    textField.getDocument().addDocumentListener(new DocumentListener() {
	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        	applyBookingFilters();
	        }

	        @Override
	        public void removeUpdate(DocumentEvent e) {
	        	applyBookingFilters();
	        }

	        @Override
	        public void changedUpdate(DocumentEvent e) {
	        	applyBookingFilters();
	        }
	    });
	    
	    return textField;
	}
	
	private JTextField createFilterSkierField() {
	    JTextField textField = new JTextField();
	    textField.getDocument().addDocumentListener(new DocumentListener() {
	        @Override
	        public void insertUpdate(DocumentEvent e) {
	            applySkierFilters();
	        }

	        @Override
	        public void removeUpdate(DocumentEvent e) {
	            applySkierFilters();
	        }

	        @Override
	        public void changedUpdate(DocumentEvent e) {
	            applySkierFilters();
	        }
	    });
	    
	    return textField;
	}
	
	private JDateChooser createFilterSkierJDateChooser() {
	    JDateChooser dateChooser = new JDateChooser();
	    dateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
	        applySkierFilters();
	    });
	    
	    return dateChooser;
	}
	
	private void handleDoubleClickOnRows(MouseEvent e) {
		if(e.getClickCount() != 2) {
			return;
		}
		
		int row = skierTable.rowAtPoint(e.getPoint());
		if (row != -1) { 
			openUpdateSkierWindow();
		}
	}
	
	private void handleClickOnSkierRows(ListSelectionEvent ev) {
	    if (ev.getValueIsAdjusting()) {
	        return;
	    }
	    
	    int selectedRow = skierTable.getSelectedRow();
	    
	    if (selectedRow < 0) { 
	        return;
	    }
	    
	    try {
	        int id = (int) skierTable.getValueAt(selectedRow, 0);
	        
	        selectedSkier = skierMap.get(id);
	        displayBookingsInTable(selectedSkier.getBookings());
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error while fetching skier data.", "Erreur", JOptionPane.ERROR_MESSAGE);
	    }
	}
	
	private void handleUpdateResult(Boolean isUpdated, Skier updatedSkier) {
        if (isUpdated) {        	
        	skierMap.replace(updatedSkier.getId(), updatedSkier);
    		displaySkiersInTable(skierMap.values());
        } 
    }
	
	private void handleClickOnUpdateButton(ActionEvent ev) {
		openUpdateSkierWindow();
	}
	
	private void openUpdateSkierWindow() {
		if(!ObjectValidator.hasValue(selectedSkier)) {
			showError(getName());
			return;
		}
		
		UpdateASkier updateASkierFrame = new UpdateASkier(selectedSkier, this::handleUpdateResult);
		updateASkierFrame.setVisible(true);
	}
	
	private boolean removeSkierFromSkiermap(int id) {
		return skierMap.remove(id) != null 
			? true 
			: false;
	}
	
	private boolean removeBookingFromSkier(Booking booking) {
		return selectedSkier.removeBooking(booking);
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
    
	private void handleClickOnDeleteSkierButton(ActionEvent ev) {
		if(!ObjectValidator.hasValue(selectedSkier)) {
			warnThereIsNoSkierSlected();
			return;
		}
		
		final int userResponse = askConfirmationBeforeDeletion(
			"This is a critical operation. Are you sure that you want to delete " + 
			selectedSkier.getLastnameFormattedForDisplay() + " " + 
			selectedSkier.getFirstNameFormattedForDisplay()
		);
		if (userResponse != 0) {
			return;
		}
		
		final boolean isDeleted = selectedSkier.deleteFromDatabase((SkierDAO) skierDAO);
		if(!isDeleted) {
			sendErrorWhileDeleting();
			return;
		}
		
		confirmDeletion(selectedSkier.getFullNameFormattedForDisplay() + " has been successfully deleted.");
		removeSkierFromSkiermap(selectedSkier.getId());
		displaySkiersInTable(skierMap.values());
		displayBookingsInTable(List.of());
	}
    
	private void handleClickOnResetSkierFiltersButton(ActionEvent ev) {
		displaySkiersInTable(skierMap.values());
		resetSkierTextFields();
	}
	
	private void resetBookingTextFields() {
		bookingIdSearchTxtField.setText("");
		bookingInstructorFullNameSearchTxtField.setText("");
		bookingLessonDateJDateChooser.setDate(null);
		buttonGroup.clearSelection();
	}
	
	private void handleClickOnResetBookingFiltersButton(ActionEvent ev) {
		if (!ObjectValidator.hasValue(selectedSkier)) {
			return;
		}
		
		resetBookingTextFields();
		displayBookingsInTable(selectedSkier.getBookings());
	}
    
	private void handleClickOnBackButton(ActionEvent e) {
		new MainMenu().setVisible(true);
		dispose();
	}
	
	private void handleClickOnAddBookingButton(ActionEvent e) {
		if (!ObjectValidator.hasValue(selectedSkier)) {
			JOptionPane.showMessageDialog(null, "Please select a skier", "Watch out", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		new AddABooking(selectedSkier, this::handleCreateCallBackResult).setVisible(true);
	}
	
	private void handleCreateCallBackResult(Boolean isCreated, Booking booking) {
		if (isCreated) {
			skierMap.get(booking.getSkier().getId()).addBooking(booking);
			displayBookingsInTable(selectedSkier.getBookings());
		}
	}
	
	private void handleClickOnDeleteBookingButton(ActionEvent ev) {
		if(!ObjectValidator.hasValue(selectedBooking)) {
			showError("Please select a booking.");
			return;
		}
		
		final int userResponse = askConfirmationBeforeDeletion(
			"This is a critical operation. Are you sure that you want to delete " + 
			selectedSkier.getLastnameFormattedForDisplay() + " " + 
			selectedSkier.getFirstNameFormattedForDisplay() + "'s booking?"
		);
		if (userResponse != 0) {
			return;
		}
		
		final boolean isDeleted = selectedBooking.deleteFromDatabase((BookingDAO) bookingDAO);
		if(!isDeleted) {
			showError(
				"Error while deleting " +
				selectedSkier.getFullNameFormattedForDisplay() + "'s booking."
			);
			return;
		}
		
		confirmDeletion(selectedSkier.getFullNameFormattedForDisplay() + "'s booking has been successfully deleted.");
		removeBookingFromSkier(selectedBooking);
		displayBookingsInTable(skierMap.get(selectedSkier.getId()).getBookings());
	}
	
	private void handleClickOnBookingRows(ListSelectionEvent ev) {
	    if (ev.getValueIsAdjusting()) {
	        return;
	    }

	    int selectedRow = bookingTable.getSelectedRow();
	    if (selectedRow < 0) {
	        return;
	    }

	    try {
	        int bookingId = getBookingIdFromRow(selectedRow);
	        selectedBooking = selectedSkier.findBookingById(bookingId);
	    } catch (Exception ex) {
	        ex.printStackTrace();
	        showError("Error while fetching skier data.");
	    }
	}

	private int getBookingIdFromRow(int row) {
	    return (int) bookingTable.getValueAt(row, 0);
	}

	private void showError(String message) {
	    JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
