package feesUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings({ "serial", "unused" })
public class GenerateFeesUI extends JFrame {
    private JTextField monthField;
    private JTextField yearField;

    public GenerateFeesUI() {
        checkLockStatusOfSystem("password.txt");

    	setTitle("CMRKS Generate Fees");
    	setSize(400, 485);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setLocationRelativeTo(null);
    	setLayout(null);

    	JPanel contentPane = new JPanel();
    	contentPane.setBackground(new Color(37, 150, 190));
    	contentPane.setLayout(null);
    	setContentPane(contentPane);

    	JPanel topPanel = new JPanel();
    	topPanel.setBackground(new Color(0, 239, 209));
    	topPanel.setBounds(0, 0, 400, 40);
    	contentPane.add(topPanel);

    	int x = 110;
    	int y = 165;

    	JLabel monthLabel = new JLabel("Month");
    	monthLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	monthLabel.setBounds(x, y, 100, 25);
    	contentPane.add(monthLabel);

    	monthField = new JTextField(10);
    	monthField.setBounds(x + 80, y, 100, 25);
    	contentPane.add(monthField);

    	JLabel yearLabel = new JLabel("  Year");
    	yearLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	yearLabel.setBounds(x, y + 35, 100, 25);
    	contentPane.add(yearLabel);

    	yearField = new JTextField(10);
    	yearField.setBounds(x + 80, y + 35, 100, 25);
    	contentPane.add(yearField);

    	JButton generateButton = new JButton("Generate");
    	generateButton.setBounds(x + 77, y + 70, 106, 30);
    	generateButton.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	        generateFeesButtonClicked();
    	    }
    	});
    	contentPane.add(generateButton);
    	JPanel backPanel = new JPanel();
    	backPanel.setBackground(new Color(72, 170, 173));
    	backPanel.setBounds(15, 100, 370, 240);
    	contentPane.add(backPanel);
    	JButton cancelButton = new JButton("Back");
    	cancelButton.setBounds(320, 428, 75, 30);
    	cancelButton.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	        FeesUI.main(null);
    	        dispose();
    	    }
    	});
    	contentPane.add(cancelButton);

    	JPanel highlightPanel = new JPanel();
    	highlightPanel.setBounds(0, 422, 400, 325);
    	highlightPanel.setBackground(new Color(173, 239, 209));
    	contentPane.add(highlightPanel);

    	setVisible(true);

    }

    private void generateFeesButtonClicked() {
        int month;
        int year;

        try {
            month = Integer.parseInt(monthField.getText());
            year = Integer.parseInt(yearField.getText());

            if (month < 1 || month > 12 || year < 1000 || year > LocalDate.now().getYear()) {
                JOptionPane.showMessageDialog(this, "Invalid month or year.");
                return;
            }

            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to generate the fee report?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                generateFeeReport(month, year);
                JOptionPane.showMessageDialog(this, "Fee report generated successfully.");

            }


            // Clear the text fields
            monthField.setText("");
            yearField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.");
        }
    }

    private static void generateFeeReport(int month, int year) {

        try (BufferedReader brAttendance = new BufferedReader(new FileReader("attendance.txt"));
             BufferedReader brRecords = new BufferedReader(new FileReader("records.txt"))) {

            List<String> attendanceLines = new ArrayList<>();
            String lineAttendance;
            boolean isFirstLine = true;
            while ((lineAttendance = brAttendance.readLine()) != null) {
            	if (isFirstLine) {
                    isFirstLine = false; 
                    continue;
                }
                attendanceLines.add(lineAttendance);
            }
            
            String lineRecords;
            isFirstLine = true;
            while ((lineRecords = brRecords.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; 
                    continue;
                }

                String[] partsRecords = lineRecords.split(", ");
                String studentName = partsRecords[0];
                String phoneNumber = partsRecords[1];
                String feesPerHour = partsRecords[6];

                int numberOfClasses = getNumberOfClasses(month, year, attendanceLines, studentName);
                double balance = Double.parseDouble(feesPerHour) * numberOfClasses;

                String monthYear = String.format("%02d-%04d", month, year);
                String feeReport = ", (" + monthYear + ", " + feesPerHour + ", " + numberOfClasses + ", " + balance + ")";

                if (checkValidity(studentName)) {
                	updateFeeReport(studentName, phoneNumber, feeReport);
                }
            }

            

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateFeeReport(String studentName, String phoneNumber, String feeReport) throws IOException {
    	File inputFile = new File("fees.txt");
        File tempFile = new File("fees_temp.txt");

        boolean duplicateFound = false; 

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = br.readLine()) != null) {

                if (line.startsWith(studentName + ", " + phoneNumber)) {
                    String[] parts = line.split(", ");

                    for (int i = 0; i < parts.length; i++) {
                        if (feeReport.substring(2, 10).equals(parts[i])) {
                            duplicateFound = true;
                            break;
                        }
                    }

                    if (duplicateFound) {
                        return;
                    }

                    double currentBalance = Double.parseDouble(parts[2]);

                    line += feeReport;

                    double newBalance = 0.0;
                    String[] feeParts = feeReport.split(", ");
                    for (int i = 2; i < feeParts.length; i += 4) {
                        double feePerHour = Double.parseDouble(feeParts[i]);
                        int numberOfClasses = Integer.parseInt(feeParts[i + 1]);
                        double balance = feePerHour * numberOfClasses;
                        newBalance += balance;
                    }
                    double totalBalance = currentBalance + newBalance;

                    line = line.replace(", " + currentBalance + ", ", ", " + String.format("%.2f", newBalance) + ", ");
                    line = line.replace(parts[2], String.format("%.2f", totalBalance));
                }
                bw.write(line);
                bw.newLine();
            }
        }

        inputFile.delete();
        tempFile.renameTo(inputFile);
    }


    private static int getNumberOfClasses(int month, int year, List<String> attendanceLines, String studentName) {
        int classCount = 0;

        for (String attendanceLine : attendanceLines) {
            String[] partsAttendance = attendanceLine.split(", ");
            String currentStudentName = partsAttendance[0];

            if (currentStudentName.equals(studentName)) {
                String[] datesAttended = Arrays.copyOfRange(partsAttendance, 4, partsAttendance.length);

                for (String dateAttended : datesAttended) {
                    String[] dateParts = dateAttended.split("-");
                    int attendanceMonth = Integer.parseInt(dateParts[1]);
                    int attendanceYear = Integer.parseInt(dateParts[2]);

                    if (attendanceMonth == month && attendanceYear == year) {
                        classCount++;
                    }
                }
            }
        }

        return classCount;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GenerateFeesUI();
            }
        });
    }
}
