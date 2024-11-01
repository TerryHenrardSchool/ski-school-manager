package be.th.jframes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;

public class MainMenu extends JFrame {
	
	private static final long serialVersionUID = -639386033572169717L;

	public MainMenu() {
        // Main window configuration
        setTitle("Ski School Manager");
        setSize(354, 242);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // "Add a skier" button
        JButton btnAddSkier = new JButton("Add a skier");
        btnAddSkier.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnAddSkier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open the AddSkierFrame window
            	AddASkier addSkierFrame = new AddASkier();
                addSkierFrame.setVisible(true);
                dispose();  // Close the main window
            }
        });
        getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
        getContentPane().add(btnAddSkier);
        
                // "Add an instructor" button
                JButton btnAddInstructor = new JButton("Add an instructor");
                btnAddInstructor.setFont(new Font("Tahoma", Font.PLAIN, 14));
                btnAddInstructor.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Open the AddInstructorFrame window
                    	AddAnInstructor addInstructorFrame = new AddAnInstructor();
                        addInstructorFrame.setVisible(true);
                        dispose();  // Close the main window
                    }
                });
                getContentPane().add(btnAddInstructor);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainMenu frame = new MainMenu();
                frame.setVisible(true);
            }
        });
    }
}
