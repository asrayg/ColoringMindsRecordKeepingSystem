package paymentUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class NewPaymentUI extends JFrame {

    private JTextField nameField;
    private JTextField dateField;
    private JTextField amountField;
    private JTextField paymentTypeField;

    public NewPaymentUI() {
        checkLockStatusOfSystem("password.txt");

    	initializeUI();
    }

    private void initializeUI() {
    	setTitle("CMRKS New Payment Record");
    	setSize(400, 485);
        getContentPane().setBackground(new Color(37, 150, 190)); 

    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    	JPanel panel = new JPanel();
    	panel.setLayout(null); 
int y = 61;
int z = 120;
    	JLabel nameLabel = new JLabel("Name");
    	nameLabel.setBounds(y+10, z+10, 100, 20); 
    	nameLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	panel.add(nameLabel);

    	JLabel dateLabel = new JLabel("Date");
    	dateLabel.setBounds(y+10, z+40, 150, 20); 
    	dateLabel.setFont(new Font("Verdana", Font.BOLD, 12));
    	panel.add(dateLabel);

    	JLabel amountLabel = new JLabel("Amount");
    	amountLabel.setBounds(y+10, z+70, 100, 20); 
    	amountLabel.setFont(new Font("Verdana", Font.BOLD, 12));

    	panel.add(amountLabel);

    	JLabel paymentTypeLabel = new JLabel("Type");
    	paymentTypeLabel.setBounds(y+10, z+100, 100, 20); 
    	paymentTypeLabel.setFont(new Font("Verdana", Font.BOLD, 12));

    	panel.add(paymentTypeLabel);

    	nameField = new JTextField();
    	nameField.setBounds(y+90, z+10, 180, 20); 
    	panel.add(nameField);

    	dateField = new JTextField();
    	dateField.setBounds(y+90, z+40, 180, 20); 
    	panel.add(dateField);

    	amountField = new JTextField();
    	amountField.setBounds(y+90, z+70, 180, 20); 
    	panel.add(amountField);

    	paymentTypeField = new JTextField();
    	paymentTypeField.setBounds(y+90, z+100, 180, 20); 
    	panel.add(paymentTypeField);

    	
    	
    	setLayout(new BorderLayout());
    	add(panel, BorderLayout.CENTER);


        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(254, 255, 80, 25); 
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleSubmit();
            }
        });
        
        JButton cancelButton = new JButton("Back");
        cancelButton.setBounds(320, 428, 75, 30);
        cancelButton.setBackground(new Color(173, 239, 209)); 
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PaymentUI.main(null);
                dispose();
            }
        });
        
        
        panel.add(cancelButton);

        panel.add(submitButton);

        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(15, 40, 370, 325); 
        highlightPanel.setBackground(new Color(72, 170, 173)); 
        highlightPanel.setLayout(null);
        panel.add(highlightPanel);
        JPanel highlightPanel1 = new JPanel();
        highlightPanel1.setBounds(0, 422, 400, 325); 
        highlightPanel1.setBackground(new Color(173, 239, 209)); 
        highlightPanel1.setLayout(null);
        panel.add(highlightPanel1);
        panel.setBackground(new Color(37, 150, 190));
        add(panel);
        
        
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

    private void handleSubmit() {
        String name = nameField.getText();
        String date = dateField.getText();
        String amountText = amountField.getText();
        String paymentType = paymentTypeField.getText();

        if (name.isEmpty() || date.isEmpty() || amountText.isEmpty() || paymentType.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill in all the fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(null, "Invalid date format. Please enter the date in dd-MM-yyyy format.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid fee amount input. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (checkName(name) && checkValidity(name)) {
                int confirmResult = JOptionPane.showConfirmDialog(null, "Confirm payment?", "Payment Form", JOptionPane.YES_NO_OPTION);
                if (confirmResult == JOptionPane.YES_OPTION) {
                    generatePaymentReport(name, amount, date, paymentType);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid name. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkName(String name) throws IOException {
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
        return false;
    }

    private boolean checkValidity(String option) throws IOException {
        @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader("records.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(", ");
            if (parts.length > 0 && parts[0].trim().equals(option)) {
                if (parts[8].equals("y")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValidDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    private void generatePaymentReport(String name, double amount, String date, String paymentType) throws IOException {
    	String paymentFilename = "payment.txt";
    	String feesFilename = "fees.txt";
    	String tempFilename = "payment_temp.txt";
    	try (BufferedReader brPayment = new BufferedReader(new FileReader(paymentFilename));
    	     BufferedWriter bwPayment = new BufferedWriter(new FileWriter(tempFilename));
    	     BufferedReader brFees = new BufferedReader(new FileReader(feesFilename));
    	     BufferedWriter bwFees = new BufferedWriter(new FileWriter("fees_temp.txt"))) {


    	    String lineFees;
    	    boolean isFirstLineFees = true;
    	    while ((lineFees = brFees.readLine()) != null) {
    	        if (isFirstLineFees) {
    	            isFirstLineFees = false;
    	            bwFees.write(lineFees);
    	            bwFees.newLine();
    	            continue;
    	        }
    	        String[] partsFees = lineFees.split(", ");
    	        ArrayList<String> feeLine = new ArrayList<>();

    	        for (int i = 0; i < partsFees.length; i++) {
    	            feeLine.add(partsFees[i]);
    	        }

    	        if (partsFees[0].equals(name)) {
    	            double balance = Double.parseDouble(feeLine.get(2));
    	            double temp1 = balance;
    	            balance -= amount;
    	            if (balance < 0) {
    	                double temp = amount - temp1;
    	                JOptionPane.showMessageDialog(null, "Error: Amount paid exceeds the total balance. Exceeds by: " + temp, "Error", JOptionPane.ERROR_MESSAGE);
    	                return;
    	            }
    	            String stringBalance = String.format("%.2f", balance);

    	            feeLine.set(2, stringBalance);
    	            for (int i = 0; i < partsFees.length; i++) {
    	                partsFees[i] = feeLine.get(i);
    	            }
    	            String newLine = Arrays.toString(partsFees).replace("[", "").replace("]", "");
    	            bwFees.write(newLine);
    	            bwFees.newLine();
    	        } else {
    	            String newLine = Arrays.toString(partsFees).replace("[", "").replace("]", "");
    	            bwFees.write(newLine);
    	            bwFees.newLine();
    	        }
    	    }

    	    String linePayment;
    	    boolean isFirstLinePayment = true;
    	    boolean found = false;
    	    while ((linePayment = brPayment.readLine()) != null) {
    	        if (isFirstLinePayment) {
    	            isFirstLinePayment = false;
    	            bwPayment.write(linePayment);
    	            bwPayment.newLine();
    	            continue;
    	        }

    	        String[] partsPayment = linePayment.split(", ");
    	        String studentName = partsPayment[0];
    	        if (studentName.equals(name)) {
    	            linePayment += ", (" + amount + ", " + date + ", " + paymentType + ")";
    	            found = true;
    	        }

    	        bwPayment.write(linePayment);
    	        bwPayment.newLine();
    	    }

    	    if (!found) {
    	        JOptionPane.showMessageDialog(null, "Name not found.", "Error", JOptionPane.ERROR_MESSAGE);
    	    } else {
    	        JOptionPane.showMessageDialog(null, "Payment recorded successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
    	    }

    	    

    	} catch (IOException e) {
    	    e.printStackTrace();
    	}

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new NewPaymentUI().setVisible(true);
            }
        });
    }
}
