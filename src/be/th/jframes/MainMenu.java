package be.th.jframes;

import javax.swing.*;
import java.awt.Font;
import java.awt.Color;

import javax.swing.border.TitledBorder;

import be.th.styles.FontStyles;

import java.awt.GridLayout;

import javax.swing.border.EtchedBorder;

public class MainMenu extends JFrame {
    
    private static final long serialVersionUID = -639386033572169717L;
    
    // Release 2.0.1 everything works fine
    public MainMenu() {
        setTitle("Ski School Manager");
        setSize(1017, 559);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Skier actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(10, 87, 983, 136);
        getContentPane().add(panel);
        panel.setLayout(new GridLayout(0, 2, 0, 0));

        JButton btnAddSkier = new JButton("Add a skier");
        panel.add(btnAddSkier);
        btnAddSkier.setFont(FontStyles.BUTTON);
        
        JButton btnNewButton = new JButton("Search a skier");
        btnNewButton.addActionListener(e -> {
        	openFrame(new SearchASkier());
        	dispose();
        });
        btnNewButton.setFont(FontStyles.BUTTON);
        panel.add(btnNewButton);
        btnAddSkier.addActionListener(e -> {
        	openFrame(new AddASkier());
        	dispose();
        });
        
        JLabel lblNewLabel_1 = new JLabel("Main menu");
        lblNewLabel_1.setOpaque(true);
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 24));
        lblNewLabel_1.setBackground(new Color(0, 153, 255));
        lblNewLabel_1.setBounds(10, 24, 983, 52);
        getContentPane().add(lblNewLabel_1);
                
        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(null, "Skier actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel_1.setBounds(10, 233, 983, 136);
        getContentPane().add(panel_1);
        panel_1.setLayout(new GridLayout(0, 2, 0, 0));
        
	    JButton btnAddInstructor = new JButton("Add an instructor");
	    panel_1.add(btnAddInstructor);
	    btnAddInstructor.setFont(FontStyles.BUTTON);
	    
	    JButton btnNewButton_1 = new JButton("Search an instructor");
	    btnNewButton_1.addActionListener(e -> {
	    	openFrame(new SearchAnInstructor());
	    	dispose();
	    });
	    
	    btnNewButton_1.setFont(FontStyles.BUTTON);
	    panel_1.add(btnNewButton_1);
	    
	    JPanel panel_1_1 = new JPanel();
	    panel_1_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Lesson Actions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
	    panel_1_1.setBounds(10, 380, 981, 136);
	    getContentPane().add(panel_1_1);
	    panel_1_1.setLayout(new GridLayout(0, 2, 0, 0));
	    
	    JButton btnAddLesson = new JButton("Add a lesson");
	    btnAddLesson.addActionListener(e -> {
	    	openFrame(new AddALesson());
	    	dispose();
	    });
	    btnAddLesson.setFont(new Font("Tahoma", Font.PLAIN, 14));
	    panel_1_1.add(btnAddLesson);
	    
	    JLabel lblNewLabel = new JLabel("To consult the lessons, go to the section \"search an instructor\"");
	    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
	    panel_1_1.add(lblNewLabel);
	    
	    
	    btnAddInstructor.addActionListener(e ->{
	    	openFrame(new AddAnInstructor());
	    	dispose();
	    });
	    
	    ClockElement.create(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainMenu frame = new MainMenu();
                
                frame.setVisible(true);
            }
        });
    }
    
    private void openFrame(JFrame frame) {
    	ClockElement.create(frame);
        frame.setVisible(true);
    }
}
