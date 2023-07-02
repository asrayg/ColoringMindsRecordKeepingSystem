package paymentUI;

import java.awt.Color;
import java.awt.Font;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class DeletePaymentUI extends JFrame {
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JButton searchButton;
    private JTextArea outputArea;
    private JLabel dateLabel;
    private JTextField dateTextField;
    private JButton deleteButton;

    public DeletePaymentUI() {
        checkLockStatusOfSystem("password.txt");

        setTitle("CMRKS Delete Payment Records");
        setSize(400, 485);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(new Color(37, 150, 190)); 
        
        int x = 12;
        int y = 35;
        nameLabel = new JLabel("Names");
        nameLabel.setBounds(x+12, y+10, 50, 25);
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        add(nameLabel);

        nameTextField = new JTextField(20);
        nameTextField.setBounds(x+65, y+10, 200, 25);
        add(nameTextField);
        
        searchButton = new JButton("Search");
        searchButton.setBounds(x+270, y+11, 100, 25);
        add(searchButton);
        
        
        JScrollPane scrollPane = new JScrollPane();
        JTextArea outputTextArea = new JTextArea();
        scrollPane.setViewportView(outputTextArea);
        scrollPane.setBounds(15, y+50, 370, 210);
        add(scrollPane);
        
        dateLabel = new JLabel("Date");
        dateLabel.setBounds(23+10, 50+260, 50, 25);
        dateLabel.setFont(new Font("Verdana", Font.BOLD, 12));

        dateTextField = new JTextField(15);
        dateTextField.setBounds(x+65, 50+260, 200, 25);
        deleteButton = new JButton("Delete");
        deleteButton.setBounds(x+270, 50+261, 100, 25);

        
        JPanel backPanel = new JPanel();
    	backPanel.setBackground(new Color(173, 239, 209));
    	backPanel.setBounds(0, 422, 400, 325);
    	
    	
    	JButton cancelButton = new JButton("Back");
    	cancelButton.setBounds(320, 428, 75, 30);
    	cancelButton.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	        PaymentUI.main(null);
    	        dispose();
    	    }
    	});
    	add(cancelButton);
    	add(backPanel);
    	
    	
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentName = nameTextField.getText().trim();

                if (studentName.equalsIgnoreCase("q")) {
                    JOptionPane.showMessageDialog(DeletePaymentUI.this, "Exiting the program...", "Message", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                if (!checkName(studentName)) {
                    JOptionPane.showMessageDialog(DeletePaymentUI.this, studentName + " is not valid. Please renew/register " + studentName + " to view.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                viewPaymentDetails(studentName);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String studentName = nameTextField.getText().trim();
                String date = dateTextField.getText().trim();

                if (!checkName(studentName)) {
                    JOptionPane.showMessageDialog(DeletePaymentUI.this, studentName + " is not valid. Please register " + studentName + " to delete payment.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
					if (!checkValidity(studentName)) {
					    JOptionPane.showMessageDialog(DeletePaymentUI.this, studentName + " is not valid. Please renew " + studentName + " to delete payment.", "Error", JOptionPane.ERROR_MESSAGE);
					    return;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				} 

                if (date.isEmpty()) {
                    JOptionPane.showMessageDialog(DeletePaymentUI.this, "Please enter a date to delete the payment report.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                dateFormat.setLenient(false);

                try {
                    dateFormat.parse(date);
                } catch (ParseException e1) {
                    JOptionPane.showMessageDialog(DeletePaymentUI.this, "Invalid date format. Please enter the date in the dd-MM-yyyy format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }


                int confirmOption = JOptionPane.showConfirmDialog(DeletePaymentUI.this, "Are you sure you want to delete the payment report?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmOption == JOptionPane.YES_OPTION) {
                    deletePaymentReport(studentName, date);
                }
            }
        });
        
        

        add(nameLabel);
        add(nameTextField);
        add(searchButton);
        add(scrollPane);
        add(dateLabel);
        add(dateTextField);
        add(deleteButton);
        JPanel highlightPanel = new JPanel();
    	highlightPanel.setBounds(0, 30, 400, 330);
    	highlightPanel.setBackground(new Color(72, 170, 173));
    	add(highlightPanel);
        setVisible(true);
        
    }

    private void viewPaymentDetails(String name) {
    	outputArea.setText("");
    	String year = "";

    	Calendar calendar = Calendar.getInstance();
    	int currentYear = calendar.get(Calendar.YEAR);

    	year = String.valueOf(currentYear);

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

    
    private void deletePaymentReport(String name, String date) {
        String paymentFilename = "payment.txt";
        String tempFilename = "payment_temp.txt";

        String feesFilename = "fees.txt";
		try (BufferedReader brPayment = new BufferedReader(new FileReader(paymentFilename));
				BufferedWriter bwPayment = new BufferedWriter(new FileWriter(tempFilename));
				BufferedReader brFees = new BufferedReader(new FileReader(feesFilename ));
				BufferedWriter bwFees = new BufferedWriter(new FileWriter("fees_temp.txt"))) {

			String linePayment;
			boolean isFirstLinePayment = true;
			boolean found = false;
			double amount = 0;
			while ((linePayment = brPayment.readLine()) != null) {
				if (isFirstLinePayment) {
					isFirstLinePayment = false;
					bwPayment.write(linePayment);
					bwPayment.newLine();
					continue;
				}

				String[] partsFees = linePayment.split(", ");
				ArrayList<String> feeLine = new ArrayList<>();

				for (int i = 0; i < partsFees.length; i++) {
					feeLine.add(partsFees[i]);
				}

				String lineName = partsFees[0];
				String stramount = null;
				if (lineName.equals(name)) {
					
					for (int i = 0; i < feeLine.size(); i++) {
						if (date.equals(feeLine.get(i).replace(")", ""))) {
							
							feeLine.remove(i);
							feeLine.remove(i);
							stramount = feeLine.get(i - 1).replace("(", "");
							amount = Double.parseDouble(stramount);
							feeLine.remove(i - 1);
							found = true;
							
						}
					}
					String[] newLinePayment = new String[partsFees.length - 2];

					for (int i = 0; i < newLinePayment.length-1; i++) {
						newLinePayment[i] = feeLine.get(i);
					}
					String newLine = Arrays.toString(newLinePayment).replace("[", "").replace("]", "").replace(", null", "");
					bwPayment.write(newLine);
					bwPayment.newLine();

				} else {
					bwPayment.write(linePayment);
					bwPayment.newLine();

				}

			}


            if (!found) {
                JOptionPane.showMessageDialog(DeletePaymentUI.this, "Payment report not found for " + date, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                updateFeeReport(name, date, amount);
            }

            File paymentFile = new File(paymentFilename);
            File tempPaymentFile = new File(tempFilename);
            if (paymentFile.delete() && tempPaymentFile.renameTo(paymentFile)) {
                System.out.println("Payment file updated successfully.");
            } else {
                System.out.println("Failed to update payment file.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateFeeReport(String name, String date, double amount) {
        String feesFilename = "fees.txt";
        String tempFilename = "fees_temp.txt";
        try (BufferedReader brFees = new BufferedReader(new FileReader(feesFilename));
             BufferedWriter bwFees = new BufferedWriter(new FileWriter(tempFilename))) {

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

                if (partsFees[0].equals(name)) {
                    double balance = Double.parseDouble(partsFees[2]);
                    balance += amount;

                    partsFees[2] = String.format("%.2f", balance);
                }

                String newLine = String.join(", ", partsFees);
                bwFees.write(newLine);
                bwFees.newLine();
            }

            File feesFile = new File(feesFilename);
            File tempFeesFile = new File(tempFilename);
            if (feesFile.delete() && tempFeesFile.renameTo(feesFile)) {
                System.out.println("Fees file updated successfully.");
            } else {
                System.out.println("Failed to update fees file.");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private boolean checkValidity(String option) throws FileNotFoundException, IOException {
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
                new DeletePaymentUI();
            }
        });
    }
}
