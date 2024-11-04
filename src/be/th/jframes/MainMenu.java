package be.th.jframes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Color;
import javax.swing.border.TitledBorder;

import be.th.styles.FontStyles;

import java.awt.GridLayout;

public class MainMenu extends JFrame {
    
    private static final long serialVersionUID = -639386033572169717L;
    
    public MainMenu() {
        setTitle("Ski School Manager");
        setSize(1017, 525);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Skier actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(20, 72, 973, 136);
        getContentPane().add(panel);
        panel.setLayout(new GridLayout(0, 2, 0, 0));

        JButton btnAddSkier = new JButton("Add a skier");
        panel.add(btnAddSkier);
        btnAddSkier.setFont(FontStyles.BUTTON);
        
        JButton btnNewButton = new JButton("Search a skier");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SearchASkier searchASkierFrame = new SearchASkier();
        		searchASkierFrame.setVisible(true);
        		
        		dispose();
        	}
        });
        btnNewButton.setFont(FontStyles.BUTTON);
        panel.add(btnNewButton);
        btnAddSkier.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddASkier addSkierFrame = new AddASkier();
                addSkierFrame.setVisible(true);
                dispose();
            }
        });
        
        JLabel lblNewLabel_1 = new JLabel("Main menu");
        lblNewLabel_1.setOpaque(true);
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 24));
        lblNewLabel_1.setBackground(new Color(0, 153, 255));
        lblNewLabel_1.setBounds(10, 10, 983, 52);
        getContentPane().add(lblNewLabel_1);
                
                JPanel panel_1 = new JPanel();
                panel_1.setBorder(new TitledBorder(null, "Skier actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
                panel_1.setBounds(20, 218, 973, 136);
                getContentPane().add(panel_1);
                panel_1.setLayout(new GridLayout(0, 2, 0, 0));
                
                        JButton btnAddInstructor = new JButton("Add an instructor");
                        panel_1.add(btnAddInstructor);
                        btnAddInstructor.setFont(FontStyles.BUTTON);
                        
                        JButton btnNewButton_1 = new JButton("Search an instructor");
                        btnNewButton_1.setFont(FontStyles.BUTTON);
                        panel_1.add(btnNewButton_1);
                btnAddInstructor.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        AddAnInstructor addInstructorFrame = new AddAnInstructor();
                        addInstructorFrame.setVisible(true);
                        dispose();  
                    }
                });
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