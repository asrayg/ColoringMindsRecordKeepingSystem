package feesUI;

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

public class FeesUI {
    private JFrame frame;
    private JButton generateFeesButton;
    private JButton deleteFeesButton;
    private JButton viewIndividualBalanceButton;
    private JButton viewPaymentsButton;
    private JButton backButton;

    public FeesUI() {
        checkLockStatusOfSystem("password.txt");

    	initialize();
    }

    private void initialize() {
        frame = new JFrame("CMRKS Fees Menu");
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

        JLabel titleLabel = new JLabel("Fees Menu");
        titleLabel.setBounds(0, 0, 400, 20); 
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setForeground(new Color(173, 239, 209)); 
        highlightPanel.add(titleLabel); 
        panel.add(highlightPanel);

        generateFeesButton = new JButton("Generate");
        generateFeesButton.setBounds(50, 150, 300, 40);
        generateFeesButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        generateFeesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GenerateFeesUI.main(null);
				frame.dispose();
            }
        });
        panel.add(generateFeesButton);

        deleteFeesButton = new JButton("Delete");
        deleteFeesButton.setBounds(50, 200, 300, 40);
        deleteFeesButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        deleteFeesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeleteFeesUI.main(null);
				frame.dispose();
            }
        });
        panel.add(deleteFeesButton);

        viewIndividualBalanceButton = new JButton("Balance");
        viewIndividualBalanceButton.setBounds(50, 250, 300, 40);
        viewIndividualBalanceButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        viewIndividualBalanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewStudentsThatHaveABalanceUI.main(null);
                frame.dispose();
            }
        });
        panel.add(viewIndividualBalanceButton);

        viewPaymentsButton = new JButton("View");
        viewPaymentsButton.setBounds(50, 300, 300, 40);
        viewPaymentsButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        viewPaymentsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewFeesUI.main(null);
                frame.dispose();
            }
        });
        panel.add(viewPaymentsButton);

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
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        FeesUI ui = new FeesUI();
        ui.show();
    }
}
