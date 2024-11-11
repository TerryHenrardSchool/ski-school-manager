package be.th.jframes;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
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
import be.th.dao.LessonTypeDAO;
import be.th.models.LessonType;

import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;

public class AddALesson extends JFrame {

	private static final long serialVersionUID = 1L;
	
	DAO<LessonType> lessonTypeDAO;
	
	private JPanel contentPane;
	private Map<String, LessonType> lessonTypeMap;
	private JTextField textField;

	/**
	 * Create the frame.
	 */
	public AddALesson() {
		DAOFactory daoFactory = new DAOFactory();
		
		this.lessonTypeDAO = daoFactory.getLessonTypeDAO();
        this.lessonTypeMap = new HashMap<>();

		
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
		
		JComboBox<String> comboBox = new JComboBox<String>();
		
		comboBox.setBounds(110, 21, 167, 31);
		panel.add(comboBox);
		
		loadLessonTypeMap();
		setModelToJComboBox(comboBox, lessonTypeMap.keySet().stream().sorted((key1, key2) -> key1.compareTo(key2)).toArray(String[]::new));
		comboBox.setSelectedIndex(-1); 
		comboBox.addActionListener(e -> {
			if(comboBox.getSelectedIndex() == -1) {
				return;
			}
			
			Object key = comboBox.getSelectedItem();
			System.out.println(lessonTypeMap.get(key));
		});
		
		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPrice.setBounds(10, 70, 90, 31);
		panel.add(lblPrice);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setEditable(false);
		textField.setBounds(110, 70, 167, 31);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Aller chercher les accréditations et proposer uniquement les instructor avec l'accrédition requise pour ce LessonType");
		lblNewLabel.setBounds(256, 233, 575, 162);
		panel.add(lblNewLabel);
	}
	
	private void setModelToJComboBox(JComboBox<String> comboBox, String[] values) {
		comboBox.setModel(new DefaultComboBoxModel<String>(values));
	}
	
	private Collection<LessonType> getLessonTypesFromDatabase() {
		return LessonType.findAllInDatabase((LessonTypeDAO) lessonTypeDAO);
	}
	
	private void loadLessonTypeMap() {
		Collection<LessonType> lessonTypes = getLessonTypesFromDatabase();
		
		for (LessonType lessonType: lessonTypes) {
			String key = lessonType.getLessonTypeInfoFormattedForDisplay();
			lessonTypeMap.put(key, lessonType);
		}
	}
}
