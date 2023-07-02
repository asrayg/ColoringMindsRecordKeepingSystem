package paymentUI;

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

public class PaymentUI {
    private JFrame frame;
    private JButton inputPaymentButton;
    private JButton deletePaymentButton;
    private JButton viewPaymentButton;
    private JButton backButton;
    
    public PaymentUI() {
        checkLockStatusOfSystem("password.txt");

    	initialize();
    }

    private void initialize() {
        frame = new JFrame("CMRKS Payment Menu");
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

        JLabel titleLabel = new JLabel("Payment Menu");
        titleLabel.setBounds(0, 0, 400, 20); 
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setForeground(new Color(173, 239, 209)); 
        highlightPanel.add(titleLabel); 
        panel.add(highlightPanel);

        inputPaymentButton = new JButton("Input");
        inputPaymentButton.setBounds(50, 150, 300, 40);
        inputPaymentButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        inputPaymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                NewPaymentUI.main(null);
				frame.dispose();
            }
        });
        panel.add(inputPaymentButton);

        deletePaymentButton = new JButton("Delete");
        deletePaymentButton.setBounds(50, 200, 300, 40);
        deletePaymentButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        deletePaymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeletePaymentUI.main(null);
				frame.dispose();
            }
        });
        panel.add(deletePaymentButton);

        viewPaymentButton = new JButton("View");
        viewPaymentButton.setBounds(50, 250, 300, 40);
        viewPaymentButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        viewPaymentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewPaymentUI.main(null);
                frame.dispose();
            }
        });
        panel.add(viewPaymentButton);

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
        PaymentUI ui = new PaymentUI();
        ui.show();
    }
}
