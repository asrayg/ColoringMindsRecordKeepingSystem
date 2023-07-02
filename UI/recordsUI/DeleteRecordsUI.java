package recordsUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

@SuppressWarnings("serial")
public class DeleteRecordsUI extends JFrame {

    private JTextField nameTextField;

    public DeleteRecordsUI() {
        checkLockStatusOfSystem("password.txt");

    	setTitle("CMRKS Delete/Renew Student Records");
        setSize(400, 485);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(37, 150, 190));

        
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(0, 239, 209));
        topPanel.setPreferredSize(new Dimension(getWidth(), 50));
        add(topPanel, BorderLayout.NORTH);


        JPanel centerPanel = new JPanel();
        
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 0, 5, 0);
        

        JLabel nameLabel = new JLabel("Enter Student's Name:");
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 12)); 
        nameTextField = new JTextField(20);
        nameTextField.setPreferredSize(new Dimension(200, 25));
        

        JButton deleteButton = new JButton("Delete");
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText().trim();
                if (!name.isEmpty()) {
                    int confirm = JOptionPane.showConfirmDialog(DeleteRecordsUI.this,
                            "Are you sure you want to delete the record for " + name + "?",
                            "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            if (checkName(name)) {
                                if (checkBalance(name)) {
                                    deleteRecords(name);
                                    JOptionPane.showMessageDialog(DeleteRecordsUI.this, "Record deleted successfully.");
                                    nameTextField.setText("");
                                } else {
                                    JOptionPane.showMessageDialog(DeleteRecordsUI.this,
                                            name + " has a balance. Record cannot be deleted without payment.",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(DeleteRecordsUI.this,
                                        "Name not found.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(DeleteRecordsUI.this,
                                    "An error occurred while deleting the record.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(DeleteRecordsUI.this,
                            "Please enter a name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton renewButton = new JButton("Renew");
        renewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameTextField.getText().trim();
                if (!name.isEmpty()) {
                    int confirm = JOptionPane.showConfirmDialog(DeleteRecordsUI.this,
                            "Are you sure you want to renew the record for " + name + "?",
                            "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            if (checkName(name)) {
                                if (!checkValidity(name)) {
                                    renewRecords(name);
                                    JOptionPane.showMessageDialog(DeleteRecordsUI.this, "Record renewed successfully.");
                                    nameTextField.setText("");
                                } else {
                                    JOptionPane.showMessageDialog(DeleteRecordsUI.this,
                                            "Name is already active.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                JOptionPane.showMessageDialog(DeleteRecordsUI.this,
                                        "Name not found.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(DeleteRecordsUI.this,
                                    "An error occurred while renewing the record.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(DeleteRecordsUI.this,
                            "Please enter a name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        centerPanel.setBackground(new Color(37, 150, 190));
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setBackground(new Color(37, 150, 190));
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(renewButton);

        

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(nameLabel, gbc);
        gbc.gridy = 1;
        centerPanel.add(nameTextField, gbc);
        gbc.gridy = 2;
        centerPanel.add(buttonsPanel, gbc);
        

        add(centerPanel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecordsUI.main(new String[0]);
                dispose();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(173, 239, 209));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
        
        
        setVisible(true);




    }
    

    private void deleteRecords(String name) throws IOException {
        File recordsFile = new File("records.txt");
        File attendanceFile = new File("attendance.txt");
        File tempRecordsFile = new File("temp_records.txt");
        File tempAttendanceFile = new File("temp_attendance.txt");

        try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
             BufferedReader brAttendance = new BufferedReader(new FileReader(attendanceFile));
             FileWriter fwRecords = new FileWriter(tempRecordsFile);
             FileWriter fwAttendance = new FileWriter(tempAttendanceFile)) {

            String recordsLine;
            String attendanceLine;

            while ((recordsLine = brRecords.readLine()) != null && (attendanceLine = brAttendance.readLine()) != null) {
                String[] recordsParts = recordsLine.split(", ");
                String[] attendanceParts = attendanceLine.split(", ");
                if (recordsParts.length > 0 && recordsParts[0].trim().equals(name)) {
                    recordsParts[recordsParts.length - 1] = "n";
                    attendanceParts[3] = "n";
                }

                fwRecords.write(String.join(", ", recordsParts) + System.lineSeparator());
                fwAttendance.write(String.join(", ", attendanceParts) + System.lineSeparator());
            }
        }

        if (recordsFile.delete() && attendanceFile.delete()) {
            tempRecordsFile.renameTo(recordsFile);
            tempAttendanceFile.renameTo(attendanceFile);
        }
    }

    private void renewRecords(String name) throws IOException {
        File recordsFile = new File("records.txt");
        File attendanceFile = new File("attendance.txt");
        File tempRecordsFile = new File("temp_records.txt");
        File tempAttendanceFile = new File("temp_attendance.txt");

        try (BufferedReader brRecords = new BufferedReader(new FileReader(recordsFile));
             BufferedReader brAttendance = new BufferedReader(new FileReader(attendanceFile));
             FileWriter fwRecords = new FileWriter(tempRecordsFile);
             FileWriter fwAttendance = new FileWriter(tempAttendanceFile)) {

            String recordsLine;
            String attendanceLine;

            while ((recordsLine = brRecords.readLine()) != null && (attendanceLine = brAttendance.readLine()) != null) {
                String[] recordsParts = recordsLine.split(", ");
                String[] attendanceParts = attendanceLine.split(", ");
                if (recordsParts.length > 0 && recordsParts[0].trim().equals(name)) {
                    recordsParts[recordsParts.length - 1] = "y";
                    attendanceParts[3] = "y";
                }

                fwRecords.write(String.join(", ", recordsParts) + System.lineSeparator());
                fwAttendance.write(String.join(", ", attendanceParts) + System.lineSeparator());
            }
        }

        if (recordsFile.delete() && attendanceFile.delete()) {
            tempRecordsFile.renameTo(recordsFile);
            tempAttendanceFile.renameTo(attendanceFile);
        }
    }

    private boolean checkBalance(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("fees.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length > 0 && parts[0].trim().equals(name)) {
                    if (!parts[2].equals("0.00")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkName(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                String nameLine = parts[0];
                if (nameLine.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkValidity(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("records.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                if (parts.length > 0 && parts[0].trim().equals(name)) {
                    String validity = parts[parts.length - 1];
                    if (validity.equals("y")) {
                        return true;
                    }
                }
            }
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


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DeleteRecordsUI();
            }
        });
    }
}
