package be.th.jframes;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import be.th.dao.DAO;
import be.th.dao.DAOFactory;
import be.th.dao.LessonDAO;
import be.th.dao.SkierDAO;
import be.th.formatters.DatabaseFormatter;
import be.th.models.Instructor;
import be.th.models.Lesson;
import be.th.models.Skier;
import be.th.parsers.DateParser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;

public class AddABooking extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable upcomingLessonsTable;
	
	private Skier selectedSkier;
	private Lesson selectedLesson;
	private LinkedHashMap<Integer, Lesson> upcomingLessonsMap = new LinkedHashMap<>();
	
	private DAO<Lesson> lessonDAO;

	public AddABooking(Skier selectedSkier) {
		DAOFactory daoFactory = new DAOFactory();
		
		this.lessonDAO = daoFactory.getLessonDAO();
		this.selectedSkier = selectedSkier;
		
		System.out.println("Selected skier: " + selectedSkier);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Take the insurance out (+ 20€)");
		chckbxNewCheckBox.setBounds(106, 297, 229, 31);
		panel.add(chckbxNewCheckBox);
		
		JLabel lblInsurance = new JLabel("Insurance");
		lblInsurance.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblInsurance.setBounds(10, 295, 90, 31);
		panel.add(lblInsurance);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(null, "Upcoming lessons", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 59, 1176, 229);
		panel.add(panel_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 1156, 197);
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
		));
		upcomingLessonsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		upcomingLessonsTable.getTableHeader().setReorderingAllowed(false);
		addDoubleClickListener(upcomingLessonsTable, doubleClickListener);

		scrollPane.setViewportView(upcomingLessonsTable);
		
		JLabel lblSelectALesson = new JLabel("Select an upcoming lesson");
		lblSelectALesson.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSelectALesson.setBounds(10, 21, 199, 31);
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
		addBtn.addActionListener(e -> handleClickOnAddBtn());
		addBtn.setOpaque(true);
		addBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addBtn.setContentAreaFilled(true);
		addBtn.setBorderPainted(false);
		addBtn.setBackground(new Color(139, 255, 96));
		addBtn.setBounds(613, 403, 154, 51);
		panel.add(addBtn);
		
		loadUpcommingLessonsMap();
		displayUpcomingLessons(upcomingLessonsMap.values());
	}
	
	private void loadUpcommingLessonsMap() {
		List<Lesson> upcomingLessons = Lesson.findAllInDatabase((LessonDAO) lessonDAO);
		
		if (!upcomingLessonsMap.isEmpty()) {
			upcomingLessonsMap.clear();
		}
		
		for (Lesson upcomingLesson : upcomingLessons) {
            upcomingLessonsMap.put(upcomingLesson.getId(), upcomingLesson);
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
		
		selectedLesson = upcomingLessonsMap.get((Integer) table.getValueAt(row, 0));
		System.out.println("Clicked on row: " + selectedLesson);
	}
	
	private void handleClickOnCancelBtn() {
		dispose();
	}
	
	private void handleClickOnAddBtn() {
		// TODO
	}
}
