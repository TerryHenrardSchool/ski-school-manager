package be.th.jframes;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import be.th.dao.DAO;
import be.th.dao.DAOFactory;
import be.th.models.LessonType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import org.eclipse.wb.swing.FocusTraversalOnArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateCollectifLesson extends JFrame {
 
	private static final long serialVersionUID = -4742457046736469695L;
	private final DAOFactory DAO_FACTORY = new DAOFactory();   
	
	private List<LessonType> lessonTypes;
    
    public CreateCollectifLesson() {
    	DAO<LessonType> lessonTypeDAO = DAO_FACTORY.getLessonTypeDAO();
    	
    	lessonTypes = lessonTypeDAO.findAll();
    	
    	int lessonTypesSize = lessonTypes.size();
    	String[] lessonTypeStringTemplates = new String[lessonTypesSize];
    	for (int i = 0; i < lessonTypesSize; i++) {
    		LessonType currLessonType = lessonTypes.get(i);
    		lessonTypeStringTemplates[i] = String.format("%-5s %-26s %-4.2f€", currLessonType.getAgeCategoryName(), currLessonType.getLevel(), currLessonType.getPrice());
    	}
    	
        setTitle("Collectif Lesson Creation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        
        // Set window to full screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);

        // Main panel with a grid layout for form fields
        JPanel formPanel = new JPanel();
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        formPanel.setBackground(new Color(245, 245, 255));

        // Label font
        Font labelFont = new Font("Arial", Font.BOLD, 14);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Create a new collectif lesson");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        formPanel.setLayout(null);

        // Add panels to frame
        getContentPane().add(titlePanel, BorderLayout.NORTH);
        getContentPane().add(formPanel, BorderLayout.CENTER);
        
        JLabel label = new JLabel("");
        label.setBounds(1444, 37, 0, 0);
        formPanel.add(label);
        
        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//JOptionPane.showConfirmDialog(null, "test");
        		System.out.println(e);
        	}
        });
        comboBox.setModel(new DefaultComboBoxModel<String>(lessonTypeStringTemplates));
        comboBox.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        comboBox.setBounds(192, 84, 314, 21);
        formPanel.add(comboBox);
        
        JLabel lblNewLabel = new JLabel("Lesson type: ");
        lblNewLabel.setBounds(87, 88, 95, 13);
        formPanel.add(lblNewLabel);
        formPanel.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{label, comboBox, lblNewLabel}));
    }
    
    public static void main(String[] args) {
        CreateCollectifLesson window = new CreateCollectifLesson();
        window.setVisible(true);
    }
}
