package attendanceUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class AttendanceRecordsUI {
    private JFrame frame;
    private JTextField dateField;
    private JTextField batchField;
    private JTextArea outputArea;
    private JCheckBox markAllCheckBox;
    private JTextField customNamesField;

    public AttendanceRecordsUI() {
        checkLockStatusOfSystem("password.txt");

        initialize();
    }

    private void initialize() {
    	
        frame = new JFrame("CMRKS Add Attendance");
        frame.setBounds(100, 100, 400, 485);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.getContentPane().setBackground(new Color(37, 150, 190));
        int x =15;
        int y =20;
        int z = 20;
        
        JPanel highlightPanel1 = new JPanel();
        highlightPanel1.setBounds(0, 25, 400, 355); 
        highlightPanel1.setBackground(new Color(72, 170, 173)); 
        highlightPanel1.setLayout(null);
        
        JLabel lblDate = new JLabel("Date");
        lblDate.setFont(new Font("Verdana", Font.BOLD, 12));
        lblDate.setBounds(x+10, z+y+11, 46, 14);
        frame.getContentPane().add(lblDate);

        dateField = new JTextField();
        dateField.setBounds(x+46, z+y+8, 86, 20);
        frame.getContentPane().add(dateField);
        dateField.setColumns(10);

        JLabel lblBatch = new JLabel("Batch");
        lblBatch.setFont(new Font("Verdana", Font.BOLD, 12));
        lblBatch.setBounds(x+ 150, z+y+11, 46, 14);
        frame.getContentPane().add(lblBatch);

        batchField = new JTextField();
        batchField.setBounds(x +194, z+y+8, 86, 20);
        frame.getContentPane().add(batchField);
        batchField.setColumns(10);

        JButton btnSearchBatch = new JButton("Search");
        btnSearchBatch.setBounds(x+298, z+y+8, 68, 23);
        frame.getContentPane().add(btnSearchBatch);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(15, z+70, 370, 187);
        frame.getContentPane().add(scrollPane);

        outputArea = new JTextArea();
        scrollPane.setViewportView(outputArea);

        markAllCheckBox = new JCheckBox("All        Custom:");
        markAllCheckBox.setFont(new Font("Verdana", Font.BOLD, 12));
        markAllCheckBox.setBounds(10, z+273, 150, 23);
        frame.getContentPane().add(markAllCheckBox);


        customNamesField = new JTextField();
        customNamesField.setBounds(145, z+275, 242, 20);
        frame.getContentPane().add(customNamesField);
        customNamesField.setColumns(10);

        JButton btnRecordAttendance = new JButton("Add");
        btnRecordAttendance.setBounds(270, z+315, 115, 22);
        frame.getContentPane().add(btnRecordAttendance);
        
        frame.add(highlightPanel1);


        btnSearchBatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchBatch();
            }
        });

        btnRecordAttendance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					recordAttendance();
				} catch (HeadlessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        
        JButton cancelButton = new JButton("Back");
        cancelButton.setBounds(320, 428, 75, 30);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AttendanceUI.main(null);
                frame.dispose();
            }
        });
        frame.add(cancelButton);

        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(0, 422, 400, 325);
        highlightPanel.setBackground(new Color(173, 239, 209));
        frame.add(highlightPanel);
    }


    private void recordAttendance() throws HeadlessException, FileNotFoundException, IOException {
        String date = dateField.getText().trim();
        String batch = batchField.getText().trim();
        boolean markAllSelected = markAllCheckBox.isSelected();
        String customNames = customNamesField.getText().trim();

        // Validate date format
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);
        Date formattedDate;
        try {
            formattedDate = dateFormat.parse(date);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid date format. Please enter in dd-MM-yyyy format.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validate batch type
        boolean isValidBatchType = false;
        while (!isValidBatchType) {
            batch = batch.toUpperCase();
            if (batch.matches("[MTWRFSU1234567890]+")) {
                isValidBatchType = true;
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Invalid batch type. Please enter from M,T,W,R,F,S,U,1,2,3,4,5,6,7,8,9,0 only.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                batch = JOptionPane.showInputDialog(frame, "Batch Type:");
                if (batch == null) {
                    return;
                }
            }
        }

        File file = new File("attendance.txt");
        File tempFile = new File("temp.txt");

        if (markAllSelected && !customNames.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select either 'Mark All' or enter custom names, not both.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (markAllSelected) {
            int option = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to mark attendance for all students in batch " + batch + "?", "Confirm Attendance",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try (BufferedReader br = new BufferedReader(new FileReader(file));
                        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(", ");
                        if (parts.length >= 3 && parts[2].trim().equals(batch) && !parts[3].equals("n")) {
                            DateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String formattedDateString = outputDateFormat.format(formattedDate);
                            parts[parts.length - 1] += ", " + formattedDateString;
                        }

                        String updatedLine = String.join(", ", parts);
                        bw.write(updatedLine);
                        bw.newLine();
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "An error occurred while marking attendance.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }

                // Rename the temporary file to the original file
                if (file.delete()) {
                    if (!tempFile.renameTo(file)) {
                        JOptionPane.showMessageDialog(frame, "An error occurred while updating attendance.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "An error occurred while updating attendance.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(frame, "Attendance marked successfully for batch " + batch + ".", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } else if (!customNames.isEmpty() ) {
            String[] names = customNames.split(",");
            List<String> validNames = new ArrayList<>();

            // Validate custom names
            for (String name : names) {
                String trimmedName = name.trim();
                if (!trimmedName.isEmpty()) {
                    if (checkName(trimmedName) && checkValidity(trimmedName)) {
                        validNames.add(trimmedName);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid name entered: " + trimmedName, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }


            if (validNames.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No valid names entered.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int option = JOptionPane.showConfirmDialog(frame,
                    "Are you sure you want to mark attendance for the following custom names?\n",
                    "Confirm Attendance", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try (BufferedReader br = new BufferedReader(new FileReader(file));
                        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(", ");
                        String studentName = parts[0].trim();
                        if (validNames.contains(studentName) && !parts[3].equals("n")) {
                            DateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                            String formattedDateString = outputDateFormat.format(formattedDate);
                            parts[parts.length - 1] += ", " + formattedDateString;
                        }

                        String updatedLine = String.join(", ", parts);
                        bw.write(updatedLine);
                        bw.newLine();
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "An error occurred while marking attendance.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }

                if (file.delete()) {
                    if (!tempFile.renameTo(file)) {
                        JOptionPane.showMessageDialog(frame, "An error occurred while updating attendance.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "An error occurred while updating attendance.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(frame,
                        "Attendance marked successfully for custom names:\n", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
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

	private void searchBatch() {
        String batchOption = batchField.getText().trim();

        // Validate batch type
        boolean isValidBatchType = false;
        while (!isValidBatchType) {
            batchOption = batchOption.toUpperCase();
            if (batchOption.matches("[MTWRFSU1234567890]+")) {
                isValidBatchType = true;
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Invalid batch type. Please enter from M,T,W,R,F,S,U,1,2,3,4,5,6,7,8,9,0 only.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                batchOption = JOptionPane.showInputDialog(frame, "Batch Type:");
                if (batchOption == null) {
                    return;
                }
            }
        }

        File file = new File("attendance.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            ArrayList<String> names = new ArrayList<>();
            StringBuilder outputBuilder = new StringBuilder();

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length >= 3 && parts[2].trim().equals(batchOption) && !parts[3].equals("n")) {
                    outputBuilder.append(parts[0]).append("\n");
                    names.add(parts[0]);
                }
            }

            br.close();

            if (names.size() == 0) {
                JOptionPane.showMessageDialog(frame, "No such batch exists. Try again.", "Batch Not Found",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            outputArea.setText(outputBuilder.toString());

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "An error occurred while searching batch.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AttendanceRecordsUI window = new AttendanceRecordsUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private static boolean checkName(String name) {
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

}
