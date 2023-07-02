package mainUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import attendanceUI.AttendanceUI;
import emailUI.EmailUI;
import feesUI.FeesUI;
import paymentUI.PaymentUI;
import recordsUI.RecordsUI;

public class CommandUI {
    private JFrame frame;
    private JButton recordsButton;
    private JButton attendanceButton;
    private JButton feesButton;
    private JButton paymentButton;
    private JButton emailButton;
    private JButton exitButton;

    public CommandUI() {
        checkLockStatusOfSystem("password.txt");

        initialize();
    }

    private void initialize() {
        frame = new JFrame("Coloring Minds Record Keeping System (CMRKS)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 485);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(37, 150, 190));

        ImageIcon imageIcon = new ImageIcon("c_minds.png");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(50, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        panel.add(imageLabel);

        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(0, 105, 400, 30); 
        highlightPanel.setBackground(new Color(0, 32, 63)); 
        highlightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Main Menu");
        titleLabel.setBounds(0, 0, 400, 20); 
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setForeground(new Color(173, 239, 209)); 
        highlightPanel.add(titleLabel); 
        panel.add(highlightPanel);
        
        recordsButton = new JButton("Records");
        recordsButton.setBounds(50, 150, 300, 40);
        recordsButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        recordsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RecordsUI.main(null);
                frame.dispose();
            }
        });
        panel.add(recordsButton);

        attendanceButton = new JButton("Attendance");
        attendanceButton.setBounds(50, 200, 300, 40);
        attendanceButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        attendanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AttendanceUI.main(null);
				frame.dispose();
            }
        });
        panel.add(attendanceButton);

        feesButton = new JButton("Fees");
        feesButton.setBounds(50, 250, 300, 40);
        feesButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        feesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                FeesUI.main(null);
				frame.dispose();
            }
        });
        panel.add(feesButton);

        paymentButton = new JButton("Payment");
        paymentButton.setBounds(50, 300, 300, 40);
        paymentButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        paymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PaymentUI.main(null);
				frame.dispose();
            }
        });
        panel.add(paymentButton);

        emailButton = new JButton("Email");
        emailButton.setBounds(50, 350, 300, 40);
        emailButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        emailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EmailUI.main(null);
				frame.dispose();
            }
        });
        panel.add(emailButton);

        exitButton = new JButton("Exit");
        exitButton.setBounds(150, 410, 100, 30);
        exitButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(exitButton);

        Dimension frameSize = frame.getSize();
        Dimension panelSize = panel.getPreferredSize();
        panel.setBounds(
            (frameSize.width - panelSize.width) / 2,
            (frameSize.height - panelSize.height) / 2 - 20, 
            panelSize.width,
            panelSize.height
        );

        frame.getContentPane().add(panel);
    }

    public void show() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
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
        CommandUI ui = new CommandUI();
        ui.show();
    }
}