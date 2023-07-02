package emailUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@SuppressWarnings("serial")
public class GenerateBalanceEmailUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;

    public GenerateBalanceEmailUI() {
        checkLockStatusOfSystem("password.txt");

        setTitle("CMRKS Generate Balance Email");
        setSize(400, 485);
        getContentPane().setBackground(new Color(37, 150, 190));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        int y = 61;
        int z = 50;
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        emailLabel.setBounds(y + 10, z + 120, 100, 20);
        emailField = new JTextField();
        emailField.setBounds(y + 90, z + 120, 180, 20);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        passwordLabel.setBounds(y + 10, z + 150, 100, 20);
        passwordField = new JPasswordField();
        passwordField.setBounds(y + 90, z + 150, 180, 20);


        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        JButton generateButton = new JButton("Generate");
        generateButton.setBounds(254, 240, 80, 25);
        panel.add(generateButton);

        generateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                if (isValidEmail(email)) {
                    int confirmation = JOptionPane.showConfirmDialog(GenerateBalanceEmailUI.this,
                            "Are you sure you want to send balance emails?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        generateEmail(email, password);
                        JOptionPane.showMessageDialog(GenerateBalanceEmailUI.this, "Emails sent successfully!");
                    }
                } else {
                    JOptionPane.showMessageDialog(GenerateBalanceEmailUI.this, "Invalid email format.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton cancelButton = new JButton("Back");
        cancelButton.setBounds(320, 428, 75, 30);
        cancelButton.setBackground(new Color(173, 239, 209));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EmailUI.main(null);
                dispose();
            }
        });
        panel.add(cancelButton);

        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(15, 40, 370, 325);
        highlightPanel.setBackground(new Color(72, 170, 173));
        highlightPanel.setLayout(null);
        panel.add(highlightPanel);
        JPanel highlightPanel1 = new JPanel();
        highlightPanel1.setBounds(0, 422, 400, 325);
        highlightPanel1.setBackground(new Color(173, 239, 209));
        highlightPanel1.setLayout(null);
        panel.add(highlightPanel1);
        panel.setBackground(new Color(37, 150, 190));
        add(panel);
    }

    private boolean isValidEmail(String email) {
        return email.matches(".+@.+\\.com");
    }

    private void generateEmail(final String email, final String password) {
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        session.setDebug(true);

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
                String name = parts[0];
                String to = parts[7];
                String isValid = parts[8];
                int balance = getBalance(name);
                if (isValid.equals("y") && balance > 0) {

                    try {
                        MimeMessage message = new MimeMessage(session);

                        message.setFrom(new InternetAddress(email));

                        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

                        message.setSubject("Coloring Minds: Balance To Be Paid");

                        String text = "Hi,\n\n" +

                                "You have a total balance to pay of Rs. " + balance + ".\n\n"
                                + "This is an auto-generated email. Please contact 81937383388.\n\n" + "Thanks,\n"
                                + "Neelima Gopa";
                        message.setText(text);

                        Transport.send(message);
                        System.out.println("");
                        System.out.println("Message sent successfully");
                    } catch (MessagingException mex) {
                        mex.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getBalance(String name) {
        File inputFile = new File("fees.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;

            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                if (firstLine == true) {
                    firstLine = false;
                    continue;
                }

                String[] parts = line.split(", ");

                String studentName = parts[0];
                String total = parts[2];
                int totalBalance = Integer.parseInt(total);
                if (name.equals(studentName)) {
                    return totalBalance;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
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
                GenerateBalanceEmailUI ui = new GenerateBalanceEmailUI();
                ui.setVisible(true);
            }
        });
    }
}
