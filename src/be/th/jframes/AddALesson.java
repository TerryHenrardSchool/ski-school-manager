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

public class AddALesson extends JFrame {

	private static final long serialVersionUID = 1L;
	
	DAO<LessonType> lessonTypeDAO;
	
	private JPanel contentPane;
	private Map<String, LessonType> lessonTypeMap;

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
		
		JLabel lblTitle = new JLabel("Add a new instructor");
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
		
		JLabel lblLessonType = new JLabel("Lesson type");
		lblLessonType.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLessonType.setBounds(10, 21, 90, 31);
		panel.add(lblLessonType);
		
		JComboBox<LessonType> comboBox = new JComboBox<LessonType>();
		
		comboBox.setBounds(110, 21, 154, 31);
		panel.add(comboBox);
	}
	
	private void setModelToJComboBox(JComboBox<LessonType> comboBox, LessonType[] lessonTypes) {
		comboBox.setModel(new DefaultComboBoxModel<LessonType>(lessonTypes));
	}
	
	private Collection<LessonType> getLessonTypesFromDatabase() {
		return LessonType.findAllInDatabase((LessonTypeDAO) lessonTypeDAO);
	}
	
	private void loadLessonTypeMap() {
		Collection<LessonType> lessonTypes = getLessonTypesFromDatabase();
		
		for (LessonType lessonType: lessonTypes) {
			String key = lessonType.getName() + " - " + lessonType.getLevel();
			lessonTypeMap.put(key, lessonType);
		}
	}
}
