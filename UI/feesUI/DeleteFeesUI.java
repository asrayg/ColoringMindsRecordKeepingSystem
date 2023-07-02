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
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class DeleteFeesUI extends JFrame {
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JButton searchButton;
    private JTextArea outputTextArea;
    private JLabel monthLabel;
    private JTextField monthTextField;
    private JLabel yearLabel;
    private JTextField yearTextField;
    private JButton deleteButton;

    public DeleteFeesUI() {
        checkLockStatusOfSystem("password.txt");

    	setTitle("CMRKS Delete Fee Records");
        setPreferredSize(new Dimension(400, 485));
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

        // Output Panel
        JScrollPane scrollPane = new JScrollPane();
        outputTextArea = new JTextArea();
        scrollPane.setViewportView(outputTextArea);
        scrollPane.setBounds(15, y+50, 370, 210);
        add(scrollPane);
int f = 23;
int z = 15;
        // Bottom Panel
        monthLabel = new JLabel("Month");
        monthLabel.setBounds(f+10, y+z+260, 50, 25);
        monthLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        add(monthLabel);

        monthTextField = new JTextField(5);
        monthTextField.setBounds(f+65, y+z+260, 60, 25);
        add(monthTextField);

        yearLabel = new JLabel("Year");
        yearLabel.setBounds(f+140, y+z+260, 40, 25);
        yearLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        add(yearLabel);

        yearTextField = new JTextField(5);
        yearTextField.setBounds(f+183, y+z+260, 60, 25);
        add(yearTextField);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(f+252, y+z+261, 100, 25);
        add(deleteButton);

        JPanel backPanel = new JPanel();
    	backPanel.setBackground(new Color(173, 239, 209));
    	backPanel.setBounds(0, 422, 400, 325);
    	
    	
    	JButton cancelButton = new JButton("Back");
    	cancelButton.setBounds(320, 428, 75, 30);
    	cancelButton.addActionListener(new ActionListener() {
    	    public void actionPerformed(ActionEvent e) {
    	        FeesUI.main(null);
    	        dispose();
    	    }
    	});
    	add(cancelButton);
    	add(backPanel);

        // Add action listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchButtonClicked();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteButtonClicked();
            }
        });

        

        JPanel highlightPanel = new JPanel();
    	highlightPanel.setBounds(0, 30, 400, 330);
    	highlightPanel.setBackground(new Color(72, 170, 173));
    	add(highlightPanel);
    	
        pack();
        setLocationRelativeTo(null);

    }

    
    private void searchButtonClicked() throws FileNotFoundException, IOException {
        String namesInput = nameTextField.getText();
        String[] names = namesInput.split(", ");
        StringBuilder outputBuilder = new StringBuilder();

	        

        if (!names[0].equals("all")) {
        	for (int i = 0; i < names.length; i++) {
	        	if (!checkName(names[i])) {
	                outputBuilder.append(names[i]).append(" is not in the records. Please register it\n");
	                showErrorDialog("Invalid Name", "Invalid or unknown name: " + names[i]);
	                return;
	            } 
	        	if (!checkValidity(names[i])) {
	        		outputBuilder.append(names[i]).append(" is not valid. Please renew it\n");
	                showErrorDialog("Invalid Name", "Invalid name: " + names[i]);
	                return;
	        	}
	            
	        }
            getNameFeeInfo(names);
        }

        
    }

    private void deleteButtonClicked() {
        String namesInput = nameTextField.getText();
        String[] names = namesInput.split(", ");
        int month = Integer.parseInt(monthTextField.getText());
        int year = Integer.parseInt(yearTextField.getText());

        // Confirmation dialog
        int confirmResult = showConfirmDialog("Confirm Deletion",
                "Are you sure you want to delete fee records?");

        if (confirmResult == JOptionPane.YES_OPTION) {
            if (names[0].equalsIgnoreCase("all")) {
			    deleteAllFeeReports(month, year);
			} else {
			    deleteFeeReport(names, month, year);
			}
			showInfoDialog("Deletion Complete", "Fee records deleted successfully.");
        }
    }
    
 // Helper methods for displaying dialogs
    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private int showConfirmDialog(String title, String message) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION);
    }

    private void showInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
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

    private void getNameFeeInfo(String[] names) {
        StringBuilder feeInfo = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("fees.txt"))) {
            String line;
            feeInfo.append("   Latest Fee Reports");
            feeInfo.append("\n");
            feeInfo.append("     (Month-Year, Fees per hour, Number of Classes, Total)");
            feeInfo.append("\n");
            feeInfo.append("\n");

            int partCount = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                for (int i = 0; i < names.length; i++) {
                    if (parts[0].contains(names[i])) {
                        feeInfo.append("   "+ parts[0]);
                        feeInfo.append("\n");
                        if (parts.length >= 15) {
                            for (int j = parts.length - 12; j < parts.length; j++) {
                                if (partCount % 4 == 0 && j != parts.length - 12) {
                                    feeInfo.append("\n");
                                }
                                if (j == parts.length - 12) {
                                    feeInfo.append("     "+ parts[j]);
                                } else {
                                	if (partCount % 4 == 0) {
                                		feeInfo.append("     "+ parts[j]);
                                	}else {
                                		feeInfo.append(", ").append(parts[j]);
                                	}
                                }
                                partCount++;
                            }
                        } else if (parts.length >= 11) {
                            for (int j = parts.length - 8; j < parts.length; j++) {
                                if (partCount % 4 == 0 && j != parts.length - 8) {
                                    feeInfo.append("\n");
                                }
                                if (j == parts.length - 8) {
                                    feeInfo.append("     "+ parts[j]);
                                } else {
                                	if (partCount % 4 == 0) {
                                		feeInfo.append("     "+ parts[j]);
                                	}else {
                                		feeInfo.append(", ").append(parts[j]);
                                	}
                                }
                                partCount++;
                            }
                        } else if (parts.length >= 7) {
                            for (int j = parts.length - 4; j < parts.length; j++) {
                                if (partCount % 4 == 0 && j != parts.length - 4) {
                                    feeInfo.append("\n");
                                }
                                if (j == parts.length - 4) {
                                    feeInfo.append("     "+ parts[j]);
                                } else {
                                	if (partCount % 4 == 0) {
                                		feeInfo.append("     "+ parts[j]);
                                	}else {
                                		feeInfo.append(", ").append(parts[j]);
                                	}
                                }
                                partCount++;
                            }
                        } else {
                            feeInfo.append(" No Fee Records to Delete");
                        }

                        feeInfo.append("\n");
                        feeInfo.append("\n");
                    }
                }
            }
            feeInfo.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        outputTextArea.setFont(new Font("Menlo", Font.BOLD, 10));
        outputTextArea.setEditable(false);
        outputTextArea.setText(feeInfo.toString());
    }


    
	private static void deleteAllFeeReports(int month, int year) {
    	File inputFile = new File("fees.txt");
        File tempFile = new File("fees_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    bw.write(line);
                    bw.newLine();
                    isFirstLine = false;
                } else {
                    String[] parts = line.split(", ");
                    String studentName = parts[0];
                    String phoneNumber = parts[1];
                    String totalBalance = parts[2];
                    
                    

                    
                    if(!checkValidity(studentName)) {
                    	bw.write(line);
                        bw.newLine();
                    	continue;
                    }

                    List<String> feeReports = new ArrayList<>();
                    for (int i = 3; i < parts.length; i++) {
                        feeReports.add(parts[i]);
                    }

                    List<String> updatedFeeReports = new ArrayList<>();
                    int count = 0;
                    int elementNumber = -1; 

                    for (String feeReport : feeReports) {
                        String[] reportParts = feeReport.split(", ");
                        String reportMonthYear = reportParts[0].substring(1);
                        if (!reportMonthYear.equals(String.format("%02d-%04d", month, year))) {
                            updatedFeeReports.add(feeReport);
                        } else {
                            elementNumber = count;
                        }
                        count++;
                    }

                    double subtractingAmount = Double.parseDouble(updatedFeeReports.get(elementNumber + 2).replace(")", ""));
                    double newTotalBalance = Double.parseDouble(totalBalance) - subtractingAmount;

                    if (elementNumber != -1 && elementNumber + 2 < updatedFeeReports.size()) {
                        updatedFeeReports.remove(elementNumber + 2);
                        updatedFeeReports.remove(elementNumber + 1);
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append(studentName).append(", ").append(phoneNumber).append(", ").append(newTotalBalance);

                    for (int i = 0; i < updatedFeeReports.size(); i++) {
                        if (i != elementNumber) {
                            sb.append(", ").append(updatedFeeReports.get(i));
                        }
                    }
                    bw.write(sb.toString());
                    bw.newLine();
                }
            }

            bw.flush();
            bw.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    private static void deleteFeeReport(String[] names, int month, int year) throws ArrayIndexOutOfBoundsException{
        File inputFile = new File("fees.txt");
        File tempFile = new File("fees_temp.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    bw.write(line);
                    bw.newLine();
                    isFirstLine = false;
                } else {
                	
                    String[] parts = line.split(", ");
                    String studentName = parts[0];
                    String totalBalance = parts[2];
                    String phoneNumber = parts[1];
                    
                    boolean hasDate = false;
                    for(int i = 0; i<names.length; i++) {
                    	if(studentName.equals(names[i])) {
                    		hasDate = true;
                    	}
                    }

                    if(!line.contains(String.format("%02d-%04d", month, year))&& hasDate) {
                    	JOptionPane.showMessageDialog(null, studentName + " did not have a fee report for " + String.format("%02d-%04d", month, year));
                	}
                    
                    boolean shouldDelete = false;
                    for (String name : names) {
                        if (studentName.equals(name.trim())) {
                            shouldDelete = true;
                            break;
                        }
                    }

                    if(!checkValidity(studentName)) {
                    	JOptionPane.showMessageDialog(null, studentName + " is not valid. Please renew " + studentName);
                    	bw.write(line);
                        bw.newLine();
                    	continue;
                    }
                    if (!shouldDelete) {
                        bw.write(line);
                        bw.newLine();
                        continue;
                    }

                    List<String> feeReports = new ArrayList<>();
                    for (int i = 3; i < parts.length; i++) {
                        feeReports.add(parts[i]);
                    }

                    List<String> updatedFeeReports = new ArrayList<>();
                    int count = 0;
                    int elementNumber = -1;

                    for (String feeReport : feeReports) {
                        String[] reportParts = feeReport.split(", ");
                        if (reportParts.length >= 1) {  
                            String reportMonthYear = reportParts[0].substring(1);
                            if (!reportMonthYear.equals(String.format("%02d-%04d", month, year))) {
                                updatedFeeReports.add(feeReport);
                            } else {
                                elementNumber = count;
                            }
                            count++;
                        }
                    }


                    double subtractingAmount = Double.parseDouble(updatedFeeReports.get(elementNumber + 2).replace(")", ""));
                    double newTotalBalance = Double.parseDouble(totalBalance) - subtractingAmount;

                    if(newTotalBalance<0) {
                    	newTotalBalance = 0.00;
                    }
                    if (elementNumber != -1 && elementNumber + 2 < updatedFeeReports.size()) {
                        updatedFeeReports.remove(elementNumber + 2);
                        updatedFeeReports.remove(elementNumber + 1);
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append(studentName).append(", ").append(phoneNumber).append(", ").append(newTotalBalance);

                    for (int i = 0; i < updatedFeeReports.size(); i++) {
                        if (i != elementNumber) {
                            sb.append(", ").append(updatedFeeReports.get(i));
                        }
                    }
                    bw.write(sb.toString());
                    bw.newLine();
                }
            }

            bw.flush();
            bw.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
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
    
    

	private static boolean checkName(String name) {
		if(name.equals("all")) {
			return true;
		}
		
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                DeleteFeesUI deleteFeesUI = new DeleteFeesUI();
                deleteFeesUI.pack();
                deleteFeesUI.setVisible(true);
            }
        });
    }
}
                		
