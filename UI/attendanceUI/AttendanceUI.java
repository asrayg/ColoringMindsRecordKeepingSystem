package attendanceUI;

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

public class AttendanceUI {
    private JFrame frame;
    private JButton addAttendanceButton;
    private JButton deleteAttendanceButton;
    private JButton viewAttendanceButton;
    private JButton backButton;
    
    public AttendanceUI() {
        checkLockStatusOfSystem("password.txt");

        initialize();
    }

    private void initialize() {
        frame = new JFrame("CMRKS Attendance Menu");
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

        JLabel titleLabel = new JLabel("Attendance Menu");
        titleLabel.setBounds(0, 0, 400, 20); 
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        titleLabel.setForeground(new Color(173, 239, 209)); 
        highlightPanel.add(titleLabel); 
        panel.add(highlightPanel);

        addAttendanceButton = new JButton("Add");
        addAttendanceButton.setBounds(50, 150, 300, 40);
        addAttendanceButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        addAttendanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AttendanceRecordsUI.main(null);
				frame.dispose();
            }
        });
        panel.add(addAttendanceButton);

        deleteAttendanceButton = new JButton("Delete");
        deleteAttendanceButton.setBounds(50, 200, 300, 40);
        deleteAttendanceButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        deleteAttendanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DeleteAttendanceUI.main(null);
				frame.dispose();
            }
        });
        panel.add(deleteAttendanceButton);

        viewAttendanceButton = new JButton("View");
        viewAttendanceButton.setBounds(50, 250, 300, 40);
        viewAttendanceButton.setFont(new Font("Verdana", Font.PLAIN, 13));
        viewAttendanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ViewAttendanceUI.main(null);
				frame.dispose();
            }
        });
        panel.add(viewAttendanceButton);

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
        AttendanceUI ui = new AttendanceUI();
        ui.show();
    }
}
