package attendanceUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class DeleteAttendanceUI extends JFrame {
    private JTextField namesTextField;
    private JTextField dateToDeleteTextField;
    private static JTextArea outputTextArea;

    public DeleteAttendanceUI() {
        checkLockStatusOfSystem("password.txt");

    	setTitle("CMRKS Delete Attendance");
        setSize(400, 485);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(37, 150, 190)); 
        
        JLabel namesLabel = new JLabel("Enter name/s:");
        namesLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        namesLabel.setBounds(20, 10, 100, 25);
        add(namesLabel);

        namesTextField = new JTextField();
        namesTextField.setBounds(16, 37, 270, 25);
        add(namesTextField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(290, 35, 100, 30);
        add(searchButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchAttendance();
            }
        });

        outputTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        scrollPane.setBounds(18, 72, 364, 238);
        add(scrollPane);

        JLabel dateToDeleteLabel = new JLabel("Enter date or 'l' for last date:");
        dateToDeleteLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        dateToDeleteLabel.setBounds(20, 315, 200, 25);
        add(dateToDeleteLabel);

        dateToDeleteTextField = new JTextField();
        dateToDeleteTextField.setBounds(15, 342, 270, 25);
        add(dateToDeleteTextField);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(290, 340, 100, 30);
        add(deleteButton);


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String namesInput = namesTextField.getText().trim();
                String dateToDelete = dateToDeleteTextField.getText().trim();

                if (namesInput.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter the names.", "Missing Names", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String[] namesArray = namesInput.split(",");
                for (int i = 0; i < namesArray.length; i++) {
                    namesArray[i] = namesArray[i].trim();
                    if (!checkName(namesArray[i])) {
                        JOptionPane.showMessageDialog(null, "Invalid name: " + namesArray[i], "Invalid Name", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                if (!dateToDelete.equalsIgnoreCase("l")) {
                    if (!checkDateFormat(dateToDelete)) {
                        JOptionPane.showMessageDialog(null, "Invalid date format. Please enter a valid date in the format dd-MM-yyyy or 'l' to delete the last date.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                int option = JOptionPane.showConfirmDialog(null, "Confirm information.", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    try {
                        deleteAttendance(namesArray, dateToDelete);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        JButton cancelButton = new JButton("Back");
        cancelButton.setBounds(320, 428, 75, 30);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AttendanceUI.main(null);
                dispose();
            }
        });
        add(cancelButton);

        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(0, 422, 400, 325);
        highlightPanel.setBackground(new Color(173, 239, 209));
        add(highlightPanel);

        setVisible(true);
        


    }
    
    private void searchAttendance() {
        String namesInput = namesTextField.getText().trim();
        if (namesInput.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter the names.", "Missing Names", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] namesArray = namesInput.split(",");
        for (int i = 0; i < namesArray.length; i++) {
            namesArray[i] = namesArray[i].trim();
            if (!checkName(namesArray[i])) {
                JOptionPane.showMessageDialog(null, "Invalid name: " + namesArray[i], "Invalid Name", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        getPreviousAttendance(namesArray);
    }
    private void deleteAttendance(String[] names, String dateToDelete) throws IOException {
        File file = new File("attendance.txt");
        String tempFile = "temp.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(file));
             FileWriter fw = new FileWriter(tempFile, true)) {

            List<String> modifiedLines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                boolean tempVariable = false;
                if (dateToDelete.equals("l") && parts.length == 4 && containsName(parts[0], names)) {
                    JOptionPane.showMessageDialog(null, "There was no attendance for " + parts[0] + " to delete", "Attendance Not Found", JOptionPane.WARNING_MESSAGE);
                }
                if (parts.length >= 5 && containsName(parts[0], names) && parts[3].equals("y")) {
                    if (!dateToDelete.equals("l")) {
                        if (!checkDate(parts[0], parts, dateToDelete)) {
                            JOptionPane.showMessageDialog(null, "There was no attendance for " + parts[0] + " on " + dateToDelete + " to delete", "Attendance Not Found", JOptionPane.WARNING_MESSAGE);
                        }
                    }

                    if (dateToDelete.equals("l")) {
                        tempVariable = true;
                        dateToDelete = parts[parts.length - 1];
                    }
                    List<String> dates = new ArrayList<>();
                    for (int i = 4; i < parts.length; i++) {
                        if (!parts[i].trim().equals(dateToDelete)) {
                            dates.add(parts[i]);
                        }
                    }
                    parts = Arrays.copyOf(parts, 4 + dates.size());
                    for (int i = 0; i < dates.size(); i++) {
                        parts[4 + i] = dates.get(i);
                    }
                } else if (containsName(parts[0], names) && parts[3].equals("n")) {
                    JOptionPane.showMessageDialog(null, "Name is invalid. Please renew.", "Attendance Not Found", JOptionPane.WARNING_MESSAGE);
                }

                modifiedLines.add(String.join(", ", parts));
                if (tempVariable) {
                    dateToDelete = "l";
                }
            }

            FileWriter originalFileWriter = new FileWriter(file);
            for (String modifiedLine : modifiedLines) {
                originalFileWriter.write(modifiedLine + System.lineSeparator());
            }
            originalFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean containsName(String name, String[] names) {
        for (String n : names) {
            if (n.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDateFormat(String date) {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(date, dateFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean checkDate(String name, String[] parts, String date) {
        for (int i = 4; i < parts.length; i++) {
            if (parts[i].trim().equals(date)) {
                return true;
            }
        }
        return false;
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

    
    private static void getPreviousAttendance(String[] names) {
        try (BufferedReader br = new BufferedReader(new FileReader("attendance.txt"))) {
            StringBuilder output = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
            	
                String[] parts = line.split(", ");
                if(parts[3].equals("y")) {
	                for (String name : names) {
	                    if (parts[0].contains(name) && parts.length > 4) {
	                        output.append("Name: ").append(parts[0]).append("\t");
	                        output.append("\n");
	                        int numOfDates = Math.min(5, parts.length - 4);
	                        output.append("Last ").append(numOfDates).append(" Dates Attended: ");
	                        output.append("\n");
	                        for (int i = parts.length - numOfDates; i < parts.length; i++) {
	                            output.append("  ").append(parts[i]).append(" ");
	                            output.append("\n");
	                        }
	                        output.append("\n");
	                    }
	                }
                } else if (parts[3].equals("n")){
                	for (String name : names) {
	                    if (parts[0].contains(name)){
	                    	output.append(parts[0] + " is invalid. Please renew");
	                    }
                	}
                }
            }

            Font lucidaFont = new Font("Menlo", Font.BOLD, outputTextArea.getFont().getSize());
	    	outputTextArea.setFont(lucidaFont);
            if (output.length() > 0) {
                try {
					outputTextArea.setText(output.toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else {
                try {
					outputTextArea.setText("No attendance found for the entered names.");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DeleteAttendanceUI deleteAttendanceUI = new DeleteAttendanceUI();
            deleteAttendanceUI.setVisible(true);
        });
    }
}
