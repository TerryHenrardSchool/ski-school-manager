package be.th.jframes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import be.th.dao.BookingDAO;
import be.th.dao.DAO;
import be.th.dao.DAOFactory;
import be.th.dao.LessonDAO;
import be.th.dao.LessonTypeDAO;
import be.th.dao.LocationDAO;
import be.th.dao.PeriodDAO;
import be.th.dao.SkierDAO;
import be.th.formatters.DatabaseFormatter;
import be.th.models.Booking;
import be.th.models.Instructor;
import be.th.models.Lesson;
import be.th.models.LessonType;
import be.th.models.Location;
import be.th.models.Period;
import be.th.models.Skier;
import be.th.parsers.DateParser;
import be.th.validators.DateValidator;
import be.th.validators.IntegerValidator;
import be.th.validators.ObjectValidator;
import be.th.validators.StringValidator;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.KeyStore.PrivateKeyEntry;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;

// Review release 1.3.0 
// Code pretty clean, could have split methods at some point but it's not a priority.
public class AddABooking extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable upcomingLessonsTable;
	private JCheckBox chckbxAddInsurance;
	
	private Skier selectedSkier;
	private Lesson selectedLesson;
	private LinkedHashMap<Integer, Lesson> unbookedUpcomingLessonsMap;
	private LinkedHashMap<String, LessonType> lessonTypesMap;
	private LinkedHashMap<String, Location> locationsMap;
	
	private DAO<Lesson> lessonDAO;
	private DAO<Period> periodDAO;
	private DAO<Booking> bookingDAO;
	private DAO<LessonType> lessonTypeDAO;
	private DAO<Location> locationDAO;
	
	private JTextField txtFieldSearchByLessonId;
	private JTextField txtFieldSearchByInstructorFullName;
	private JDateChooser dateChooserSearchByDate;
	private JComboBox<String> comboBoxSearchByLessonType;
	private JComboBox<String> comboBoxSearchByLocation;

	public AddABooking(Skier selectedSkier, BiConsumer<Boolean, Booking> onCreateCallback) {
		DAOFactory daoFactory = new DAOFactory();
		
		this.lessonDAO = daoFactory.getLessonDAO();
		this.periodDAO = daoFactory.getPeriodDAO();
		this.bookingDAO = daoFactory.getBookingDAO();
		this.lessonTypeDAO = daoFactory.getLessonTypeDAO();
		this.locationDAO = daoFactory.getLocationDAO();
		
		this.selectedSkier = selectedSkier;
		this.unbookedUpcomingLessonsMap = new LinkedHashMap<>();
		this.lessonTypesMap = new LinkedHashMap<>();
		this.locationsMap = new LinkedHashMap<>();
		
		loadLessonTypesMap();
		loadLocationsMap();
		loadUnbookedUpcommingLessonsMap();
				
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1232, 589);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Add a new booking to " + selectedSkier.getFullNameFormattedForDisplay());
		lblTitle.setOpaque(true);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblTitle.setBackground(new Color(0, 153, 255));
		lblTitle.setBounds(10, 11, 1196, 52);
		contentPane.add(lblTitle);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Booking information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 74, 1196, 465);
		contentPane.add(panel);
		panel.setLayout(null);
		
		chckbxAddInsurance = new JCheckBox("cover injuries (+20€)");
		chckbxAddInsurance.setBounds(106, 343, 229, 31);
		panel.add(chckbxAddInsurance);
		
		JLabel lblInsurance = new JLabel("Insurance");
		lblInsurance.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInsurance.setBounds(10, 341, 90, 31);
		panel.add(lblInsurance);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(null, "Upcoming lessons", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(345, 59, 841, 275);
		panel.add(panel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 821, 243);
		panel_1.add(scrollPane);
		
		MouseListener doubleClickListener = new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        handleSingleClickOnUpcomingLessonRows(e, upcomingLessonsTable);
		    }
		};
		
		upcomingLessonsTable = new JTable();
		upcomingLessonsTable.setModel(new DefaultTableModel(
			new Object[][] {},
			new String[] { "lesson id", "Lesson type", "instructor", "date", "Bookings left", "Location", "Price", }
		)
		{
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		});
		upcomingLessonsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		upcomingLessonsTable.getTableHeader().setReorderingAllowed(false);
		addDoubleClickListener(upcomingLessonsTable, doubleClickListener);

		scrollPane.setViewportView(upcomingLessonsTable);
		
		JLabel lblSelectALesson = new JLabel("Select an upcoming lesson (only those that haven't already been booked are displayed).");
		lblSelectALesson.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSelectALesson.setBounds(10, 21, 924, 31);
		panel.add(lblSelectALesson);
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(e -> handleClickOnCancelBtn());
		cancelBtn.setOpaque(true);
		cancelBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelBtn.setContentAreaFilled(true);
		cancelBtn.setBorderPainted(false);
		cancelBtn.setBackground(new Color(255, 57, 57));
		cancelBtn.setBounds(430, 403, 154, 51);
		panel.add(cancelBtn);
		
		JButton addBtn = new JButton("Add");
		addBtn.addActionListener(e -> handleClickOnAddBtn(onCreateCallback));
		addBtn.setOpaque(true);
		addBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addBtn.setContentAreaFilled(true);
		addBtn.setBorderPainted(false);
		addBtn.setBackground(new Color(139, 255, 96));
		addBtn.setBounds(613, 403, 154, 51);
		panel.add(addBtn);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Search criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(10, 59, 325, 275);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		JLabel lblSearchLessonId = new JLabel("Id:");
		lblSearchLessonId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchLessonId.setBounds(10, 23, 88, 31);
		panel_2.add(lblSearchLessonId);
		
		txtFieldSearchByLessonId = createFilterTextField();
		txtFieldSearchByLessonId.setToolTipText("");
		txtFieldSearchByLessonId.setColumns(10);
		txtFieldSearchByLessonId.setBounds(108, 25, 207, 31);
		panel_2.add(txtFieldSearchByLessonId);
		
		JLabel lblLessonType = new JLabel("Lesson type");
		lblLessonType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLessonType.setBounds(10, 65, 88, 31);
		panel_2.add(lblLessonType);
		
		txtFieldSearchByInstructorFullName = createFilterTextField();
		txtFieldSearchByInstructorFullName.setToolTipText("");
		txtFieldSearchByInstructorFullName.setColumns(10);
		txtFieldSearchByInstructorFullName.setBounds(108, 107, 110, 31);
		panel_2.add(txtFieldSearchByInstructorFullName);
		
		JLabel lblSearchByInstructorFullName = new JLabel("Instructor");
		lblSearchByInstructorFullName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchByInstructorFullName.setBounds(10, 107, 88, 31);
		panel_2.add(lblSearchByInstructorFullName);
		
		JLabel lblSearchByLessonDate = new JLabel("Date");
		lblSearchByLessonDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchByLessonDate.setBounds(10, 149, 88, 31);
		panel_2.add(lblSearchByLessonDate);
		
		JLabel lblSearchByLocation = new JLabel("Location");
		lblSearchByLocation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchByLocation.setBounds(10, 191, 88, 31);
		panel_2.add(lblSearchByLocation);
		
		comboBoxSearchByLessonType = createFilterComboBox(lessonTypesMap.keySet().stream().collect(Collectors.toList()));
		comboBoxSearchByLessonType.setBounds(108, 65, 207, 31);
		panel_2.add(comboBoxSearchByLessonType);
		
		comboBoxSearchByLocation = createFilterComboBox(locationsMap.keySet().stream().collect(Collectors.toList()));
		comboBoxSearchByLocation.setBounds(108, 191, 207, 31);
		panel_2.add(comboBoxSearchByLocation);
		
		dateChooserSearchByDate = createFilterDateChooser();
		dateChooserSearchByDate.setBounds(108, 149, 110, 31);
		panel_2.add(dateChooserSearchByDate);
		
		JButton btnResetSearch = new JButton("Reset");
		btnResetSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnResetSearch.addActionListener(this::handleCLickOnResetSearch);
		btnResetSearch.setBackground(new Color(0, 147, 255));
		btnResetSearch.setBounds(108, 233, 110, 31);
		panel_2.add(btnResetSearch);
		
		displayUpcomingLessons(unbookedUpcomingLessonsMap.values().stream()
		    .sorted(Comparator.comparing(Lesson::getDate))
		    .collect(Collectors.toList())
	    );
	}
	
	private void loadLocationsMap() {
		Collection<Location> locations = Location.findAllInDatabase((LocationDAO) locationDAO);
		locationsMap.clear();

		for (Location location : locations) {
			locationsMap.put(location.getName() + " (N° " + location.getId() + ")", location);
		}
	}
	
	private void loadLessonTypesMap() {
        Collection<LessonType> lessonTypes = LessonType.findAllInDatabase((LessonTypeDAO) lessonTypeDAO);
        lessonTypesMap.clear();

        for (LessonType lessonType : lessonTypes) {
            lessonTypesMap.put(lessonType.getLessonTypeInfoFormattedForDisplay(), lessonType);
        }
    }
	
	private void loadUnbookedUpcommingLessonsMap() {
	    List<Lesson> upcomingLessons = Lesson.findAllAfterDateInDatabase(LocalDate.now(), (LessonDAO) lessonDAO);

	    unbookedUpcomingLessonsMap.clear();

	    for (Lesson upcomingLesson : upcomingLessons) {
	        if (!selectedSkier.hasBookingForLesson(upcomingLesson)) {
	            unbookedUpcomingLessonsMap.put(upcomingLesson.getId(), upcomingLesson);
	        }
	    }
	}

	
	private void displayUpcomingLessons(Collection<Lesson> upcomingLessons) {
        DefaultTableModel model = (DefaultTableModel) upcomingLessonsTable.getModel();
        model.setRowCount(0);
        
        for (Lesson upcomingLesson : upcomingLessons) {
            model.addRow(getPreparedUpcomingLessonInfoForTableModel(upcomingLesson));
        }
    }
	
	private Object[] getPreparedUpcomingLessonInfoForTableModel(Lesson upcomingLesson) {
		return new Object[] {
			upcomingLesson.getId(),
			upcomingLesson.getLessonType().getLessonTypeInfoFormattedForDisplay(),
			upcomingLesson.getInstructor().getFullNameFormattedForDisplay(),
			DatabaseFormatter.toBelgianFormat(upcomingLesson.getDate().toLocalDate()),
			upcomingLesson.getRemainingBookingsCount(),
			upcomingLesson.getLocation().getName(),
			upcomingLesson.getLessonType().getPriceFormattedForDisplay()
		};
	}
	
	private void addDoubleClickListener(JTable table, MouseListener doubleClickListener) {
        table.addMouseListener(doubleClickListener);
    }
	
	private void handleSingleClickOnUpcomingLessonRows(MouseEvent e, JTable table) {
		int row = table.rowAtPoint(e.getPoint());
		if (row <= -1) { 
			return;
		}
		
		selectedLesson = unbookedUpcomingLessonsMap.get((Integer) table.getValueAt(row, 0));
	}
	
	private Period getCurrentPeriod() {
		return Period.findInDatabase(LocalDate.now(), (PeriodDAO) periodDAO);
	}
	
	private void handleClickOnCancelBtn() {
		dispose();
	}
	
	private Booking buildBookingFromFields() {
		Booking booking = new Booking(
			LocalDate.now().atStartOfDay(), 
			chckbxAddInsurance.isSelected(),
			getCurrentPeriod(),
			selectedLesson,
			selectedSkier
		);
		booking.setLesson(selectedLesson);
		
		return booking;
	}
	
	private void handleClickOnAddBtn(BiConsumer<Boolean, Booking> onCreateCallback) {
		if (!ObjectValidator.hasValue(selectedLesson)) {
			JOptionPane.showMessageDialog(null, "Please select a lesson to add a booking.", "Watch out", JOptionPane.WARNING_MESSAGE);
            return;
        }
			
		Booking newBooking = buildBookingFromFields(); 
		boolean isAdded = newBooking.insertIntoDatabase((BookingDAO) bookingDAO);
		if(!isAdded) {
			JOptionPane.showMessageDialog(null, "An error occurred while adding the booking. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		JOptionPane.showMessageDialog(null, "The booking has been successfully added.", "Success", JOptionPane.INFORMATION_MESSAGE);
		onCreateCallback.accept(isAdded, newBooking);
		dispose();
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
	
	private JDateChooser createFilterDateChooser() {
	    JDateChooser dateChooser = new JDateChooser();
	    dateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
	        applyFilters();
	    });
	    
	    return dateChooser;
	}
	
	private JComboBox<String> createFilterComboBox(List<String> items) {
		JComboBox<String> comboBox = new JComboBox<>();
		items.forEach(comboBox::addItem);
		comboBox.setSelectedIndex(-1);
		comboBox.addActionListener(e -> applyFilters());

		return comboBox;
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
	
	private void applyFilters() {
	    final Integer id = getNumberField(txtFieldSearchByLessonId, "id");
	    final String instructorFullName = getTextField(txtFieldSearchByInstructorFullName);
	    final LocalDate date = DateParser.toLocalDate(dateChooserSearchByDate.getDate());
	    final LessonType lessonType = lessonTypesMap.get(comboBoxSearchByLessonType.getSelectedItem());
	    final Location location = locationsMap.get(comboBoxSearchByLocation.getSelectedItem());

	    displayUpcomingLessons(searchUpcomingLesson(id, instructorFullName, date, lessonType, location));
	}
	
	private List<Lesson> searchUpcomingLesson(
		Integer id,
		String instructorFullName,
		LocalDate date,
		LessonType lessonType,
		Location location
	) {
	    if (IntegerValidator.hasValue(id)) {
	        final Lesson lesson = unbookedUpcomingLessonsMap.get(id);
	        
	        return ObjectValidator.hasValue(lesson) 
        		? List.of(lesson) 
				: List.of();
	    }

	    Stream<Lesson> stream = unbookedUpcomingLessonsMap.values().stream();

	    stream = applyFilter(stream, instructorFullName, lesson -> lesson.getInstructor().getFullNameFormattedForDisplay());
	    stream = applyFilter(stream, date, lesson -> lesson.getDate().toLocalDate());
	    
		if (ObjectValidator.hasValue(lessonType)) {
			stream = applyFilter(stream, String.valueOf(lessonType.getId()), lesson -> String.valueOf(lesson.getLessonType().getId()));
	    }
		
		if (ObjectValidator.hasValue(location)) {
			stream = applyFilter(stream, String.valueOf(location.getId()), lesson -> String.valueOf(lesson.getLocation().getId()));
		}

	    return stream.collect(Collectors.toList());
	}
	
	private Stream<Lesson> applyFilter(Stream<Lesson> stream, String searchValue, Function<Lesson, String> getter) {
	    if (!StringValidator.hasValue(searchValue)) {
	    	return stream;
	    }
	    
	    String searchValueLower = searchValue.toLowerCase();
        return stream.filter(lesson -> getter.apply(lesson).toLowerCase().contains(searchValueLower));
	}
	
	 private Stream<Lesson> applyFilter(Stream<Lesson> stream, LocalDate searchValue, Function<Lesson, LocalDate> getter) {
        if (!DateValidator.hasValue(searchValue)) {
            return stream;
        }

        return stream.filter(lesson -> getter.apply(lesson).equals(searchValue));
    }
	 
	private void handleCLickOnResetSearch(ActionEvent e) {
        displayUpcomingLessons(unbookedUpcomingLessonsMap.values());
        resetFilters();
    }
	
	private void resetFilters() {
		txtFieldSearchByLessonId.setText("");
        txtFieldSearchByInstructorFullName.setText("");
        dateChooserSearchByDate.setDate(null);
        comboBoxSearchByLessonType.setSelectedIndex(-1);
        comboBoxSearchByLocation.setSelectedIndex(-1);
	}
}
