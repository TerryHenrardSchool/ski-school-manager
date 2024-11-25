package be.th.jframes;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import be.th.dao.DAO;
import be.th.dao.DAOFactory;
import be.th.dao.InstructorDAO;
import be.th.dao.LessonDAO;
import be.th.dao.LessonTypeDAO;
import be.th.dao.LocationDAO;
import be.th.formatters.DatabaseFormatter;
import be.th.models.Accreditation;
import be.th.models.Instructor;
import be.th.models.Lesson;
import be.th.models.LessonType;
import be.th.models.Location;
import be.th.models.Period;
import be.th.parsers.DateParser;
import be.th.validators.ObjectValidator;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.print.event.PrintJobAttributeEvent;
import javax.swing.AbstractButton;
import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import java.awt.Cursor;
import javax.swing.DebugGraphics;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.awt.event.ActionEvent;

public class AddALesson extends JFrame {

	private static final long serialVersionUID = 1L;
	
	DAO<LessonType> lessonTypeDAO;
	DAO<Instructor> instructorDAO;
	DAO<Location> locationDAO;
	DAO<Lesson> lessonDAO;	
	
	private Map<String, LessonType> lessonTypeMap;
	private Map<String, Instructor> instructorMap;
	private Map<String, Location> locationMap;
	
	private JPanel contentPane;
	private JComboBox<String> lessonTypeComboBox;
	private JComboBox<String> eligibleInstructorsComboBox;
	private JComboBox<String> locationComboBox;
	private JDateChooser startDateField;
	private JRadioButton rdbtnMorningLesson;
	private JRadioButton rdbtnAfternoonLesson;
	private ButtonGroup startHourLessonRadioButtonGroup;
	private JRadioButton rdbtnPrivateLessonHour;
	private List<AbstractButton> privateLessonRadioButtonGroup;
	private List<AbstractButton> groupLessonRadioButtonGroup;

	public AddALesson() {
		DAOFactory daoFactory = new DAOFactory();
		
		this.lessonTypeDAO = daoFactory.getLessonTypeDAO();
		this.instructorDAO = daoFactory.getInstructorDAO();
		this.locationDAO = daoFactory.getLocationDAO();
		this.lessonDAO = daoFactory.getLessonDAO();
        this.lessonTypeMap = new HashMap<>();
        this.instructorMap = new HashMap<>();
        this.locationMap = new HashMap<>();
        
        loadLessonTypeMap();
        loadInstructorMap();
        loadLocationMap();
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Create a new lesson");
		lblTitle.setOpaque(true);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblTitle.setBackground(new Color(0, 153, 255));
		lblTitle.setBounds(10, 11, 640, 52);
		contentPane.add(lblTitle);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Lesson information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 74, 640, 326);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblSport = new JLabel("Lesson type");
		lblSport.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSport.setBounds(340, 100, 90, 31);
		panel.add(lblSport);
		
		lessonTypeComboBox = new JComboBox<String>();
		
		lessonTypeComboBox.setBounds(440, 100, 190, 31);
		lessonTypeComboBox.setRenderer(createLessonTypeRenderer(lessonTypeMap, instructorMap.values()));
		panel.add(lessonTypeComboBox);
		
		setModelToJComboBox(lessonTypeComboBox, lessonTypeMap.keySet().stream().sorted((key1, key2) -> key1.compareTo(key2)).toArray(String[]::new));
		lessonTypeComboBox.setSelectedIndex(-1); 
		lessonTypeComboBox.addActionListener(e -> handleLessonTypeSelection());
		
		JLabel lblInstructor = new JLabel("Instructor");
		lblInstructor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInstructor.setBounds(340, 194, 90, 31);
		panel.add(lblInstructor);
		
		eligibleInstructorsComboBox = new JComboBox<String>();
		eligibleInstructorsComboBox.setEnabled(false);
		eligibleInstructorsComboBox.setSelectedIndex(-1);
		eligibleInstructorsComboBox.setBounds(440, 194, 190, 31);
		panel.add(eligibleInstructorsComboBox);
		
		JTextArea txtrAGrayLesson = new JTextArea();
		txtrAGrayLesson.setEditable(false);
		txtrAGrayLesson.setFont(new Font("Tahoma", Font.PLAIN, 11));
		txtrAGrayLesson.setBackground(UIManager.getColor("Button.background"));
		txtrAGrayLesson.setWrapStyleWord(true);
		txtrAGrayLesson.setLineWrap(true);
		txtrAGrayLesson.setText("A gray lesson type indicates no eligible instructor for this lesson type at this date.");
		txtrAGrayLesson.setBounds(340, 58, 290, 31);
		panel.add(txtrAGrayLesson);
		
		locationComboBox = new JComboBox<String>();
		locationComboBox.setSelectedIndex(-1);
		locationComboBox.setEnabled(false);
		locationComboBox.setBounds(120, 194, 190, 31);
		panel.add(locationComboBox);
		
		JLabel lblLocation = new JLabel("Location");
		lblLocation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLocation.setBounds(10, 194, 90, 31);
		panel.add(lblLocation);
		
		startDateField = new JDateChooser();
	    startDateField.setBounds(120, 100, 190, 31);
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DAY_OF_YEAR, 1); 
	    startDateField.setDate(calendar.getTime()); 
	    startDateField.addPropertyChangeListener("date", new PropertyChangeListener() {
	        @Override
	        public void propertyChange(PropertyChangeEvent evt) {
	        	setComponentsEnabled(List.of(eligibleInstructorsComboBox, locationComboBox), false);
	        	setGroupDisabled(getButtonsFromGroup(startHourLessonRadioButtonGroup));
	            clearJComboBox(List.of(eligibleInstructorsComboBox, locationComboBox));
	        }
	    });

	    panel.add(startDateField);
		
		JLabel lblStartDate = new JLabel("Start date");
		lblStartDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblStartDate.setBounds(10, 100, 90, 31);
		panel.add(lblStartDate);
		
		JButton createBtn = new JButton("Create");
		createBtn.addActionListener(e -> handleClickOnCreateButton());
		createBtn.setOpaque(true);
		createBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		createBtn.setContentAreaFilled(true);
		createBtn.setBorderPainted(false);
		createBtn.setBackground(new Color(139, 255, 96));
		createBtn.setBounds(340, 264, 154, 51);
		panel.add(createBtn);
		
		JButton cancelBtn = new JButton("Cancel");
		cancelBtn.addActionListener(e -> handleClickOnCancelButton());
		cancelBtn.setOpaque(true);
		cancelBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelBtn.setContentAreaFilled(true);
		cancelBtn.setBorderPainted(false);
		cancelBtn.setBackground(new Color(255, 57, 57));
		cancelBtn.setBounds(146, 264, 154, 51);
		panel.add(cancelBtn);
		
		rdbtnMorningLesson = new JRadioButton("Morning (09h - 12h)");
		rdbtnMorningLesson.setBounds(120, 138, 190, 23);
		rdbtnMorningLesson.setSelected(true);
		rdbtnMorningLesson.addActionListener(e -> handleRadioButtonSelection(rdbtnMorningLesson.getModel()));
		panel.add(rdbtnMorningLesson);
		
		rdbtnAfternoonLesson = new JRadioButton("Afternoon (14h - 17h)");
		rdbtnAfternoonLesson.setBounds(120, 164, 190, 23);
		rdbtnAfternoonLesson.addActionListener(e -> handleRadioButtonSelection(rdbtnAfternoonLesson.getModel()));
		panel.add(rdbtnAfternoonLesson);
		
		
		JLabel lblOpeningDates = new JLabel(
			"The ski school is open from " + DatabaseFormatter.toBelgianFormat(Period.SKI_SCHOOL_OPENING_DATE)  + 
			" to " + DatabaseFormatter.toBelgianFormat(Period.SKI_SCHOOL_CLOSING_DATE)
		);
		lblOpeningDates.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpeningDates.setFont(new Font("Leelawadee UI Semilight", Font.PLAIN, 16));
		lblOpeningDates.setBounds(10, 11, 620, 45);
		panel.add(lblOpeningDates);
		
		JLabel lblGroupLessonHours = new JLabel("Group lessons");
		lblGroupLessonHours.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblGroupLessonHours.setBounds(10, 138, 105, 24);
		panel.add(lblGroupLessonHours);
		
		JLabel lblPrivateLessonHours = new JLabel("Private lesson");
		lblPrivateLessonHours.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPrivateLessonHours.setBounds(340, 142, 105, 24);
		panel.add(lblPrivateLessonHours);
		
		rdbtnPrivateLessonHour = new JRadioButton("From 12h");
		rdbtnPrivateLessonHour.setBounds(450, 141, 180, 23);
		rdbtnPrivateLessonHour.addActionListener(e -> handleRadioButtonSelection(rdbtnPrivateLessonHour.getModel()));
		panel.add(rdbtnPrivateLessonHour);		
		
		startHourLessonRadioButtonGroup = new ButtonGroup();
		startHourLessonRadioButtonGroup.add(rdbtnAfternoonLesson);
		startHourLessonRadioButtonGroup.add(rdbtnMorningLesson);
		startHourLessonRadioButtonGroup.add(rdbtnPrivateLessonHour);
		
		privateLessonRadioButtonGroup = List.of(rdbtnPrivateLessonHour);
		groupLessonRadioButtonGroup = List.of(rdbtnMorningLesson, rdbtnAfternoonLesson);
		
		setGroupDisabled(getButtonsFromGroup(startHourLessonRadioButtonGroup));
	}

	private void setFirstButtonSelected(List<AbstractButton> buttons) {;
	    buttons.get(0).setSelected(true);    
	}

	private void setGroupEnabled(List<AbstractButton> buttons) {
        for (AbstractButton button : buttons) {
            button.setEnabled(true);
        }
    }
	
	private void setGroupDisabled(List<AbstractButton> buttons) {
        for (AbstractButton button : buttons) {
            button.setEnabled(false);
        }
    }
	
	private static List<AbstractButton> getButtonsFromGroup(ButtonGroup group) {
	    List<AbstractButton> buttons = new ArrayList<>();
	    Enumeration<AbstractButton> elements = group.getElements();
	    
	    while (elements.hasMoreElements()) {
	        buttons.add(elements.nextElement());
	    }
	    
	    return buttons;
	}

	
	private void setModelToJComboBox(JComboBox<String> comboBox, String[] values) {
		comboBox.setModel(new DefaultComboBoxModel<String>(values));
	}
	
	private Collection<LessonType> getLessonTypesFromDatabase() {
		return LessonType.findAllInDatabase((LessonTypeDAO) lessonTypeDAO);
	}
	
	private Collection<Instructor> getInstructorsFromDatabase(){
		return Instructor.findAllInDatabase((InstructorDAO) instructorDAO);
	}
	
	private Collection<Location> getLocationsFromDatabase(){
		return Location.findAllInDatabase((LocationDAO) locationDAO);
	}
	
	private void loadLessonTypeMap() {
		Collection<LessonType> lessonTypes = getLessonTypesFromDatabase();
		
		for (LessonType lessonType: lessonTypes) {
			String key = lessonType.getLessonTypeInfoFormattedForDisplay();
			lessonTypeMap.put(key, lessonType);
		}
	}
	
	private void loadInstructorMap() {
		Collection<Instructor> instructors = getInstructorsFromDatabase();
		
		for(Instructor instructor: instructors) {
			String key = getInstructorFormattedNames(instructor);
			
			instructorMap.put(key, instructor);
		}
	}
	
	private void loadLocationMap() {
		Collection<Location> locations = getLocationsFromDatabase();
				
		for (Location location: locations) {
			String key = location.getName() + " (N° " + location.getId() + ")" ;
			
			locationMap.put(key, location);
		}
	}
	
	private String getInstructorFormattedNames(Instructor instructor) {
		return 
			instructor.getLastnameFormattedForDisplay() + " " + 
			instructor.getFirstNameFormattedForDisplay() + " " + 
			"(N° " + instructor.getId() + ')';
	}
	
	
	private Collection<Instructor> getEligibleInstructors(
        Collection<Instructor> instructors, 
        Accreditation accreditation, 
        LocalDateTime dateTime
    ) {
	    return instructors.stream()
            .filter(instructor -> instructor.isEligible(accreditation, dateTime))
            .collect(Collectors.toList());
	}

	
	private void setComponentsEnabled(Collection<JComponent> components, boolean enabled) {
        for (JComponent component : components) {
            if (component != null) {
                component.setEnabled(enabled);
            }
        }
    }
	
	private void handleLessonTypeSelection() {
		if(lessonTypeComboBox.getSelectedIndex() == -1) {
			return;
		}
		
		LocalDate selectedDate = DateParser.toLocalDate(startDateField.getDate());
		Object lessonTypeKey = lessonTypeComboBox.getSelectedItem();
		Accreditation associatedAccreditation = lessonTypeMap.get(lessonTypeKey).getAccreditation();
		boolean isPrivateLessonType = lessonTypeMap.get(lessonTypeKey).getIsPrivate();
				
		Collection<JComponent> components = List.of(eligibleInstructorsComboBox, locationComboBox);
		Collection<JComboBox<?>> comboBoxes = List.of(eligibleInstructorsComboBox, locationComboBox);
		Collection<Instructor> eligibleInstructors = getEligibleInstructors(
			instructorMap.values(), 
			associatedAccreditation, 
			adjustTimeOfStartDate(selectedDate.atStartOfDay(), isPrivateLessonType)
		);
		
		if(eligibleInstructors.isEmpty()) {
			if(!isComboBoxEmpty(eligibleInstructorsComboBox)) {
				clearJComboBox(comboBoxes);
				setComponentsEnabled(components, false);
			}
			
			JOptionPane.showMessageDialog(
				null, 
				"There is no instructor eligible for this lesson type at this date.", 
				"Warning", 
				JOptionPane.WARNING_MESSAGE
			);
			
			return;
		}
		
		
		String[] eligibleInstructorsFormatted = 	
			eligibleInstructors
				.stream()
				.map(eligibleInstructor -> getInstructorFormattedNames(eligibleInstructor))
				.toArray(String[]::new);
		
		updateLessonTypeGroups(isPrivateLessonType);
		setModelToJComboBox(eligibleInstructorsComboBox, eligibleInstructorsFormatted);
		setModelToJComboBox(locationComboBox, locationMap.keySet().toArray(String[]::new));
		setComponentsEnabled(components, true);
	}
	
	private void updateLessonTypeGroups(boolean isPrivateLessonType) {
	    List<AbstractButton> enableGroup = isPrivateLessonType 
	        ? privateLessonRadioButtonGroup 
	        : groupLessonRadioButtonGroup;
	    
	    List<AbstractButton> disableGroup = isPrivateLessonType 
	        ? groupLessonRadioButtonGroup 
	        : privateLessonRadioButtonGroup;

	    
	    setFirstButtonSelected(enableGroup);
	    setGroupEnabled(enableGroup);
	    setGroupDisabled(disableGroup);
	}

	
	private void clearJComboBox (Collection<JComboBox<?>> comboBoxes) {
		for (JComboBox<?> comboBox: comboBoxes) {			
			if (comboBox != null) {
				comboBox.removeAllItems();
			}
		}
	}
	
	private boolean isComboBoxEmpty(JComboBox<?> comboBox) {
        return comboBox != null && comboBox.getItemCount() <= 0;
    }
	
	private DefaultListCellRenderer createLessonTypeRenderer(
        Map<? extends Object, LessonType> lessonTypeMap,
        Collection<Instructor> instructors
    ) {
        return new DefaultListCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getListCellRendererComponent(
        		JList<?> list, 
        		Object value, 
        		int index, 
        		boolean isSelected, 
        		boolean cellHasFocus
    		) {    
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value == null) {
                    return this;
                }

                if (!isOneInstructorEligible(value, lessonTypeMap, instructors)) {
                    setEnabled(false); 
                }

                return this;
            }
        };
    }

	private boolean isOneInstructorEligible(
        Object lessonTypeKey, 
        Map<? extends Object, LessonType> lessonTypeMap, 
        Collection<Instructor> instructors
    ) {
        LessonType lessonType = lessonTypeMap.get(lessonTypeKey);
        if (lessonType == null) {
        	return false;        	
        }

        Accreditation accreditation = lessonType.getAccreditation();
        LocalDateTime dateTime = adjustTimeOfStartDate(DateParser.toLocalDateTime(startDateField.getDate()), lessonType.getIsPrivate());
        return instructors.stream().anyMatch(instructor -> instructor.isEligible(accreditation, dateTime));
    }
	
	private void handleClickOnCancelButton() {
		openMainMenu();
		dispose();
	}
	
	private void openMainMenu() {
		MainMenu mainMenu = new MainMenu();
		mainMenu.setVisible(true);
	}
	
	private void handleClickOnCreateButton() {
		try {
			Lesson lesson = buildLessonFromFields();
			boolean isAddedIntoInstructor = false;
			
			isAddedIntoInstructor = lesson.getInstructor().addLesson(lesson);
			if (!isAddedIntoInstructor) {
				displayAddLessonFailure(JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			boolean isAddedIntoDatabase = insertLessonIntoDatabase(lesson);
			
			if(isAddedIntoDatabase) {
				displayAddLessonSuccess(JOptionPane.INFORMATION_MESSAGE);
				openMainMenu();
				dispose();
	        } else {
	        	lesson.getInstructor().removeLesson(lesson);
	        	displayAddLessonFailure(JOptionPane.ERROR_MESSAGE);
	        }
		} catch (Exception ex) {
			displayErrorMessage(ex);
		}
	}
	
	private void displayAddLessonSuccess(int messageType) {
        JOptionPane.showMessageDialog(null, "Lesson added successfully!", "Success", messageType);
	}
	
	private void displayAddLessonFailure(int messageType) {
        JOptionPane.showMessageDialog(null, "Failed to add lesson. Please try again.", "Error", messageType);
	}
	
	private boolean insertLessonIntoDatabase(Lesson lesson) {
        return lesson.insertIntoDatabase((LessonDAO) lessonDAO);
    }
	
	private void displayErrorMessage(Exception ex) {
		JOptionPane.showMessageDialog(
    		null, 
    		ex.getMessage(), 
    		"Error", 
    		JOptionPane.ERROR_MESSAGE
		);
	}
	
	private LocalDateTime adjustTimeOfStartDate(LocalDateTime dateTime, boolean isPrivate) {
		dateTime = dateTime.toLocalDate().atStartOfDay();
		
	    if (isPrivate) {
	        return dateTime.withHour(12);
	    }

	    Map<ButtonModel, Integer> hourMapping = Map.of(
	        rdbtnPrivateLessonHour.getModel(), 12,
	        rdbtnAfternoonLesson.getModel(), 14,
	        rdbtnMorningLesson.getModel(), 9
	    );

	    ButtonModel selectedModel = startHourLessonRadioButtonGroup.getSelection();

	    return dateTime.withHour(hourMapping.get(selectedModel));
	}

	
	private Lesson buildLessonFromFields() {
		try {
			LessonType lessonType = lessonTypeMap.get(lessonTypeComboBox.getSelectedItem());
			Instructor instructor = instructorMap.get(eligibleInstructorsComboBox.getSelectedItem());
			Location location = locationMap.get(locationComboBox.getSelectedItem());
			LocalDateTime startDate = DateParser.toLocalDateTime(startDateField.getDate());
			startDate = adjustTimeOfStartDate(startDate, lessonType.getIsPrivate());
			
			return new Lesson(startDate, lessonType, instructor, location.getId(), location.getName());			
		} catch (Exception ex) {
            throw new IllegalArgumentException("Failed to build lesson from fields.");
		}
	}
	
	private void handleRadioButtonSelection(ButtonModel selectedModel) {
		LessonType lessonType = lessonTypeMap.get(lessonTypeComboBox.getSelectedItem());
		Collection<Instructor> eligibleInstructors = getEligibleInstructors(
			instructorMap.values(),
			lessonType.getAccreditation(),
			adjustTimeOfStartDate(DateParser.toLocalDateTime(startDateField.getDate()), lessonType.getIsPrivate())
		);
		
		clearJComboBox(List.of(eligibleInstructorsComboBox));
		setModelToJComboBox(
			eligibleInstructorsComboBox, 
			eligibleInstructors.stream().map(this::getInstructorFormattedNames).toArray(String[]::new)
		);
	}
}
