package recordsUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ReadRecordsUI extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;


    public ReadRecordsUI() {
        checkLockStatusOfSystem("password.txt");

    	setTitle("CMRKS View Student Information");
        setSize(400, 485);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(37, 150, 190));

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        inputPanel.setBackground(new Color(37, 150, 190));

        JLabel inputLabel = new JLabel("Enter students' names or 'all'");
        inputLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        inputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        inputField = new JTextField(20);
        inputField.setPreferredSize(new Dimension(200, 25));

        JButton searchButton = new JButton("Search");

        inputPanel.add(inputField);
        inputPanel.add(searchButton);

        JPanel outputPanel = new JPanel(new BorderLayout());
        outputArea = new JTextArea();
        outputArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setPreferredSize(new Dimension(380, 400));

        outputPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(inputLabel);
        mainPanel.add(inputPanel);
        mainPanel.add(outputPanel);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecordsUI.main(new String[0]); 
                dispose(); 
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(173, 239, 209));
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);


        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputField.getText().trim();
                searchRecords(input);
            }
        });
    }

    private void searchRecords(String input) {
    	 outputArea.setText(""); 
        File file = new File("records.txt");
        List<String> validNames = new ArrayList<>();

        String[] selectedNames = input.split(",");
        for (String name : selectedNames) {
            name = name.trim();
            if (name.equalsIgnoreCase("q")) {
                outputArea.append("Exiting the program...");
                RecordsUI.main(selectedNames);
            }

            if (checkName(name) && checkValidity(name)) {
                validNames.add(name);
            } else if (!checkValidity(name)) {
            	outputArea.append("\n");
                outputArea.append("   " +name + " is not valid. \n");
                outputArea.append("   Please renew/register " + name + " to view. \n");
                outputArea.append("\n");
                outputArea.setFont(new Font("Menlo", Font.BOLD, outputArea.getFont().getSize()));
            }
        }

        if (validNames.isEmpty()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            if (validNames.contains("all")) {
                boolean isFirstLine = true;
                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue;
                    }

                    String[] parts = line.split(", ");
                    if (parts.length > 0 && !parts[parts.length - 1].equals("n")) {
                        printRecord(parts);
                    }
                }
            } else {
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(", ");
                    if (parts.length > 0 && validNames.contains(parts[0].trim()) && !parts[parts.length - 1].equals("n")) {
                        printRecord(parts);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private void printRecord(String[] parts) {
    	outputArea.append("\n");
        outputArea.append("   Name           : " + parts[0] + "\n");
        outputArea.append("   Phone Number   : " + parts[1] + "\n");
        outputArea.append("   Start Date     : " + parts[2] + "\n");
        outputArea.append("   Age            : " + parts[3] + "\n");
        outputArea.append("   Batch Type     : " + parts[4] + "\n");
        outputArea.append("   Mode           : " + parts[5] + "\n");
        outputArea.append("   Fees Per Hour  : " + parts[6] + "\n");
        outputArea.append("   Email          : " + parts[7] + "\n");
        outputArea.append("   Validity       : y\n");
        outputArea.append("\n");


    	// Apply bold font to the labels
    	Font lucidaFont = new Font("Menlo", Font.BOLD, outputArea.getFont().getSize());
    	outputArea.setFont(lucidaFont);

    }

    private boolean checkName(String name) {
        if (name.equals("all")) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkValidity(String option) {
        if (option.equals("all")) {
            return true;
        }

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ReadRecordsUI ui = new ReadRecordsUI();
                ui.setVisible(true);
            }
        });
    }
}

