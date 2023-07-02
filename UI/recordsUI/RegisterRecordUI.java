package recordsUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import records.RegisterRecord;


public class RegisterRecordUI {
    private JFrame frame;
    private JTextField nameField;
    private JTextField phNoField;
    private JTextField batchTypeField;
    private JComboBox<String> modeComboBox;
    private JTextField ageField;
    private JTextField feesField;
    private JTextField emailField;
    private JFormattedTextField startDateField;

    public RegisterRecordUI() {
        checkLockStatusOfSystem("password.txt");

    	initialize();
    }

    private void initialize() {
        frame = new JFrame("CMRKS Register Student");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 485);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(37, 150, 190));
        
        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(15, 20, 370, 325); 
        highlightPanel.setBackground(new Color(72, 170, 173)); 
        highlightPanel.setLayout(null);
        

        Font labelFont = new Font("Verdana", Font.BOLD, 12);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 30, 100, 20);
        nameLabel.setFont(labelFont);
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 30, 225, 20);
        panel.add(nameField);

        JLabel phNoLabel = new JLabel("Ph No:");
        phNoLabel.setBounds(30, 70, 100, 20);
        phNoLabel.setFont(labelFont);
        panel.add(phNoLabel);

        phNoField = new JTextField();
        phNoField.setBounds(130, 70, 225, 20);
        panel.add(phNoField);

        JLabel startDateLabel = new JLabel("Start Date:");
        startDateLabel.setBounds(30, 110, 100, 20);
        startDateLabel.setFont(labelFont);
        panel.add(startDateLabel);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        startDateField = new JFormattedTextField(dateFormatter.toFormat());
        startDateField.setBounds(130, 110, 225, 20);
        panel.add(startDateField);

        JLabel batchTypeLabel = new JLabel("Batch Type:");
        batchTypeLabel.setBounds(30, 150, 100, 20);
        batchTypeLabel.setFont(labelFont);
        panel.add(batchTypeLabel);

        batchTypeField = new JTextField();
        batchTypeField.setBounds(130, 150, 225, 20);
        panel.add(batchTypeField);

        JLabel modeLabel = new JLabel("Mode:");
        modeLabel.setBounds(30, 190, 150, 20);
        modeLabel.setFont(labelFont);
        panel.add(modeLabel);

        String[] modes = {"Online", "Offline", "Combined"};
        modeComboBox = new JComboBox<>(modes);
        modeComboBox.setBounds(130, 190, 230, 20);
        panel.add(modeComboBox);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setBounds(30, 230, 100, 20);
        ageLabel.setFont(labelFont);
        panel.add(ageLabel);

        ageField = new JTextField();
        ageField.setBounds(130, 230, 225, 20);
        panel.add(ageField);

        JLabel feesLabel = new JLabel("Fees/Hour:");
        feesLabel.setBounds(30, 270, 100, 20);
        feesLabel.setFont(labelFont);
        panel.add(feesLabel);

        feesField = new JTextField();
        feesField.setBounds(130, 270, 225, 20);
        panel.add(feesField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 310, 100, 20);
        emailLabel.setFont(labelFont);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(130, 310, 225, 20);
        panel.add(emailField);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.setBounds(245, 360, 115, 30);
        confirmButton.setBackground(Color.green);
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmButtonClicked();
            }
        });
        panel.add(confirmButton);

        
        
        JPanel highlightPanel1 = new JPanel();
        highlightPanel1.setBounds(0, 422, 400, 325); 
        highlightPanel1.setBackground(new Color(173, 239, 209)); 
        highlightPanel1.setLayout(null);
        
        
        
        JButton cancelButton = new JButton("Back");
        cancelButton.setBounds(320, 428, 75, 30);
        cancelButton.setBackground(new Color(173, 239, 209)); 
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RecordsUI.main(null);
                frame.dispose();
            }
        });
        
        
        panel.add(cancelButton);
        panel.add(highlightPanel);
        panel.add(highlightPanel1);
        
        
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int frameWidth = frame.getSize().width;
        int frameHeight = frame.getSize().height;
        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;
        frame.setLocation(x, y);

        frame.getContentPane().add(panel);
    }

    private void confirmButtonClicked() {
        StringBuilder errorMessage = new StringBuilder();

        String name = nameField.getText();
        String phno = phNoField.getText();
        String date = startDateField.getText();
        String batchType = batchTypeField.getText();
        String mode = modeComboBox.getSelectedItem().toString();
        String ageText = ageField.getText();
        String feesText = feesField.getText();
        String email = emailField.getText();

        boolean hasError = false;

        if (name.isEmpty()) {
            errorMessage.append("Name field is empty. Please enter a name.\n");
            hasError = true;
        }

        if (phno.isEmpty()) {
            errorMessage.append("Phone number field is empty. Please enter a phone number.\n");
            hasError = true;
        } else if (!phno.matches("\\d{10}")) {
            errorMessage.append("Invalid phone number format. Please enter 10 digits.\n");
            hasError = true;
        }

        if (date.isEmpty()) {
            errorMessage.append("Start date field is empty. Please enter a date.\n");
            hasError = true;
        } else {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            try {
                LocalDate.parse(date, dateFormatter);
            } catch (DateTimeParseException e) {
                errorMessage.append("Invalid date format. Please enter a valid date in the format (dd-MM-yyyy).\n");
                hasError = true;
            }
        }

        if (batchType.isEmpty()) {
            errorMessage.append("Batch type field is empty. Please enter a batch type.\n");
            hasError = true;
        } else if (!batchType.matches("[MTWRFSU1234567890]+")) {
            errorMessage.append("Invalid batch type. Please enter from M,T,W,R,F,S,U,1,2,3,4,5,6,7,8,9 only.\n");
            hasError = true;
        }

        if (mode.isEmpty()) {
            errorMessage.append("Mode of instruction field is empty. Please enter a mode of instruction.\n");
            hasError = true;
        } else if (!mode.equalsIgnoreCase("Online") && !mode.equalsIgnoreCase("Offline") && !mode.equalsIgnoreCase("Combined")) {
            errorMessage.append("Invalid mode of instruction. Please enter Online, Offline, or Combined.\n");
            hasError = true;
        }

        if (ageText.isEmpty()) {
            errorMessage.append("Age field is empty. Please enter an age.\n");
            hasError = true;
        } else {
            try {
                int age = Integer.parseInt(ageText);
                if (age <= 0) {
                    errorMessage.append("Invalid age. Please enter a valid number.\n");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Invalid age. Please enter a valid number.\n");
                hasError = true;
            }
        }

        if (feesText.isEmpty()) {
            errorMessage.append("Fees field is empty. Please enter fees.\n");
            hasError = true;
        } else {
            try {
                double fees = Double.parseDouble(feesText);
                if (fees <= 0) {
                    errorMessage.append("Invalid fees. Please enter a valid number.\n");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                errorMessage.append("Invalid fees. Please enter a valid number.\n");
                hasError = true;
            }
        }

        if (email.isEmpty()) {
            errorMessage.append("Email field is empty. Please enter an email.\n");
            hasError = true;
        } else if (!email.matches(".+@.+\\.com")) {
            errorMessage.append("Invalid email format. Please enter a valid email address.\n");
            hasError = true;
        }

        if (hasError) {
            JOptionPane.showMessageDialog(frame, errorMessage.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int confirmResult = JOptionPane.showConfirmDialog(frame, "Confirm information?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmResult == JOptionPane.YES_OPTION) {
                RegisterRecord.addToFile(name, phno, date, batchType, mode, confirmResult, confirmResult, email);
                JOptionPane.showMessageDialog(frame, "Record added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                RecordsUI.main(null);
                frame.dispose();
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

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                RegisterRecordUI ui = new RegisterRecordUI();
                ui.show();
            }
        });
    }
}
