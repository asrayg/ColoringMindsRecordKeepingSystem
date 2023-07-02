package mainUI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 * @author      Asray Gopa
 * @version     1.00
 * @since       June 10th, 2023
 */

public class StartupPageUI {
    private static final String PASSWORD_FILE = "password.txt";
    private static final String REGISTER_PAGE = "Register Password";
    private static final String LOGIN_PAGE = "Login Page";
    private static JFrame frame;
    private static CardLayout cardLayout;
    private static JPanel cardPanel;
    private static JButton registerButton;
    private static JButton loginButton;

    public static void main(String[] args) {
    	checkLockStatusOfSystem("password.txt");
    	createUI();
    }

    private static void createUI() {
        frame = new JFrame("Password System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        File passwordFile = new File(PASSWORD_FILE);
        File file = new File("attendance.txt");
        File file1 = new File("fees.txt");
        File file2 = new File("payment.txt");
        File file3 = new File("record.txt");
        
        if (passwordFile.length() == 0 && file.length() == 0 && file1.length() == 0&& file2.length() == 0 && file3.length() == 0) {
            createRegisterPage();
        } else {
            createLoginPage();
        }

        frame.getContentPane().add(cardPanel);
        frame.setVisible(true);
    }

    private static void createRegisterPage() {
    	frame = new JFrame("CMRKS Password Registration ");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(400, 485);
    	frame.setLocationRelativeTo(null);

    	cardLayout = new CardLayout();
    	cardPanel = new JPanel(cardLayout);

    	
    	JPanel registerPanel = new JPanel();
    	
    	
    	ImageIcon imageIcon = new ImageIcon("c_minds.png");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(50, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        registerPanel.add(imageLabel);

        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(0, 105, 400, 30); 
        highlightPanel.setBackground(new Color(0, 32, 63)); 
        highlightPanel.setLayout(null);

        JLabel titleLabel = new JLabel("Register Password");
        titleLabel.setBounds(0, 0, 400, 20); 
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setForeground(new Color(173, 239, 209)); 
        highlightPanel.add(titleLabel); 
        registerPanel.add(highlightPanel);
    	
    	
    	
    	
    	int y =30;
    	registerPanel.setLayout(null);
    	JLabel label = new JLabel("New Password");
    	label.setFont(new Font("Verdana", Font.BOLD, 13));
    	final JPasswordField passwordField = new JPasswordField(20);
    	JLabel confirmLabel = new JLabel("Confirm password");
    	confirmLabel.setFont(new Font("Verdana", Font.BOLD, 13));
    	final JPasswordField confirmPasswordField = new JPasswordField(20);
    	registerButton = new JButton("Register");
    	registerButton.setBounds(160, 330-y, 80, 25);

    	registerPanel.add(registerButton);
    	registerButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        char[] passwordChars = passwordField.getPassword();
    	        String password = new String(passwordChars);
    	        char[] confirmPasswordChars = confirmPasswordField.getPassword();
    	        String confirmPassword = new String(confirmPasswordChars);

    	        if (password.equals(confirmPassword)) {
    	            if (password.length() > 6) {
    	                int confirmation = JOptionPane.showConfirmDialog(frame, "Are you sure you want to register this password?", "Confirmation", JOptionPane.YES_NO_OPTION);
    	                if (confirmation == JOptionPane.YES_OPTION) {
    	                    registerPassword(password);
    	                    Arrays.fill(passwordChars, '0');
    	                    Arrays.fill(confirmPasswordChars, '0');
    	                    passwordField.setText("");
    	                    confirmPasswordField.setText("");
    	                }
    	            } else {
    	                JOptionPane.showMessageDialog(frame, "Password should be at least 7 characters long.", "Invalid Password", JOptionPane.ERROR_MESSAGE);
    	                Arrays.fill(passwordChars, '0');
    	                Arrays.fill(confirmPasswordChars, '0');
    	                passwordField.setText("");
    	                confirmPasswordField.setText("");
    	            }
    	        } else {
    	            JOptionPane.showMessageDialog(frame, "Passwords do not match. Please try again.", "Password Mismatch", JOptionPane.ERROR_MESSAGE);
    	            Arrays.fill(passwordChars, '0');
    	            Arrays.fill(confirmPasswordChars, '0');
    	            passwordField.setText("");
    	            confirmPasswordField.setText("");
    	        }
    	    }
    	});


    	label.setBounds(144, 195-y, 150, 20);
    	passwordField.setBounds(124, 225-y, 150, 20);
    	confirmLabel.setBounds(133, 260-y, 150, 20);
    	confirmPasswordField.setBounds(124, 290-y, 150, 20);

    	registerPanel.add(label);
    	registerPanel.add(passwordField);
    	registerPanel.add(confirmLabel);
    	registerPanel.add(confirmPasswordField);
    	registerPanel.add(registerButton);
    	registerPanel.setBackground(new Color(37, 150, 190));

    	
    	JButton exitButton = new JButton("Exit");
    	exitButton.setBounds(150, 410, 100, 30);
        exitButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        registerPanel.add(exitButton);
        
    	cardPanel.add(registerPanel, REGISTER_PAGE);

    	frame.getContentPane().add(cardPanel);
    	frame.setVisible(true);


    }

    
    private static void createLoginPage() {
    	frame = new JFrame("Coloring Minds Record Keeping System Log-In Page");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(400, 485);
    	frame.setLocationRelativeTo(null);

    	
    	
    	cardLayout = new CardLayout();
    	cardPanel = new JPanel(cardLayout);

    	JPanel loginPanel = new JPanel();
    	
    	ImageIcon imageIcon = new ImageIcon("c_minds.png");
        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(50, 0, imageIcon.getIconWidth(), imageIcon.getIconHeight());
        loginPanel.add(imageLabel);

        JPanel highlightPanel = new JPanel();
        highlightPanel.setBounds(0, 105, 400, 30); 
        highlightPanel.setBackground(new Color(0, 32, 63)); 
        highlightPanel.setLayout(null);
        int y = 25;
        JLabel titleLabel = new JLabel("Log-In");
        titleLabel.setBounds(0, 0, 400, 20); 
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setForeground(new Color(173, 239, 209)); 
        highlightPanel.add(titleLabel); 
        loginPanel.add(highlightPanel);
        
    	loginPanel.setLayout(null);
    	JLabel label = new JLabel("Enter password");
    	final JPasswordField passwordField = new JPasswordField(20);
    	loginButton = new JButton("Login");

    	loginButton.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent e) {
    	        @SuppressWarnings("deprecation")
				String password = passwordField.getText();
    	        verifyPassword(password);
    	    }
    	});
    	
    	label.setBounds(144, 195-y, 150, 20);
    	passwordField.setBounds(97, 232-y, 200, 20);
    	loginButton.setBounds(160, 270-y, 80, 25);
    	label.setFont(new Font("Verdana", Font.BOLD, 13));

    	loginPanel.add(label);
    	loginPanel.add(passwordField);
    	loginPanel.add(loginButton);
    	loginPanel.setBackground(new Color(37, 150, 190));

        JButton exitButton = new JButton("Exit");
    	exitButton.setBounds(150, 410, 100, 30);
        exitButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        loginPanel.add(exitButton);
        
        JButton resetPassword = new JButton("Reset");
        resetPassword.setBounds(160, 310-y, 80, 25);
        resetPassword.setFont(new Font("Verdana", Font.PLAIN, 13));
        resetPassword.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ResetPasswordUI.main(null);
                frame.dispose();
            }
        });
        loginPanel.add(resetPassword);
        
    	cardPanel.add(loginPanel, LOGIN_PAGE);

    	frame.getContentPane().add(cardPanel);
    	frame.setVisible(true);


    }

    private static void registerPassword(String password) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(PASSWORD_FILE))) {
            writer.println(password);
            writer.println("0");
            writer.println("unlocked");
            CommandUI.main(null);
            // Switch to the login page
            frame.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void verifyPassword(String passwordCheck) {
        String storedPassword = getPasswordFromPasswordFile();
        int counter = getNumber();

        if (counter >= 5) {
            String message = "Too many incorrect attempts. System has been locked. Call customer support to reopen.";
            JLabel messageLabel = new JLabel(message);
            JOptionPane.showMessageDialog(frame, messageLabel, "System Locked", JOptionPane.ERROR_MESSAGE);
            lockFile();
            return;
        } else if (!passwordCheck.equals(storedPassword)) {
            String message = "Password is incorrect. Try again. Attempts remaining: " + (5 - counter);
            JLabel messageLabel = new JLabel(message);
            JOptionPane.showMessageDialog(frame, messageLabel, "Password Incorrect", JOptionPane.ERROR_MESSAGE);
            incrementCounter(counter);
            return;
        }

        resetCounter();
        CommandUI.main(null);
        frame.dispose();
    }


    private static void lockFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String[] lines = new String[3];
            for (int i = 0; i < 3; i++) {
                lines[i] = br.readLine();
            }
            lines[1] = "0";
            lines[2] = "locked";

            try (PrintWriter writer = new PrintWriter(new FileWriter(PASSWORD_FILE))) {
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int getNumber() {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String line;
            boolean isFirstLine = true;
            int lineIndex = 0;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                } else if (lineIndex == 1) {
                    return Integer.parseInt(line);
                }

                lineIndex++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static void incrementCounter(int counter) {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String[] lines = new String[3];
            for (int i = 0; i < 3; i++) {
                lines[i] = br.readLine();
            }

            lines[1] = Integer.toString(counter + 1); // Increment the counter by 1

            try (PrintWriter writer = new PrintWriter(new FileWriter(PASSWORD_FILE))) {
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void resetCounter() {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String[] lines = new String[3];
            for (int i = 0; i < 3; i++) {
                lines[i] = br.readLine();
            }

            lines[1] = "0";

            try (PrintWriter writer = new PrintWriter(new FileWriter(PASSWORD_FILE))) {
                for (String line : lines) {
                    writer.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getPasswordFromPasswordFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

}
