package be.th.jframes;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import be.th.dao.DAO;
import be.th.dao.DAOFactory;
import be.th.dao.InstructorDAO;
import be.th.dao.LessonTypeDAO;
import be.th.models.Accreditation;
import be.th.models.Instructor;
import be.th.models.LessonType;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.print.event.PrintJobAttributeEvent;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

public class AddALesson extends JFrame {

	private static final long serialVersionUID = 1L;
	
	DAO<LessonType> lessonTypeDAO;
	DAO<Instructor> instructorDAO;
	
	private Map<String, LessonType> lessonTypeMap;
	private Map<String, Instructor> instructorMap;
	
	private JPanel contentPane;
	private JTextField textField;
	private JComboBox<String> lessonTypeComboBox;
	private JComboBox<String> accreditedInstructorsComboBox;

	/**
	 * Create the frame.
	 */
	public AddALesson() {
		DAOFactory daoFactory = new DAOFactory();
		
		this.lessonTypeDAO = daoFactory.getLessonTypeDAO();
		this.instructorDAO = daoFactory.getInstructorDAO();
        this.lessonTypeMap = new HashMap<>();
        this.instructorMap = new HashMap<>();
        
        loadLessonTypeMap();
        loadInstructorMap();
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1053, 740);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Add a new lesson");
		lblTitle.setOpaque(true);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblTitle.setBackground(new Color(0, 153, 255));
		lblTitle.setBounds(10, 11, 1017, 52);
		contentPane.add(lblTitle);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Lesson information", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 74, 1017, 616);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblSport = new JLabel("Lesson type");
		lblSport.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSport.setBounds(10, 21, 90, 31);
		panel.add(lblSport);
		
		lessonTypeComboBox = new JComboBox<String>();
		
		lessonTypeComboBox.setBounds(110, 21, 167, 31);
		panel.add(lessonTypeComboBox);
		
		setModelToJComboBox(lessonTypeComboBox, lessonTypeMap.keySet().stream().sorted((key1, key2) -> key1.compareTo(key2)).toArray(String[]::new));
		lessonTypeComboBox.setSelectedIndex(-1); 
		lessonTypeComboBox.addActionListener(e -> handleLessonTypeSelection());
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPrice.setBounds(393, 21, 90, 31);
		panel.add(lblPrice);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setBounds(493, 21, 167, 31);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblInstructor = new JLabel("Instructor");
		lblInstructor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInstructor.setBounds(10, 63, 90, 31);
		panel.add(lblInstructor);
		
		accreditedInstructorsComboBox = new JComboBox<String>();
		accreditedInstructorsComboBox.setEnabled(false);
		accreditedInstructorsComboBox.setSelectedIndex(-1);
		accreditedInstructorsComboBox.setBounds(110, 63, 167, 31);
		panel.add(accreditedInstructorsComboBox);
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
	
	private void loadLessonTypeMap() {
		Collection<LessonType> lessonTypes = getLessonTypesFromDatabase();
		
		for (LessonType lessonType: lessonTypes) {
			String key = lessonType.getLessonTypeInfoFormattedForDisplay();
			lessonTypeMap.put(key, lessonType);
		}
	}
	
	private String getInstructorFormattedNames(Instructor instructor) {
		return 
			instructor.getLastnameFormattedForDisplay() + " " + 
			instructor.getFirstNameFormattedForDisplay() + " " + 
			"(N° " + instructor.getId() + ')';
	}
	
	private void loadInstructorMap() {
		 Collection<Instructor> instructors = getInstructorsFromDatabase();
		 
		 for(Instructor instructor: instructors) {
			 String key = getInstructorFormattedNames(instructor);
			 
			 instructorMap.put(key, instructor);
		 }
	}
	
	private Collection<Instructor> getAccreditedInstructors(Collection<Instructor> instructors, Accreditation accreditation) {
		Collection<Instructor> accreditedInstructors = new ArrayList<Instructor>();
		
		for (Instructor instructor: instructors) {
			if(instructor.isAccreditate(accreditation)) {
				accreditedInstructors.add(instructor);
			}
		}
		
		return accreditedInstructors;
	}
	
	private void handleLessonTypeSelection() {
		{
			if(lessonTypeComboBox.getSelectedIndex() == -1) {
				return;
			}
			
			Object lessonTypeKey = lessonTypeComboBox.getSelectedItem();
			Accreditation associatedAccreditation = lessonTypeMap.get(lessonTypeKey).getAccreditation();
			
			Collection<Instructor> accreditedInstructors = getAccreditedInstructors(instructorMap.values(), associatedAccreditation);
			
			if(accreditedInstructors.size() <= 0) {
				JOptionPane.showMessageDialog(null, "There is no instructor available for this lesson type", "Warning", JOptionPane.WARNING_MESSAGE);
			}
			String[] accreditedInstructorsFormatted = 	
					accreditedInstructors
					.stream()
					.map(accreditedInstructor -> getInstructorFormattedNames(accreditedInstructor))
					.toArray(String[]::new);
			
			setModelToJComboBox(accreditedInstructorsComboBox, accreditedInstructorsFormatted);
			accreditedInstructorsComboBox.setEnabled(true);
			
		}
	}
}
