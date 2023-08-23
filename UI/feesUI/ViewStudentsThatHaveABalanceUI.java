package feesUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@SuppressWarnings("serial")
public class ViewStudentsThatHaveABalanceUI extends JFrame {

    private JTextArea outputArea;

    public ViewStudentsThatHaveABalanceUI() {
        checkLockStatusOfSystem("password.txt");

        setTitle("CMRKS Students With Balances");
        setSize(400, 485);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Students With Balances");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(37, 150, 190));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FeesUI.main(new String[0]);
                dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(173, 239, 209));
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        displayStudentsWithABalance();
    }

    private void displayStudentsWithABalance() {
        File inputFile = new File("fees.txt");
        int counter = 0;
        StringBuilder sb = new StringBuilder();
        
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            sb.append("\n");
            sb.append(String.format("%-20s%-20s%-20s\n", "Student Name", "Ph. No.", "Total Balance"));
            sb.append("\n");

            boolean firstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.replace("(", "").replace(")", "").split(", ");
                
                String studentName = parts[0];
                String phno = parts[1];
                String totalBalance = parts[2];
                
                if (Double.valueOf(totalBalance) > 0) {
                    sb.append(String.format("%-20s%-20s%-20s\n", studentName, phno, totalBalance));
                    counter++;
                }
            }
            
            if (counter == 0) {
                sb.append("All balances have been cleared.\n");
            }
            outputArea.setFont(new Font("Menlo", Font.BOLD, 12));
            outputArea.setText(sb.toString());
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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ViewStudentsThatHaveABalanceUI().setVisible(true);
            }
        });
    }
}
