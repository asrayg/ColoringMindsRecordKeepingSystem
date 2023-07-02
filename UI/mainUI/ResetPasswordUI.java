package mainUI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@SuppressWarnings("serial")
public class ResetPasswordUI extends JFrame {
    private JTextField passwordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmNewPasswordField;
    private JButton resetButton;

    public ResetPasswordUI() {
        checkLockStatusOfSystem("password.txt");

        setTitle("CMKTS Reset Password");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 485);
        setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(null);

        ImageIcon imageIcon = new ImageIcon("c_minds.png");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(50, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        loginPanel.add(imageLabel);

        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(0, 105, 400, 30);
        highlightPanel.setBackground(new Color(0, 32, 63));
        highlightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Reset Password");
        titleLabel.setBounds(0, 0, 400, 20);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setForeground(new Color(173, 239, 209));
        highlightPanel.add(titleLabel);
        loginPanel.add(highlightPanel);

        JLabel passwordLabel = new JLabel("Current Password");
        passwordLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        passwordField = new JTextField();
        JLabel newPasswordLabel = new JLabel("New Password");
        newPasswordLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        newPasswordField = new JPasswordField();
        JLabel confirmNewPasswordLabel = new JLabel("Confirm Password");
        confirmNewPasswordLabel.setFont(new Font("Verdana", Font.BOLD, 12));
        confirmNewPasswordField = new JPasswordField();

        passwordField = new JPasswordField(20);
        resetButton = new JButton("Reset");

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetPassword();
            }
        });

        passwordField.setBounds(97, 232, 200, 20);
        resetButton.setBounds(160, 310, 80, 25);

        loginPanel.add(passwordField);
        loginPanel.add(resetButton);
        loginPanel.setBackground(new Color(37, 150, 190));

        JButton exitButton = new JButton("Back");
        exitButton.setBounds(150, 410, 100, 30);
        exitButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                StartupPageUI.main(null);
                dispose();
            }
        });
        loginPanel.add(exitButton);
        int y = 130;
        passwordLabel.setBounds(30, 300-y, 150, 20);
        passwordField.setBounds(180, 300-y, 193, 20);
        newPasswordLabel.setBounds(51, 350-y, 150, 20);
        newPasswordField.setBounds(180, 350-y, 193, 20);
        confirmNewPasswordLabel.setBounds(28, 380-y, 150, 20);
        confirmNewPasswordField.setBounds(180, 380-y, 193, 20);

        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(newPasswordLabel);
        loginPanel.add(newPasswordField);
        loginPanel.add(confirmNewPasswordLabel);
        loginPanel.add(confirmNewPasswordField);

        getContentPane().add(loginPanel);
        setVisible(true);
    }

    private void resetPassword() {
        String password = passwordField.getText();

        if (!password.equals(getPasswordFromPasswordFile())) {
            JOptionPane.showMessageDialog(this, "Incorrect current password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char[] newPasswordChars = newPasswordField.getPassword();
        String newPassword = new String(newPasswordChars);
        char[] confirmNewPasswordChars = confirmNewPasswordField.getPassword();
        String confirmNewPassword = new String(confirmNewPasswordChars);

        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(this, "New password must be at least 6 characters long.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (newPassword.equals(password)) {
            JOptionPane.showMessageDialog(this, "New password cannot be the same as the current password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to reset the password?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            replaceFirstLine("password.txt", newPassword);
            JOptionPane.showMessageDialog(this, "Password reset successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        StartupPageUI.main(null);
        dispose();
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


    private static void replaceFirstLine(String passwordFile, String newFirstLine) {
        try {
            Path filePath = Paths.get(passwordFile);
            List<String> lines = Files.readAllLines(filePath);

            if (!lines.isEmpty()) {
                lines.set(0, newFirstLine);
                Files.write(filePath, lines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getPasswordFromPasswordFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("password.txt"))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ResetPasswordUI frame = new ResetPasswordUI();
                frame.setVisible(true);
            }
        });
    }
}
