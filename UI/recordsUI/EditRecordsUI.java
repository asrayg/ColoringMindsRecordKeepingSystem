package recordsUI;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class EditRecordsUI {
    private JFrame frame;
    private JTextField nameTextField;
    private JButton searchButton;
    private JTextArea outputTextArea;
    private JComboBox<String> editOptionsComboBox;
    private JTextField editValueTextField;
    private JButton editButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                EditRecordsUI window = new EditRecordsUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public EditRecordsUI() {
        checkLockStatusOfSystem("password.txt");

    	initialize();
    }

    private void initialize() {
    	frame = new JFrame("CMRKS Edit Student Records");
        frame.setBounds(100, 100, 400, 485);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set background color of the content pane
        JPanel contentPane = new JPanel();
        contentPane.setBackground(new Color(37, 150, 190));
        contentPane.setLayout(null);
        frame.setContentPane(contentPane);


    	
    	JLabel nameLabel = new JLabel("Enter name:");
    	nameLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	nameLabel.setBounds(14, 20, 250, 20);
    	frame.getContentPane().add(nameLabel);

    	nameTextField = new JTextField();
    	nameTextField.setBounds(10, 45, 270, 20);
    	frame.getContentPane().add(nameTextField);
    	nameTextField.setColumns(10);

    	searchButton = new JButton("Search");
    	searchButton.setBounds(290, 45, 100, 20);
    	frame.getContentPane().add(searchButton);
    	searchButton.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	        String name = nameTextField.getText().trim();
    	        if (checkName(name)) {
    	            try {
    	                getName(name);
    	            } catch (IOException e1) {
    	                e1.printStackTrace();
    	            }

    	        } else {
    	            outputTextArea.setText("Name not found. Please try again.");
    	        }
    	    }
    	});

    	JLabel outputLabel = new JLabel("Current Info:");
    	outputLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	outputLabel.setBounds(12, 80, 200, 20);
    	frame.getContentPane().add(outputLabel);

    	outputTextArea = new JTextArea();
    	outputTextArea.setBounds(10, 105, 380, 150);
    	frame.getContentPane().add(outputTextArea);

    	JLabel editOptionsLabel = new JLabel("Select the field you'd like to edit:");
    	editOptionsLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	editOptionsLabel.setBounds(14, 270, 270, 20);
    	frame.getContentPane().add(editOptionsLabel);

    	String[] editOptions = { "Ph No", "Start Date", "Age", "Batch Type", "Mode", "Fees Per Hour", "Email" };
    	editOptionsComboBox = new JComboBox<String>(editOptions);
    	editOptionsComboBox.setBounds(10, 295, 270, 20);
    	frame.getContentPane().add(editOptionsComboBox);

    	JLabel editValueLabel = new JLabel("Enter new value:");
    	editValueLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	editValueLabel.setBounds(14, 330, 250, 20);
    	frame.getContentPane().add(editValueLabel);

    	editValueTextField = new JTextField();
    	editValueTextField.setBounds(10, 355, 270, 20);
    	frame.getContentPane().add(editValueTextField);
    	editValueTextField.setColumns(10);

    	editButton = new JButton("Edit");
    	editButton.setBounds(290, 355, 100, 20);
    	frame.getContentPane().add(editButton);

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText().trim();
                String field = editOptionsComboBox.getSelectedItem().toString();
                String value = editValueTextField.getText().trim();

                StringBuilder errorMessage = new StringBuilder();
                boolean hasError = false;

                if (name.isEmpty()) {
                    errorMessage.append("Name field is empty. Please enter a name.\n");
                    hasError = true;
                }
                
                try {
					if (!checkValidity(name)) {
					    errorMessage.append("Name is invalid. Please renew it.\n");
					    hasError = true;
					}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
                

                if (field.equals("Ph No")) {
                    if (value.isEmpty()) {
                        errorMessage.append("Phone number field is empty. Please enter a phone number.\n");
                        hasError = true;
                    } else if (!value.matches("\\d{10}")) {
                        errorMessage.append("Invalid phone number format. Please enter 10 digits.\n");
                        hasError = true;
                    }
                } else if (field.equals("Start Date")) {
                    if (value.isEmpty()) {
                        errorMessage.append("Start date field is empty. Please enter a date.\n");
                        hasError = true;
                    } else {
                        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        try {
                            LocalDate.parse(value, dateFormatter);
                        } catch (DateTimeParseException ex) {
                            errorMessage.append("Invalid date format. Please enter a valid date in the format (dd-MM-yyyy).\n");
                            hasError = true;
                        }
                    }
                } else if (field.equals("Age")) {
                    if (value.isEmpty()) {
                        errorMessage.append("Age field is empty. Please enter an age.\n");
                        hasError = true;
                    } else {
                        try {
                            int age = Integer.parseInt(value);
                            if (age <= 0) {
                                errorMessage.append("Invalid age. Please enter a valid number.\n");
                                hasError = true;
                            }
                        } catch (NumberFormatException ex) {
                            errorMessage.append("Invalid age. Please enter a valid number.\n");
                            hasError = true;
                        }
                    }
                } else if (field.equals("Batch Type")) {
                    if (value.isEmpty()) {
                        errorMessage.append("Batch type field is empty. Please enter a batch type.\n");
                        hasError = true;
                    } else if (!value.matches("[MTWRFSU1234567890]+")) {
                        errorMessage.append("Invalid batch type. Please enter from M,T,W,R,F,S,U,1,2,3,4,5,6,7,8,9 only.\n");
                        hasError = true;
                    }
                } else if (field.equals("Mode")) {
                    if (value.isEmpty()) {
                        errorMessage.append("Mode of instruction field is empty. Please enter a mode of instruction.\n");
                        hasError = true;
                    } else if (!value.equalsIgnoreCase("Online") && !value.equalsIgnoreCase("Offline")
                            && !value.equalsIgnoreCase("Combined")) {
                        errorMessage.append("Invalid mode of instruction. Please enter Online, Offline, or Combined.\n");
                        hasError = true;
                    }
                } else if (field.equals("Fees Per Hour")) {
                    if (value.isEmpty()) {
                        errorMessage.append("Fees field is empty. Please enter fees.\n");
                        hasError = true;
                    } else {
                        try {
                            double fees = Double.parseDouble(value);
                            if (fees <= 0) {
                                errorMessage.append("Invalid fees. Please enter a valid number.\n");
                                hasError = true;
                            }
                        } catch (NumberFormatException ex) {
                            errorMessage.append("Invalid fees. Please enter a valid number.\n");
                            hasError = true;
                        }
                    }
                } else if (field.equals("Email")) {
                    if (value.isEmpty()) {
                        errorMessage.append("Email field is empty. Please enter an email.\n");
                        hasError = true;
                    } else if (!value.matches(".+@.+\\.com")) {
                        errorMessage.append("Invalid email format. Please enter a valid email address.\n");
                        hasError = true;
                    }
                }

                if (hasError) {
                    JOptionPane.showMessageDialog(frame, errorMessage.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    int confirmResult = JOptionPane.showConfirmDialog(frame, "Confirm information?", "Confirmation",
                            JOptionPane.YES_NO_OPTION);
                    if (confirmResult == JOptionPane.YES_OPTION) {
                        if (field.equals("Ph No")) {
                            try {
                                editPhoneNumber(name, value);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        } else if (field.equals("Start Date")) {
                            try {
								editStartDate(name, value);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
                        } else if (field.equals("Age")) {
                            try {
								editAge(name, value);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
                        } else if (field.equals("Batch Type")) {
                            try {
								editBatchType(name, value);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
                        } else if (field.equals("Mode")) {
                            try {
								editMode(name, value);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
                        } else if (field.equals("Fees Per Hour")) {
                            try {
								editFeesPerHour(name, value);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
                        } else if (field.equals("Email")) {
                            try {
								editEmail(name, value);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
                        }
                    }
                }
            }
        });
        JButton cancelButton = new JButton("Back");
        cancelButton.setBounds(320, 428, 75, 30);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RecordsUI.main(null);
                frame.dispose();
            }
        });
        frame.add(cancelButton);
        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(0, 422, 400, 325); 
        highlightPanel.setBackground(new Color(173, 239, 209)); 
        highlightPanel.setLayout(null);
        
        frame.add(highlightPanel);

        frame.setVisible(true);
        
       
    }
    
    

    private boolean checkName(String name) {
    	try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader("records.txt"));
			String line;
			boolean isFirstLinePayment = true;
			while ((line = br.readLine()) != null) {
				if (isFirstLinePayment) {
					isFirstLinePayment = false;
					continue;
				}

				String[] parts = line.split(", ");
				String nameLine = parts[0];

				if (nameLine.equals(name)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
    }

    private void getName(String option) throws FileNotFoundException, IOException {
    	try (BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(", ");
				if (parts.length > 0 && parts[0].trim().equals(option) && !parts[parts.length - 1].equals("n")) {
					outputTextArea.setText("");
					outputTextArea.append("   Name           : " + parts[0] + "\n");
					outputTextArea.append("   Phone Number   : " + parts[1] + "\n");
			        outputTextArea.append("   Start Date     : " + parts[2] + "\n");
			        outputTextArea.append("   Age            : " + parts[3] + "\n");
			        outputTextArea.append("   Batch Type     : " + parts[4] + "\n");
			        outputTextArea.append("   Mode           : " + parts[5] + "\n");
			        outputTextArea.append("   Fees Per Hour  : " + parts[6] + "\n");
			        outputTextArea.append("   Email          : " + parts[7] + "\n");
			        outputTextArea.append("\n");


			    	Font lucidaFont = new Font("Menlo", Font.PLAIN, outputTextArea.getFont().getSize());
			    	outputTextArea.setFont(lucidaFont);
				} else if (parts.length > 0 && parts[0].trim().equals(option) && parts[parts.length - 1].equals("n")) {
					outputTextArea.setText("");
					outputTextArea.setText("Name is invalid. Please renew it.");
				}
			}
		}

    }
    
    private static void checkLockStatusOfSystem(String passwordFile) {
        String thirdLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader(passwordFile))) {
            String line;
            boolean isFirstLine = true;
            int lineCount = 0;

            while ((line = br.readLine()) != null && lineCount < 3) {
                if (isFirstLine) {
                    isFirstLine = false;
                } else if (lineCount == 2) {
                    thirdLine = line;
                    break;
                }
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (thirdLine.equals("locked")) {
            System.exit(0);
        }
    }


    private void editPhoneNumber(String name, String phoneNumber) throws IOException {
    	File recordsFile = new File("records.txt");
		File attendanceFile = new File("attendance.txt");
		File feesFile = new File("fees.txt");
		File paymentFile = new File("payment.txt");
		String tempRecordsFile = "temp_records.txt";
		String tempAttendanceFile = "temp_attendance.txt";
		String tempFeesFile = "temp_fees.txt";
		String tempPaymentFile = "temp_payment.txt";

		try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
				BufferedReader brAttendance = new BufferedReader(new FileReader(attendanceFile));
				BufferedReader brFees = new BufferedReader(new FileReader(feesFile));
				BufferedReader brPayment = new BufferedReader(new FileReader(paymentFile));
				FileWriter fwRecords = new FileWriter(tempRecordsFile, true);
				FileWriter fwAttendance = new FileWriter(tempAttendanceFile, true);
				FileWriter fwFees = new FileWriter(tempFeesFile, true);
				FileWriter fwPayment = new FileWriter(tempPaymentFile, true)) {
    	
			String lineRecords;
			while ((lineRecords = brRecords.readLine()) != null) {
				String[] partsRecords = lineRecords.split(", ");
				if (partsRecords.length > 0 && partsRecords[0].trim().equals(name)) {
					partsRecords[1] = phoneNumber;
				}
	
				fwRecords.write(String.join(", ", partsRecords) + System.lineSeparator());
			}
			String lineAttendance;
			while ((lineAttendance = brAttendance.readLine()) != null) {
				String[] partsAttendance = lineAttendance.split(", ");
				if (partsAttendance.length > 0 && partsAttendance[0].trim().equals(name)) {
					partsAttendance[1] = phoneNumber;
				}

				fwAttendance.write(String.join(", ", partsAttendance) + System.lineSeparator());
			}

			String lineFees;
			while ((lineFees = brFees.readLine()) != null) {
				String[] partsFees = lineFees.split(", ");
				if (partsFees.length > 0 && partsFees[0].trim().equals(name) ) {
					partsFees[1] = phoneNumber;
				}

				fwFees.write(String.join(", ", partsFees) + System.lineSeparator());
			}

			String linePayment;
			while ((linePayment = brPayment.readLine()) != null) {
				String[] partsPayment = linePayment.split(", ");
				if (partsPayment.length > 0 && partsPayment[0].trim().equals(name)) {
					partsPayment[1] = phoneNumber;
				}

				fwPayment.write(String.join(", ", partsPayment) + System.lineSeparator());
			}

			fwRecords.flush();
			fwRecords.close();
			fwAttendance.flush();
			fwAttendance.close();
			fwFees.flush();
			fwFees.close();
			fwPayment.flush();
			fwPayment.close();

			File tempRecords = new File(tempRecordsFile);
			if (recordsFile.delete()) {
				tempRecords.renameTo(recordsFile);
			}

			File tempAttendance = new File(tempAttendanceFile);
			if (attendanceFile.delete()) {
				tempAttendance.renameTo(attendanceFile);
			}

			File tempFees = new File(tempFeesFile);
			if (feesFile.delete()) {
				tempFees.renameTo(feesFile);
			}

			File tempPayment = new File(tempPaymentFile);
			if (paymentFile.delete()) {
				tempPayment.renameTo(paymentFile);
			}
			
		}
    }

    private void editStartDate(String name, String startDate) throws FileNotFoundException, IOException {
    	File recordsFile = new File("records.txt");
		
		String tempRecordsFile = "temp_records.txt";
		

		try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
				FileWriter fwRecords = new FileWriter(tempRecordsFile, true);)
				 {
    	
			String lineRecords;
			while ((lineRecords = brRecords.readLine()) != null) {
				String[] partsRecords = lineRecords.split(", ");
				if (partsRecords.length > 0 && partsRecords[0].trim().equals(name)) {
					partsRecords[2] = startDate;
				}
	
				fwRecords.write(String.join(", ", partsRecords) + System.lineSeparator());
			}

			fwRecords.flush();
			fwRecords.close();
			
			File tempRecords = new File(tempRecordsFile);
			if (recordsFile.delete()) {
				tempRecords.renameTo(recordsFile);
			}

		}
    }

    private void editAge(String name, String age) throws IOException {
    	File recordsFile = new File("records.txt");
		
		String tempRecordsFile = "temp_records.txt";
		

		try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
				FileWriter fwRecords = new FileWriter(tempRecordsFile, true);)
				 {
    	
			String lineRecords;
			while ((lineRecords = brRecords.readLine()) != null) {
				String[] partsRecords = lineRecords.split(", ");
				if (partsRecords.length > 0 && partsRecords[0].trim().equals(name)) {
					partsRecords[3] = age;
				}
	
				fwRecords.write(String.join(", ", partsRecords) + System.lineSeparator());
			}

			fwRecords.flush();
			fwRecords.close();
			
			File tempRecords = new File(tempRecordsFile);
			if (recordsFile.delete()) {
				tempRecords.renameTo(recordsFile);
			}

		}
    }

    private void editBatchType(String name, String batchType) throws FileNotFoundException, IOException {
    	File recordsFile = new File("records.txt");
		File attendanceFile = new File("attendance.txt");
		String tempRecordsFile = "temp_records.txt";
		String tempAttendanceFile = "temp_attendance.txt";

		try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
				BufferedReader brAttendance = new BufferedReader(new FileReader(attendanceFile));
				FileWriter fwRecords = new FileWriter(tempRecordsFile, true);
				FileWriter fwAttendance = new FileWriter(tempAttendanceFile, true)) {
    	
			String lineRecords;
			while ((lineRecords = brRecords.readLine()) != null) {
				String[] partsRecords = lineRecords.split(", ");
				if (partsRecords.length > 0 && partsRecords[0].trim().equals(name)) {
					partsRecords[4] = batchType;
				}
	
				fwRecords.write(String.join(", ", partsRecords) + System.lineSeparator());
			}
			String lineAttendance;
			while ((lineAttendance = brAttendance.readLine()) != null) {
				String[] partsAttendance = lineAttendance.split(", ");
				if (partsAttendance.length > 0 && partsAttendance[0].trim().equals(name)) {
					partsAttendance[2] = batchType;
				}

				fwAttendance.write(String.join(", ", partsAttendance) + System.lineSeparator());
			}

			

			fwRecords.flush();
			fwRecords.close();
			fwAttendance.flush();
			fwAttendance.close();
			

			File tempRecords = new File(tempRecordsFile);
			if (recordsFile.delete()) {
				tempRecords.renameTo(recordsFile);
			}

			File tempAttendance = new File(tempAttendanceFile);
			if (attendanceFile.delete()) {
				tempAttendance.renameTo(attendanceFile);
			}

			
		}
    }

    private void editMode(String name, String mode) throws IOException {
    	File recordsFile = new File("records.txt");
		
		String tempRecordsFile = "temp_records.txt";
		

		try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
				FileWriter fwRecords = new FileWriter(tempRecordsFile, true);)
				 {
    	
			String lineRecords;
			while ((lineRecords = brRecords.readLine()) != null) {
				String[] partsRecords = lineRecords.split(", ");
				if (partsRecords.length > 0 && partsRecords[0].trim().equals(name)) {
					partsRecords[5] = mode;
				}
	
				fwRecords.write(String.join(", ", partsRecords) + System.lineSeparator());
			}

			fwRecords.flush();
			fwRecords.close();
			
			File tempRecords = new File(tempRecordsFile);
			if (recordsFile.delete()) {
				tempRecords.renameTo(recordsFile);
			}

		}
    }

    private void editFeesPerHour(String name, String feesPerHour) throws IOException {
    	File recordsFile = new File("records.txt");
		
		String tempRecordsFile = "temp_records.txt";
		

		try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
				FileWriter fwRecords = new FileWriter(tempRecordsFile, true);)
				 {
    	
			String lineRecords;
			while ((lineRecords = brRecords.readLine()) != null) {
				String[] partsRecords = lineRecords.split(", ");
				if (partsRecords.length > 0 && partsRecords[0].trim().equals(name)) {
					partsRecords[6] = feesPerHour;
				}
	
				fwRecords.write(String.join(", ", partsRecords) + System.lineSeparator());
			}

			fwRecords.flush();
			fwRecords.close();
			
			File tempRecords = new File(tempRecordsFile);
			if (recordsFile.delete()) {
				tempRecords.renameTo(recordsFile);
			}

		}
    }

    private void editEmail(String name, String email) throws IOException {
        File recordsFile = new File("records.txt");
		
		String tempRecordsFile = "temp_records.txt";
		

		try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
				FileWriter fwRecords = new FileWriter(tempRecordsFile, true);)
				 {
    	
			String lineRecords;
			while ((lineRecords = brRecords.readLine()) != null) {
				String[] partsRecords = lineRecords.split(", ");
				if (partsRecords.length > 0 && partsRecords[0].trim().equals(name)) {
					partsRecords[7] = email;
				}
	
				fwRecords.write(String.join(", ", partsRecords) + System.lineSeparator());
			}

			fwRecords.flush();
			fwRecords.close();
			
			File tempRecords = new File(tempRecordsFile);
			if (recordsFile.delete()) {
				tempRecords.renameTo(recordsFile);
			}

		}
    }

    
    private static boolean checkValidity(String option) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(", ");
				if (parts.length > 0 && parts[0].trim().equals(option)) {
					if (parts[8].equals("y")) {
						return true;
					}
				}
			}
		}
		return false;
	}
}