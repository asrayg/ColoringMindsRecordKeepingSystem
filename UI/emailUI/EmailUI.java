package emailUI;

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

import mainUI.CommandUI;

public class EmailUI {
    private JFrame frame;
    private JButton generateNormalEmailButton;
    private JButton sendSpecificEmailButton;
    private JButton sendBalanceEmailButton;
    private JButton backButton;
    private JButton specificDatesButton;
    
    public EmailUI() {
        checkLockStatusOfSystem("password.txt");

    	initialize();
    }

    private void initialize() {
        frame = new JFrame("CMRKS Email Menu");
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

        JLabel titleLabel = new JLabel("Email Menu");
        titleLabel.setBounds(0, 0, 400, 20); 
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setForeground(new Color(173, 239, 209)); 
        highlightPanel.add(titleLabel); 
        panel.add(highlightPanel);


        generateNormalEmailButton = new JButton("Generate");
        generateNormalEmailButton.setBounds(50, 150, 300, 40);
        generateNormalEmailButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        generateNormalEmailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenerateEmailUI.main(null);
                frame.dispose();
            }
        });
        panel.add(generateNormalEmailButton);

        sendSpecificEmailButton = new JButton("Specific Student");
        sendSpecificEmailButton.setBounds(50, 200, 300, 40);
        sendSpecificEmailButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        sendSpecificEmailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenerateSpecificEmailUI.main(null);
                frame.dispose();

            }
        });
        panel.add(sendSpecificEmailButton);

        sendBalanceEmailButton = new JButton("Balance");
        sendBalanceEmailButton.setBounds(50, 250, 300, 40);
        sendBalanceEmailButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        sendBalanceEmailButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenerateBalanceEmailUI.main(null);
                frame.dispose();

            }
        });
        panel.add(sendBalanceEmailButton);
        
        specificDatesButton = new JButton("Specific Dates");
        specificDatesButton.setBounds(50, 300, 300, 40);
        specificDatesButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        specificDatesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	GenerateSpecificDateEmailUI.main(null);
                frame.dispose();
            }
        });
        panel.add(specificDatesButton);

        backButton = new JButton("Back");
        backButton.setBounds(150, 410, 100, 30);
        backButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        CommandUI.main(null);
                        frame.dispose();
                    }
                });
            }
        });
        panel.add(backButton);

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
        EmailUI ui = new EmailUI();
        ui.show();
    }
}
