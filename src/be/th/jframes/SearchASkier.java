package be.th.jframes;

import java.awt.EventQueue;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import be.th.dao.DAO;
import be.th.dao.DAOFactory;
import be.th.dao.SkierDAO;
import be.th.formatters.DatabaseFormatter;
import be.th.models.Address;
import be.th.models.Person;
import be.th.models.Skier;
import be.th.parsers.DateParser;
import be.th.styles.ColorStyles;
import be.th.styles.FontStyles;
import be.th.validators.DateValidator;
import be.th.validators.IntegerValidator;
import be.th.validators.ObjectValidator;
import be.th.validators.StringValidator;

import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.security.KeyStore.PrivateKeyEntry;
import java.time.LocalDate;
import java.awt.event.ActionEvent;
import javax.swing.border.EtchedBorder;
import com.toedter.calendar.JDateChooser;

public class SearchASkier extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private DAO<Skier> skierDAO = new DAOFactory().getSkierDAO();
	
	private JPanel contentPane;
	private JTable table;
	private JTextField idSearchTxtField;
	
	private LinkedHashMap<Integer, Skier> skierMap = new LinkedHashMap<>();
	private JTextField lastNameSearchTxtField;
	private JTextField firstNameSearchTxtField;
	private JDateChooser birthDateTextField;
	private JTextField emailSearchTxtField;
	private JTextField addressSearchTxtField;
	private JTextField phoneNumberTextField;
	
	private Skier selectedSkier;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchASkier frame = new SearchASkier();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SearchASkier() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1539, 1003);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Skiers", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(282, 64, 1239, 313);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 24, 1219, 279);
		panel.add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(e -> {
		    if (e.getValueIsAdjusting()) {
		        return;
		    }
		    
		    int selectedRow = table.getSelectedRow();
		    
		    if (selectedRow < 0) { 
		        return;
		    }
		    
		    try {
		        Integer id = (Integer) table.getValueAt(selectedRow, 0);
		        Map<String, String> nameMap = Person.splitLastNameAndFirstName((String) table.getValueAt(selectedRow, 1));
		        LocalDate birthdate = DateParser.toLocalDate((String) table.getValueAt(selectedRow, 2));
		        Map<String, String> addressMap = Address.destructureFormattedAddress((String) table.getValueAt(selectedRow, 3));
		        String phoneNumber = (String) table.getValueAt(selectedRow, 4);
		        String email = (String) table.getValueAt(selectedRow, 5);
		        
		        selectedSkier = new Skier(
	        		id, 
	        		nameMap.get("lastName"), 
	        		nameMap.get("firstName"), 
	        		birthdate, 
	        		addressMap.get("city"), 
	        		addressMap.get("postcode"), 
	        		addressMap.get("streetName"), 
	        		addressMap.get("streetNumber"), 
	        		phoneNumber, 
	        		email
        		);
		        
		        // TODO: Add delete button
		        // TODO: Add update button that opens a form like create skier with pre-filled fields and a button to submit
		        // TODO: Add JTable with the upcoming lessons
		    } catch (Exception ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Error while fetching skier data.", "Erreur", JOptionPane.ERROR_MESSAGE);
		    }
		});

		
		DefaultTableModel tableModel = new DefaultTableModel(
		    new Object[][] {},
		    new String[] { "Id", "Names", "Birthdate", "Address", "Phone number", "Email" }
		) {
			private static final long serialVersionUID = -4108980079580312070L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		table.setModel(tableModel);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(10); 
		table.getColumnModel().getColumn(1).setPreferredWidth(75);
		table.getColumnModel().getColumn(2).setPreferredWidth(50); 
		table.getColumnModel().getColumn(3).setPreferredWidth(200); 
		table.getColumnModel().getColumn(4).setPreferredWidth(80); 
		table.getColumnModel().getColumn(5).setPreferredWidth(180); 
		
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Search criteria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(20, 64, 252, 313);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblSearchSkierId = new JLabel("Id:");
		lblSearchSkierId.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchSkierId.setBounds(10, 24, 64, 25);
		panel_1.add(lblSearchSkierId);
		
		idSearchTxtField = new JTextField();
		idSearchTxtField.setToolTipText("");
		idSearchTxtField.setBounds(130, 24, 110, 31);
		panel_1.add(idSearchTxtField);
		idSearchTxtField.setColumns(10);
		
		JButton btnSearchSkier = new JButton("Search");
		btnSearchSkier.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSearchSkier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final Integer id = getNumberField(idSearchTxtField, "id");
				final String lastName = getTextField(lastNameSearchTxtField);
				final String firstName = getTextField(firstNameSearchTxtField);
				final LocalDate birthdate = DateParser.toLocalDate(birthDateTextField.getDate());
				final String email = getTextField(emailSearchTxtField);
				final String address = getTextField(addressSearchTxtField);
				final String phoneNumber = getTextField(phoneNumberTextField);

				displaySkiersInTable(searchSkiers(id, lastName, firstName, birthdate, email, address, phoneNumber));
			}
		});
		btnSearchSkier.setBounds(130, 272, 110, 31);
		panel_1.add(btnSearchSkier);
		
		JLabel lblSearchLastName = new JLabel("Last name:");
		lblSearchLastName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchLastName.setBounds(10, 60, 85, 25);
		panel_1.add(lblSearchLastName);
		
		lastNameSearchTxtField = new JTextField();
		lastNameSearchTxtField.setToolTipText("");
		lastNameSearchTxtField.setColumns(10);
		lastNameSearchTxtField.setBounds(130, 60, 110, 31);
		/*lastNameSearchTxtField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onTextChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onTextChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onTextChanged();
            }

            // Méthode pour exécuter une action à chaque modification
            private void onTextChanged() {
                System.out.println("Texte actuel : " + lastNameSearchTxtField.getText());
            }
        });*/
		panel_1.add(lastNameSearchTxtField);
		
		JLabel lblSearchFirstName = new JLabel("First name:");
		lblSearchFirstName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchFirstName.setBounds(10, 96, 85, 25);
		panel_1.add(lblSearchFirstName);
		
		firstNameSearchTxtField = new JTextField();
		firstNameSearchTxtField.setToolTipText("");
		firstNameSearchTxtField.setColumns(10);
		firstNameSearchTxtField.setBounds(130, 96, 110, 31);
		panel_1.add(firstNameSearchTxtField);
		
		JButton btnResetSearch = new JButton("Reset");
		btnResetSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displaySkiersInTable(skierMap.values());
				resetTextFields();
			}
		});
		btnResetSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnResetSearch.setBounds(10, 272, 110, 31);
		panel_1.add(btnResetSearch);
		
		JLabel lblSearchBirthdate = new JLabel("Birthdate:");
		lblSearchBirthdate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSearchBirthdate.setBounds(10, 131, 85, 25);
		panel_1.add(lblSearchBirthdate);
		
		JLabel lblSearchAddress = new JLabel("Address:");
		lblSearchAddress.setBounds(10, 166, 110, 25);
		panel_1.add(lblSearchAddress);
		lblSearchAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		addressSearchTxtField = new JTextField();
		addressSearchTxtField.setBounds(130, 166, 110, 31);
		panel_1.add(addressSearchTxtField);
		addressSearchTxtField.setToolTipText("");
		addressSearchTxtField.setColumns(10);
		
		JLabel lblSearchPhoneNumber = new JLabel("Phone number:");
		lblSearchPhoneNumber.setBounds(10, 201, 110, 25);
		panel_1.add(lblSearchPhoneNumber);
		lblSearchPhoneNumber.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		phoneNumberTextField = new JTextField();
		phoneNumberTextField.setBounds(130, 201, 110, 31);
		panel_1.add(phoneNumberTextField);
		phoneNumberTextField.setToolTipText("");
		phoneNumberTextField.setColumns(10);
		
		JLabel lblSearchEmailAddress = new JLabel("Email address:");
		lblSearchEmailAddress.setBounds(10, 236, 110, 25);
		panel_1.add(lblSearchEmailAddress);
		lblSearchEmailAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		emailSearchTxtField = new JTextField();
		emailSearchTxtField.setBounds(130, 236, 110, 31);
		panel_1.add(emailSearchTxtField);
		emailSearchTxtField.setToolTipText("");
		emailSearchTxtField.setColumns(10);
		
		birthDateTextField = new JDateChooser();
		birthDateTextField.setBounds(130, 131, 110, 31);
		panel_1.add(birthDateTextField);
		
		JLabel lblTitle = new JLabel("Search for a skier");
		lblTitle.setBounds(20, 10, 1501, 52);
		contentPane.add(lblTitle);
		lblTitle.setOpaque(true);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblTitle.setBackground(new Color(0, 153, 255));
		
		JButton cancelBtn = new JButton("Back");
		cancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainMenu mainMenuFrame = new MainMenu();
				mainMenuFrame.setVisible(true);
				
				dispose();
			}
		});
		cancelBtn.setOpaque(true);
		cancelBtn.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelBtn.setContentAreaFilled(true);
		cancelBtn.setBorderPainted(false);
		cancelBtn.setBackground(new Color(255, 57, 57));
		cancelBtn.setBounds(20, 10, 154, 52);
		contentPane.add(cancelBtn);
		
		JButton btnDeleteSkier = new JButton("Delete");
		btnDeleteSkier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!ObjectValidator.hasValue(selectedSkier)) {
					warnThereIsNoSkierSlected();
					return;
				}
				
				final int userResponse = askConfirmationBeforeDeletion();
				if (userResponse != 0) {
					return;
				}
				
				final boolean isDeleted = skierDAO.delete(selectedSkier.getId());
				if(!isDeleted) {
					sendErrorWhileDeleting();
				}
				
				confirmDeletion();
			}
		});
		btnDeleteSkier.setFont(FontStyles.BUTTON);
		btnDeleteSkier.setBackground(ColorStyles.CANCEL_BUTTON);
		btnDeleteSkier.setBounds(292, 388, 110, 31);
		contentPane.add(btnDeleteSkier);
				
		loadSkierMap();
		displaySkiersInTable(skierMap.values());
	}
	
	private void warnThereIsNoSkierSlected() {
		JOptionPane.showMessageDialog(
			null, 
			"Please, select a skier.", 
			"Watch out!",
			JOptionPane.WARNING_MESSAGE
		);
	}
	
	private void confirmDeletion() {
		JOptionPane.showMessageDialog(
			null, 
			selectedSkier.getLastnameFormattedForDisplay() + " " + 
			selectedSkier.getFirstNameFormattedForDisplay() + " has been successfully deleted.",
			"Success",
			JOptionPane.WARNING_MESSAGE
		);
	}
	
	private int askConfirmationBeforeDeletion() {
		return JOptionPane.showConfirmDialog(
			null, 
			"This is a critical operation. Are you sure that you want to delete " + 
			selectedSkier.getLastnameFormattedForDisplay() + " " + 
			selectedSkier.getFirstNameFormattedForDisplay(),
			"Are you sure?",
			JOptionPane.YES_NO_OPTION
		);
	}
	
	private void sendErrorWhileDeleting() {
		JOptionPane.showMessageDialog(
			null, 
			"Error while deleting " + 
			selectedSkier.getLastnameFormattedForDisplay() + " " + 
			selectedSkier.getFirstNameFormattedForDisplay() + 
			".Please, try again later."
		);
	}
	
	private void displaySkiersInTable(Collection<Skier> skiers) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0);
		
		for (final Skier skier : skiers) {
			model.addRow(getPreparedSkierInfoForTableModel(skier));
		}
	}
	
	private void loadSkierMap() {	
		List<Skier> skiers = skierDAO.findAll();
		for (final Skier skier : skiers) {
            skierMap.put(skier.getId(), skier);
        }
	}
	
	private List<Skier> searchSkiers(
		Integer id, 
		String lastName, 
		String firstName, 
		LocalDate birthdate,
		String email, 
		String address, 
		String phoneNumber
	) {
	    if (IntegerValidator.hasValue(id)) {
	        final Skier skier = skierMap.get(id);
	        
	        return ObjectValidator.hasValue(skier) 
        		? List.of(skier) 
				: List.of();
	    }

	    Stream<Skier> stream = skierMap.values().stream();

	    stream = applyFilter(stream, lastName, Skier::getLastName);
	    stream = applyFilter(stream, firstName, Skier::getFirstName);
	    stream = applyFilter(stream, birthdate, Skier::getDateOfBirth);
	    stream = applyFilter(stream, email, Skier::getEmail);
	    stream = applyFilter(stream, address, skier -> skier.getAddress().getAddressFormattedForDisplay());
	    stream = applyFilter(stream, phoneNumber, Skier::getPhoneNumber);

	    return stream.collect(Collectors.toList());
	}

	private Stream<Skier> applyFilter(Stream<Skier> stream, String searchValue, Function<Skier, String> getter) {
	    if (!StringValidator.hasValue(searchValue)) {
	    	return stream;
	    }
	    
	    String searchValueLower = searchValue.toLowerCase();
        return stream.filter(skier -> getter.apply(skier).toLowerCase().contains(searchValueLower));
	}
	
	 private Stream<Skier> applyFilter(Stream<Skier> stream, LocalDate searchValue, Function<Skier, LocalDate> getter) {
        if (!DateValidator.hasValue(searchValue)) {
            return stream;
        }

        return stream.filter(skier -> getter.apply(skier).equals(searchValue));
    }

	
	private Object[] getPreparedSkierInfoForTableModel(Skier skier) {
		return new Object[] {
			skier.getId(),
			skier.getLastnameFormattedForDisplay() + " " + skier.getFirstNameFormattedForDisplay(),
			DatabaseFormatter.toBelgianFormat(skier.getDateOfBirth()),
			skier.getAddress().getAddressFormattedForDisplay(),
			skier.getPhoneNumber(),
			skier.getEmail()
		};
	}
	
	private String getTextField(JTextField textField) {
		final String texte = textField.getText();
		
		return texte.isBlank() ? null : texte;
	}
	
	private Integer getNumberField(JTextField textField, String fieldName) {
		final String texte = getTextField(textField);

		if(!StringValidator.hasValue(texte)) {
			return null;
		}
		
		try {
			return Integer.parseInt(texte);
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "The field " + fieldName + " must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
	        return null;
	    }
	}
	
	private void resetTextFields() {
		idSearchTxtField.setText("");
		lastNameSearchTxtField.setText("");
		firstNameSearchTxtField.setText("");
		birthDateTextField.setDate(null);
		emailSearchTxtField.setText("");
		addressSearchTxtField.setText("");
		phoneNumberTextField.setText("");
	}
}
