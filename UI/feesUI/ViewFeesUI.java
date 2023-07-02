package feesUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Year;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import fees.FeesMain;

@SuppressWarnings("serial")
public class ViewFeesUI extends JFrame {
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel yearLabel;
    private JTextField yearTextField;
    private JButton searchButton;
    private static JTextArea outputTextArea;

    public ViewFeesUI() {
        checkLockStatusOfSystem("password.txt");

        setTitle("CMRKS View Student Fees");
        setPreferredSize(new Dimension(400, 485));
        getContentPane().setBackground(new Color(37, 150, 190)); 

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Name Panel
        nameLabel = new JLabel("Names");
        nameLabel.setBounds(13, 10, 200, 25);
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        add(nameLabel);

        nameTextField = new JTextField();
        nameTextField.setBounds(67, 11, 322, 25);
        add(nameTextField);
        int x = 55;
        int y = 25;
        // Year Panel
        yearLabel = new JLabel("Year");
        yearLabel.setBounds(x+21, 70-y, 50, 25);
        yearLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        add(yearLabel);

        yearTextField = new JTextField();
        yearTextField.setBounds(x+65, 70-y, 100, 25);
        add(yearTextField);

        // Search Button
        searchButton = new JButton("Search");
        searchButton.setBounds(x+175, 71-y, 100, 25);
        add(searchButton);

        // Output Panel
        JScrollPane scrollPane = new JScrollPane();
        outputTextArea = new JTextArea();
        scrollPane.setViewportView(outputTextArea);
        scrollPane.setBounds(0, 105-y, 400, 342);
        add(scrollPane);

        // Add action listeners
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchButtonClicked();
            }
        });
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
        pack();
        setLocationRelativeTo(null); 
    }

    private void searchButtonClicked() {
        String namesInput = nameTextField.getText();
        outputTextArea.setText(""); 
        if (namesInput.isEmpty()) {
            outputTextArea.append("Please enter a name.");
            Font lucidaFont = new Font("Menlo", Font.BOLD, 11);
        	outputTextArea.setFont(lucidaFont);
            return;
        }
        String[] names = namesInput.split(", ");
        String year = yearTextField.getText().trim();

        outputTextArea.setText(""); 

        
        
        if (year.isEmpty()) {
            outputTextArea.append("Please enter a year.");
            Font lucidaFont = new Font("Menlo", Font.BOLD, 11);
        	outputTextArea.setFont(lucidaFont);
            return;
        }

        int currentYear = Year.now().getValue();

        try {
            int inputYear = Integer.parseInt(year);

            if (inputYear < 1000 || inputYear > currentYear) {
                outputTextArea.append("Please enter a valid year between 1000 and " + currentYear + ".");
                Font lucidaFont = new Font("Menlo", Font.BOLD, 11);
            	outputTextArea.setFont(lucidaFont);
                return;
            }

            for (String name : names) {
                viewFeeDetails(name.trim(), year);
            }
        } catch (NumberFormatException e) {
            outputTextArea.append("Invalid year format. Please enter a valid year.");
            Font lucidaFont = new Font("Menlo", Font.BOLD, 11);
        	outputTextArea.setFont(lucidaFont);
        }
        
    }


    private static void viewFeeDetails(String name, String year) {
        if(!checkName(name)) {
            outputTextArea.append(name + " does not exist in the records.");
        }
    	File inputFile = new File("fees.txt");
        File batchFile = new File("records.txt");
        boolean checker = false;
        
        
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedReader dr = new BufferedReader(new FileReader(batchFile))) {
            String line;
            String line1;
            boolean firstLine = true;
            String batchType = null;
            
            while ((line1 = dr.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line1.split(", ");

                if (parts[0].equals(name)) {
                    batchType = parts[4];
                    if (!checkValidity(parts[0])) {
                        outputTextArea.append(parts[0] + " is not valid. Please renew " + parts[0] + "\n");
                        checker = true;
                    }
                    break;
                }
            }

            firstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.replace("(", "").replace(")", "").split(", ");

                if (parts.length < 6) {
                    outputTextArea.append("Invalid format in the input file. Please ensure each line follows the format: Name, Batch Type, Fees Per Hour, Month, Classes, Total Fees\n");
                    String[] args = null;
                    FeesMain.main(args);
                }

                String studentName = parts[0];
                String totalBalance = parts[2];

                if (studentName.equals(name) && !checker) {
                    outputTextArea.append(studentName + ": Total Balance: " + totalBalance + " || Batch Type: " + batchType + "\n");
                    outputTextArea.append("\n");
                    outputTextArea.append("Month-Year     Fees       Per Hour:\n");

                    for (int i = 3; i < parts.length; i = i + 4) {
                        if (parts[i].substring(3, 7).equals(year)) {
                            outputTextArea.append(String.format("%-14s%-12s%-12s\n", parts[i], " "+ parts[i + 3], parts[i + 1]));
                        }
                    }
                }


                Font lucidaFont = new Font("Menlo", Font.BOLD, 11);
    	    	outputTextArea.setFont(lucidaFont);



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputTextArea.append("\n");
        outputTextArea.append("\n");
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ViewFeesUI frame = new ViewFeesUI();
                frame.setVisible(true);
            }
        });
    }
}
