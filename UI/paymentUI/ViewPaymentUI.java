package paymentUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("serial")
public class ViewPaymentUI extends JFrame {
    private JTextField nameField;
    private JTextField yearField;
    private JTextArea outputArea;

    public ViewPaymentUI() {
        checkLockStatusOfSystem("password.txt");

        setTitle("CMRKS View Student Payments");
        setSize(400, 485);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        add(panel);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(13, 10, 200, 25);
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        panel.add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(67, 11, 322, 25);
        panel.add(nameField);

        JLabel yearLabel = new JLabel("Year");
        yearLabel.setBounds(55+21, 70-25, 50, 25);
        yearLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        panel.add(yearLabel);

        yearField = new JTextField();
        yearField.setBounds(55+65, 70-25, 100, 25);
        panel.add(yearField);

        JButton viewButton = new JButton("View");
        viewButton.setBounds(55+175, 71-25, 100, 25);
        panel.add(viewButton);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBounds(0, 105-25, 400, 342);
        panel.add(scrollPane);

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText().trim();
                String year = yearField.getText().trim();
                
                if (!name.isEmpty() && !year.isEmpty()) {
                    if (year.matches("\\d{4}")) {
                        viewPaymentDetails(name, year);
                    } else {
                        outputArea.setText("Please enter a valid 4-digit year.");
                    }
                } else {
                    outputArea.setText("Please fill in the empty field/s.");
                }
            }
        });
        
        JButton cancelButton = new JButton("Back");
    	cancelButton.setBounds(320, 428, 75, 30);
    	cancelButton.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	        PaymentUI.main(null);
    	        dispose();
    	    }
    	});
    	panel.add(cancelButton);
    	
    	JPanel highlightPanel = new JPanel();
    	highlightPanel.setBounds(0, 422, 400, 325);
    	highlightPanel.setBackground(new Color(173, 239, 209));
    	panel.add(highlightPanel);

    	panel.setBackground(new Color(37, 150, 190));
    }

    private void viewPaymentDetails(String name, String year) {
        outputArea.setText("");

        try (BufferedReader br = new BufferedReader(new FileReader("payment.txt"))) {
            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] parts = line.replace("(", "").replace(")", "").split(", ");
                String studentName = parts[0];

                if (studentName.equals(name)) {
                    found = true;
                    outputArea.append(String.format("%-20s%-15s%-15s\n", "Date", "Fee Paid", "Payment Type"));
                    outputArea.append("\n");
                    int count = 0;
                    for (int i = 3; i < parts.length; i = i + 3) {
                        if (parts[i].substring(6, 10).equals(year)) {
                            outputArea.append(String.format("%-20s%-15s%-15s\n", parts[i], parts[i - 1], parts[i + 1]));
                            count++;
                        }
                    }

                    if (count == 0) {
                        outputArea.append(name + " does not have any payment records in " + year + "\n");
                    }
                }
            }

            if (!found) {
                outputArea.append(name + " is not registered in the system\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputArea.setFont(new Font("Menlo", Font.BOLD, 12));

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
            public void run() {
                ViewPaymentUI viewPaymentUI = new ViewPaymentUI();
                viewPaymentUI.setVisible(true);
            }
        });
    }
}
